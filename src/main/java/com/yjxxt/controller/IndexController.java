package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.bean.User;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.CookieUtil;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping("index")
    public String  index(){
        return  "index";
    }

    @RequestMapping("welcome")
    public String  welcome(){
        return  "welcome";
    }

    @RequestMapping("main")
    public String  main(HttpServletRequest req){
        //获取用户信息,从Cookie获取用户用户的ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //根据ID查询用户信息
        User user = userService.selectByPrimaryKey(userId);
        //存储User
        req.setAttribute("user",user);
        //返回目标试图
        return  "main";
    }


}
