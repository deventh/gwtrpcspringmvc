package org.gspring.mvc.sample.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class PageController {
	@RequestMapping("/index")
     String index() {
        return "Index";
    }

    @RequestMapping("/secured")
    String secured() {
        return "Secured";
    }

    @RequestMapping("/login")
    String login() {
        return "Login";
    }

    @RequestMapping("/failed_login")
    String failedLogin() {
        return "FailedLogin";
    }
}
