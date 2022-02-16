package com.nikolay.task4.controller;

import com.nikolay.task4.model.Message;
//import com.nikolay.task4.repo.MessageRepository;
import com.nikolay.task4.service.NotificationService;
import com.nikolay.task4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class MessageController {

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

//    @Autowired
//    MessageRepository messageRepository;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/message")
    public String showTable(Model model, HttpServletRequest httpServletRequest) {
//        Iterable<Message> messages = messageRepository.findAllByUsername(httpServletRequest.getRemoteUser());
//        model.addAttribute("messages", messages);
        return "message-main";
    }

//    @PostMapping("/message")
//    public String sendMessage(@RequestParam String receiver, @RequestParam String subject, @RequestParam String full_text, Model model, HttpServletRequest httpServletRequest) {
//        Message message = new Message(httpServletRequest.getRemoteUser(), receiver, subject, full_text);
//        messageRepository.save(message);
//        return "redirect:/message";
//    }


    @MessageMapping("/private-message")
    //@SendToUser("/topic/private-messages")
    public void getPrivateMessage(final Message message, Principal principal) throws InterruptedException {
        //Thread.sleep(1000);
        message.setSender(principal.getName());
        notificationService.sendPrivateNotification(message.getReceiver());
        messagingTemplate.convertAndSendToUser(message.getReceiver(), "/topic/private-messages", message);
        //return new Message(HtmlUtils.htmlEscape(message.getFull_text()), HtmlUtils.htmlEscape(message.getReceiver()));
    }

}
