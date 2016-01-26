package com.deicos.desktop.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Lince_v2
 * com.deicos.lince.controller
 * Created by alber in 26/01/2016.
 * Description:
 */
@Controller
public class GreetingController {

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", StringUtils.defaultString(name,"pepito"));
        return "/templates/greeting";
    }
}
