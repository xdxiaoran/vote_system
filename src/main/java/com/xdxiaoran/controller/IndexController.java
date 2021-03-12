package com.xdxiaoran.controller;

import com.google.zxing.WriterException;
import com.xdxiaoran.mapper.VoteMapper;
import com.xdxiaoran.pojo.Vote;
import com.xdxiaoran.pojo.WXChatRoom;
import com.xdxiaoran.service.WXServerListener;
import com.xdxiaoran.tool.GetDate;
import com.xdxiaoran.tool.QRCodeGenerator;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    WXServerListener wxServerListener;
    @Autowired
    QRCodeGenerator qrCodeGenerator;
    @Autowired
    VoteMapper voteMapper;
    @RequestMapping("/sendvote")
    public String test(String wxid,int vid) throws IOException, WriterException, InterruptedException {
        qrCodeGenerator.getQRCodeImageByUrl("http://localhost:8080/vote/cn/"+vid);
        Vote vote = voteMapper.getVote(vid);
        wxServerListener.sendTextMsg("fileHelper","投票名称"+vote.getTitle()+"请同学们尽快扫码填写");
        wxServerListener.sendImgMsg("fileHelper", (ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/images/QRCodeImage.png").substring(1));
        Thread.sleep(500);
        return "redirect:/votes/0";
    }
    @RequestMapping("/charmlist")
    public String charmlist(Model model,int vid) throws IOException, WriterException {
        List<WXChatRoom> rooms = wxServerListener.getRooms();
        model.addAttribute("rooms",rooms);
        model.addAttribute("vid",vid);
        return "charmlist";
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
