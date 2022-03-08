package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.bean.User;
import com.yjxxt.model.UserModel;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("login")
    @ResponseBody
    public ResultInfo login(String userName, String userPwd) {
        //实例化对象
        ResultInfo resultInfo = new ResultInfo();
        //登录操作
        UserModel userModel = userService.userLogin(userName, userPwd);
        resultInfo.setResult(userModel);

        //返回目标对象
        return resultInfo;
    }


    @RequestMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest req, String oldPassword, String newPassword, String confirmPwd) {
        //实例化对象
        ResultInfo resultInfo = new ResultInfo();
        //获取当前用户的ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //修改密码
        userService.updateUserPassword(userId, oldPassword, newPassword, confirmPwd);
        //返回目标对象
        return resultInfo;
    }


    @RequestMapping("updateUser")
    @ResponseBody
    public ResultInfo updateUser(User user){
        //修改
        Integer x = userService.updateByPrimaryKeySelective(user);
        //返回目标对象
        return  success("修改成功了");
    }

    @RequestMapping("toPasswordPage")
    public String update() {
        return "user/password";
    }

    @RequestMapping("toSettingPage")
    public String setting(HttpServletRequest req) {
        //从Cookie获取用户的ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //调用方法查询用户信息
        User user = userService.selectByPrimaryKey(userId);
        //存储数据
        req.setAttribute("user",user);
        //转发
        return "user/setting";
    }


    @RequestMapping("sales")
    @ResponseBody
    public List<Map<String,Object>> saySales(){
        return  userService.findSales();
    }
}
