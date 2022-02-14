package com.nikolay.task3.controller;

import com.nikolay.task3.model.User;
import com.nikolay.task3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TableController {

    @Autowired
    private UserService userService;

    @PreAuthorize("@userService.validateUser(#httpServletRequest)")
    @GetMapping("/table")
    public String showTable(Model model, HttpServletRequest httpServletRequest) {
        Iterable<User> users = userService.allUsers();
        model.addAttribute("users", users);
        return "table-main";
    }

    @PreAuthorize("@userService.validateUser(#httpServletRequest)")
    @PostMapping(value = "/table/edit", params = "delete")
    public String userDelete(@RequestParam(value = "ids", required = false) List<Long> ids, HttpServletRequest httpServletRequest) {
        if (ids != null) {
            for (Long id : ids) {
                userService.deleteUser(id);
            }
        }
        return "redirect:/table";
    }

    @PreAuthorize("@userService.validateUser(#httpServletRequest)")
    @PostMapping(value = "/table/edit", params = "block")
    public String userBlock(HttpServletRequest request, @RequestParam(value = "ids", required = false) List<Long> ids, HttpServletRequest httpServletRequest) {
        if (ids != null) {
            for (Long id : ids) {
                User newUser = userService.findUserById(id);
                newUser.setUnblocked(false);
                userService.editUser(newUser);
            }
        }
        return "redirect:/table";
    }

    @PreAuthorize("@userService.validateUser(#httpServletRequest)")
    @PostMapping(value = "/table/edit", params = "unblock")
    public String userUnblock(@RequestParam(value = "ids", required = false) List<Long> ids, HttpServletRequest httpServletRequest) {
        if (ids != null) {
            for (Long id : ids) {
                User newUser = userService.findUserById(id);
                newUser.setUnblocked(true);
                userService.editUser(newUser);
            }
        }
        return "redirect:/table";
    }
}