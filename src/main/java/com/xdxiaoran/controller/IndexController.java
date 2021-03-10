package com.xdxiaoran.controller;

import com.google.zxing.WriterException;
import com.xdxiaoran.service.WXServerListener;
import com.xdxiaoran.tool.GetDate;
import com.xdxiaoran.tool.QRCodeGenerator;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class IndexController {
    @Autowired
    WXServerListener wxServerListener;
    @Autowired
    QRCodeGenerator qrCodeGenerator;
    @RequestMapping("/hello1")
    public String test() throws IOException, WriterException {
        qrCodeGenerator.getQRCodeImageByUrl("http://localhost:8080/vote/cn/1");

        wxServerListener.sendTextMsg("fileHelper","投票名称：test，请同学们尽快扫码填写");
        wxServerListener.sendImgMsg("fileHelper", (ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/images/QRCodeImage.png").substring(1));
        return wxServerListener.getRooms().toString();
    }
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
    @RequestMapping("/public/cn")
    public ModelAndView pubCn() {
        ModelAndView modelAndView = new ModelAndView("/public/public_cn");
        modelAndView.addObject("YEAR", GetDate.year());
        return modelAndView;
    }
//    }
}
