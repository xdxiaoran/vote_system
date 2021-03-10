package com.xdxiaoran.controller;

import com.xdxiaoran.service.WXServerListener;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController {
//    @Autowired
//    WXServerListener wxServerListener;
//    @RequestMapping("hello")
//    public String hello(){
////        wxServerListener.sendTextMsg("fileHelper","test");
//        return  wxServerListener.getRooms().toString();
    @RequestMapping("/")
    public String index(){
        return "index";
    }
    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }
    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }
//    }
}
