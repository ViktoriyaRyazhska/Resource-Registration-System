package org.registrator.community.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;

@Controller
public class ChatController {

    @RequestMapping("/chat")
    @PreAuthorize("hasRole('ROLE_ANONYMOUS') or hasRole('ROLE_ADMIN')")
    public String chat() {
        return "chat";
    }

    @RequestMapping("/getSessionId")
    @ResponseBody
    public String getSessionId() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }
}

