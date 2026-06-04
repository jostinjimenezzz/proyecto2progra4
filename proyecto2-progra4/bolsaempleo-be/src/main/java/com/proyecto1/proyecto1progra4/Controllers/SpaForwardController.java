package com.proyecto1.proyecto1progra4.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaForwardController {

    @RequestMapping(value = {"/", "/login", "/registro/**", "/puestos/**", "/dashboard/**"})
    public String forward() {
        return "forward:/index.html";
    }
}
