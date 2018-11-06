package com.geelaro.register.web;

import com.geelaro.register.domain.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/register")
    public String createUser(Model model){
        model.addAttribute("user",new UserDto());
        return "register";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("user",new UserDto());
        return "login";
    }

    @GetMapping("/login?error")
    public String loginError(Model model){
        model.addAttribute("loginError",true);
        return "login";
    }

    @ResponseBody
    @GetMapping("/hello")
    public Map<String,String > hello(){

        Map<String,String> map = new HashMap<>();
        map.put("data","Hello Spring Security.");
        map.put("status","SUCCESS");
        return map;
    }

}
