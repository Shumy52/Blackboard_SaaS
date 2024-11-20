package org.shumy.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("message", "Hello+World!");
        return "view/hello";
    }
    @GetMapping("/")
    public String redirectToHello() {
        return "redirect:/hello";
    }

    @GetMapping("/blackboard")
    public String showBlackboard() {
        return "view/blackboard";     // Spring will look for blackboard.html in src/main/resources/templates
    }

}
