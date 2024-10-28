package com.example.shared_blackboard.controller;

import com.example.shared_blackboard.model.DrawAction;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DrawController {

    @MessageMapping("/draw") // Clients will send messages here
    @SendTo("/topic/updates") // Broadcast messages here
    public DrawAction handleDrawAction(DrawAction action) {
        return action; // Sends draw action back to all clients
    }
}
