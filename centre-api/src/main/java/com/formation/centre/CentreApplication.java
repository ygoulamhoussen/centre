package com.formation.centre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class CentreApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CentreApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CentreApplication.class);
    }

    // Fallback controller to serve the UI with valid mappings
    @Controller
    static class ForwardController {
        @RequestMapping(value = { "/", "/{path:[^\\.]*}", "/**/{path:[^\\.]*}" })
        public String forward() {
            return "forward:/index.html";
        }

        // Removed /error mapping to avoid conflict with BasicErrorController
        // @RequestMapping("/error")
        // public String handleError() {
        //     return "forward:/index.html";
        // }
    }
}
