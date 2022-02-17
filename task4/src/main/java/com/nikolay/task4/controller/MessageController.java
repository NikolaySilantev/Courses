package com.nikolay.task4.controller;

import com.nikolay.task4.model.Message;
import com.nikolay.task4.model.User;
import com.nikolay.task4.repo.MessageRepository;
import com.nikolay.task4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.Timestamp;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserService userService;

    @GetMapping("/message")
    public String showTable(Model model, HttpServletRequest httpServletRequest) {
        Iterable<Message> messages = messageRepository.findAllByUsername(httpServletRequest.getRemoteUser());
        Iterable<User> users = userService.allUsers();
        model.addAttribute("messages", messages);
        model.addAttribute("users", users);
        return "message-main";
    }

    @MessageMapping("/private-message")
    public void getPrivateMessage(final Message message, Principal principal) {
        message.setSender(principal.getName());
        message.setTime(new Timestamp(System.currentTimeMillis()));
        messageRepository.save(message);
        messagingTemplate.convertAndSendToUser(message.getReceiver(), "/topic/private-messages", message);
    }

}
