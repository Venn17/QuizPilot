package src.quiz.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @RequestMapping("/test")
    public String test(){

        return "test";
    }
    @RequestMapping("/demo1")
    public String demo1(){

        return "demo1";
    }

    @RequestMapping("/demo2")
    public String demo2(){

        return "demo2";
    }
//    @RequestMapping (value={"/","/home"})
//    public String home(){
//
//        return "home";
//    }
}
