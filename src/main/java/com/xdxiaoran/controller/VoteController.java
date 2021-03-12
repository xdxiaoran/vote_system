package com.xdxiaoran.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.util.concurrent.RateLimiter;
import com.xdxiaoran.analyzer.Selection;
import com.xdxiaoran.analyzer.Serialize;
import com.xdxiaoran.limit.IpUtil;
import com.xdxiaoran.limit.LoadingCacheServiceImpl;
import com.xdxiaoran.mapper.VoteMapper;
import com.xdxiaoran.pojo.Vote;
import com.xdxiaoran.tool.GetDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class VoteController {
    @Autowired
    VoteMapper voteMapper;
    @Resource
    LoadingCacheServiceImpl loadingCacheService;


    @RequestMapping("/votes/{pageNum}")
    public String voteList(@PathVariable("pageNum") int pageNum,Model model) {
        PageHelper.startPage(pageNum,8);
        List<Vote> votes=voteMapper.getAllVotes();
        PageInfo<Vote> page = new PageInfo<Vote>(votes);
        model.addAttribute("page",page);
        return "votelist";
    }
    @RequestMapping("/votedeatils/{VID}")
    public ModelAndView voteDetails(@PathVariable Integer VID){
        Vote vote = voteMapper.getVote(VID);
        ModelAndView modelAndView = new ModelAndView("/vote/voteDeTail");
        modelAndView.addObject("VoteID", vote.getVID());
        modelAndView.addObject("Title", vote.getTitle());
        modelAndView.addObject("Describe", vote.getDescribe());
        modelAndView.addObject("Type", vote.getType());
        modelAndView.addObject("Limit", vote.getLimit());
        //Selection process
        List<Map<String, String>> selects = Selection.analyze(vote.getSelection());
        modelAndView.addObject("Selection", selects);
        modelAndView.addObject("YEAR", GetDate.year());
        return modelAndView;
    }
    @RequestMapping("/vote/cn/{VID}")
    public ModelAndView showVoteCN(@PathVariable Integer VID) {
        Vote vote = voteMapper.getVote(VID);
        ModelAndView modelAndView = new ModelAndView("/vote/index_cn");
        modelAndView.addObject("VoteID", vote.getVID());
        modelAndView.addObject("Title", vote.getTitle());
        modelAndView.addObject("Describe", vote.getDescribe());
        modelAndView.addObject("Type", vote.getType());
        modelAndView.addObject("Limit", vote.getLimit());
        //Selection process
        List<Map<String, String>> selects = Selection.analyze(vote.getSelection());
        modelAndView.addObject("Selection", selects);
        modelAndView.addObject("YEAR", GetDate.year());
        return modelAndView;
    }

    @RequestMapping("/checkVoteID/{VID}")
    @ResponseBody
    public Integer checkVoteID(@PathVariable Integer VID) {
        try {
            voteMapper.checkVote(VID).getVID();
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @RequestMapping("/voterSubmit")
    @ResponseBody
    public Integer voterSubmit(String title, String describe, String selection, Integer type, Integer limit) {
        //Verify
        if (title == "") {
            return -7426;
        }
        if (describe == "") {
            return -7426;
        }
        if (limit < -1 || limit == 0) {
            return -7426;
        }
        //Update
        try {
            voteMapper.insertVote(title, describe, selection, type, limit);
            Integer VID = voteMapper.queryVoteID(title, describe, selection, type, limit);
            return VID;
        } catch (Exception E) {
            E.printStackTrace();
            return -1;
        }
    }

    @RequestMapping("/submitVote")
    @ResponseBody
    public String submitVote(HttpServletRequest request, Integer VID, String selected) {
        String ipAddr = IpUtil.getIpAddr(request);
        String ipAndVID = ipAddr + ":" + VID;
        try {
            RateLimiter limiter = loadingCacheService.getRateLimiter(ipAndVID);
            boolean localAccess = false;
            //Localhost is in the WhiteList
            if (ipAddr.equals("0:0:0:0:0:0:0:1")) {
                localAccess = true;
            }
            if (limiter.tryAcquire() || localAccess) {
                //PASS
                //Get VID info
                Vote vote = voteMapper.getVote(VID);
                String selectionSerial = vote.getSelection();
                //Package and readout
                List<Map<String, String>> selects = Selection.analyze(selectionSerial);
                //Split selection
                String[] selectedList = selected.split(",");
                //Convert to Integer
                Integer[] selList = new Integer[selectedList.length];
                Integer i = 0;
                for (String sel : selectedList) {
                    selList[i] = Integer.parseInt(sel);
                    ++i;
                }
                //Write changes
                //Generate new List
                List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
                //Change one by one
                for (int voteFor = 0; voteFor < selects.size(); ++voteFor) {
                    Map<String, String> stringMap = selects.get(voteFor);
                    boolean flag = false;
                    //Submit
                    for (Integer sel : selList) {
                        if (Integer.parseInt(stringMap.get("num")) == sel) {
                            flag = true;
                        }
                    }
                    if (flag) {
                        //Change
                        Integer before = Integer.parseInt(stringMap.get("count"));
                        Integer after = before + 1;
                        stringMap.put("count", String.valueOf(after));
                    }
                    newList.add(stringMap);
                }
                //To serial
                String serial = Serialize.makeSerial(newList);
                //Update to database
                voteMapper.vote(serial, VID);
                return "1";
            } else {
                //DENIED
                return "0";
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "0";
    }
}
