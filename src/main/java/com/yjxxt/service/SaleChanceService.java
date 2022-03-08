package com.yjxxt.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.base.BaseService;
import com.yjxxt.bean.SaleChance;
import com.yjxxt.mapper.SaleChanceMapper;
import com.yjxxt.query.SaleChanceQuery;
import com.yjxxt.utils.AssertUtil;
import com.yjxxt.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {


    @Resource
    private SaleChanceMapper saleChanceMapper;


    /*条件查询*/

    public Map<String,Object> selectSaleChanceByParam(SaleChanceQuery query){

        //初始化条件
        PageHelper.startPage(query.getPage(),query.getLimit());
        //没有分页集合数据
        List<SaleChance> list = saleChanceMapper.selectByParams(query);
        //分页好的集合数据
        PageInfo<SaleChance> slist=new PageInfo<SaleChance>(list);
        System.out.println(slist);
        //实例化Map
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("msg","success");
        map.put("count",slist.getTotal());
        map.put("data",slist.getList());
        //返回目标map
        return  map;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        //验证
        checkSaleChanceParam(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //设定默认值
        //未分配
        if(StringUtils.isBlank(saleChance.getAssignMan())){
//            未分配
            saleChance.setState(0);
            //未开发
            saleChance.setDevResult(0);
        }
        //已经分配
        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        //判断是否修改成功
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"添加失败了");
    }

    /**
     * 验证方法
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkSaleChanceParam(String customerName, String linkMan, String linkPhone) {
        //客户名非空
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名不能为空");
        //联系人非空
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        //电话非空，是合法手机号
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"手机号不能为空");
        //是合法手机号
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"请输入合法的手机号");
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        //验证当前用户ID是否存在
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(temp==null,"带修改记录不存在");
        //验证参数
        checkSaleChanceParam(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //设定默认值
        //未分配
        if(temp.getAssignMan()==null && StringUtils.isNotBlank(saleChance.getAssignMan())){
            //已经分配
            saleChance.setState(1);
            //正在开发中
            saleChance.setDevResult(1);
            //分配时间
            saleChance.setAssignTime(new Date());
        }else if(temp.getAssignMan()!=null && StringUtils.isBlank(saleChance.getAssignMan())){
            //已经分配--->未分配
            saleChance.setState(0);
            saleChance.setDevResult(0);
            //分配时间用原来的时间
            saleChance.setAssignTime(null);
            saleChance.setAssignMan("");
        }
        //已经分配
        //判断是否修改成功
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"修改失败了");
    }



    public void deleteSaleChances(Integer[] ids){
        //非空验证
        AssertUtil.isTrue(ids==null || ids.length==0,"请求选择数据");
        //批量删除
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)!=ids.length,"删除失败");
    }
}
