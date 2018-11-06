package com.geelaro.register.web;

import com.geelaro.register.domain.entity.User;
import com.geelaro.register.domain.dto.UserDto;
import com.geelaro.register.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.DateUtils;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

@Controller
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String createUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult result, Model model) {

        if (result.hasErrors()) {

            return "register";
        }
        //check name 是否已使用
        if (userService.checkUserByName(userDto.getName())) {
//            FieldError fieldError  = new FieldError("user","name","name 已存在");
//            result.addError(fieldError);
            result.rejectValue("name", "error.user", "name已使用");
            logger.info("name已存在！");
            return "register";
        }
        //check email 是否已注册。
        if (userService.checkUserByEmail(userDto.getEmail())) {
            result.rejectValue("email", "error.user", "Email已注册");
            logger.info("email已注册！");
            return "register";
        }
        //check password equal
        if (!checkPassWordUniform(userDto.getPassWd(),userDto.getMatchPassWd())){
            result.rejectValue("passWd","error.user","两次输入密码不一致");
            return "register";
        }

        try {
            createUserAccount(userDto);
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("email", "error.user","Email already exists.");
            result.rejectValue("name", "error.user","Name already exists");
        }

        return "redirect:/userInfo";

    }

//    @PostMapping("/login")
//    public ModelAndView login(@ModelAttribute("user")UserDto userDto ,BindingResult result){
//        User user = userService.findByName(userDto.getName());
//        if (user==null){
//            result.rejectValue("name","name Error");
//        }else if (user.getPassWd().equals(passwordEncoder.encode(userDto.getPassWd()))){
//            logger.info("密码正确");
//            logger.info("未加密："+userDto.getPassWd());
//            logger.info("用户密码："+user.getPassWd());
//            return new ModelAndView("userInfo","user",userDto);
//        }
//        return null;
//    }

    @GetMapping("/userInfo")
    public String userInfo(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = userDetails.getUsername();

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        model.addAttribute("name", name);
        model.addAttribute("date", date);
        return "userInfo";
    }

    private User createUserAccount(UserDto userDto) {
        User registered = null;

        registered = userService.registerNewAccount(userDto);
        return registered;
    }

    private boolean checkPassWordUniform(String passWd,String matchPassWd){
        return passWd.equals(matchPassWd);
    }

}
