package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.bean.SaleChance;
import com.yjxxt.bean.User;
import com.yjxxt.query.SaleChanceQuery;
import com.yjxxt.service.SaleChanceService;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {


    @Autowired
    private SaleChanceService saleChanceService;


    @Resource
    private UserService userService;


    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list(SaleChanceQuery query){
        //查询数据
        Map<String, Object> map = saleChanceService.selectSaleChanceByParam(query);
        //返回目标map--json
        return  map;
    }


    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }

    @RequestMapping("addOrUpdateDialog")
    public String addOrUpdate(Integer id, Model model){
        if(id!=null){
            //根据ID查询对象信息
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            //存储
            model.addAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }


    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(SaleChance saleChance, HttpServletRequest req){
        //获取创建人(当前登录的用户)的Id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
        User user = userService.selectByPrimaryKey(userId);
        //设定当前机会的创建人
        saleChance.setCreateMan(user.getTrueName());
        saleChance.setCreateDate(new Date());
        //添加营销机会对象数据
        saleChanceService.addSaleChance(saleChance);
        //添加结果的判断
        return  success("添加成功了");
    }


    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(SaleChance saleChance){
        //修改
        saleChanceService.updateSaleChance(saleChance);
        //添加结果的判断
        return  success("修改成功了");
    }


    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer [] ids){
        //删除
        saleChanceService.deleteSaleChances(ids);
        //添加结果的判断
        return  success("删除成功了");
    }
}
