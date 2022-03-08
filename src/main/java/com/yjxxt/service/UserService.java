package com.yjxxt.service;

import com.github.pagehelper.util.StringUtil;
import com.yjxxt.base.BaseService;
import com.yjxxt.bean.User;
import com.yjxxt.mapper.UserMapper;
import com.yjxxt.model.UserModel;
import com.yjxxt.utils.AssertUtil;
import com.yjxxt.utils.Md5Util;
import com.yjxxt.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User, Integer> {

    @Resource
    private UserMapper userMapper;

    //根据用户名密码登录
    public UserModel userLogin(String userName, String userPwd) {
        //校验用户和密码
        checkLoginParams(userName, userPwd);
        //查询用户是否存在
        User user = userMapper.selectUserByName(userName);
        AssertUtil.isTrue(user == null, "用户名已注销或者不存在");
        //密码校验，加密+比对==
        checkLoginPwd(userPwd,user.getUserPwd());
        //构建返回对象
        return buidlerUserInfo(user);
    }

    /**
     * 构建返回对象的
     * @param user
     * @return
     */
    private UserModel buidlerUserInfo(User user) {
        //实例化对象
        UserModel userModel=new UserModel();
        //加密
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        //返回
        return  userModel;
    }

    /**
     * 校验用户密码，和数据库中密码是否配置
     * @param userPwd
     * @param userPwd1
     */
    private void checkLoginPwd(String userPwd, String userPwd1) {
        //将输入的密码加密
        userPwd=Md5Util.encode(userPwd);
        //比对密码是否正确
        AssertUtil.isTrue(!userPwd.equals(userPwd1),"用户密码不正确");
    }

    /**
     * 验证用户名称和密码的
     *
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "用户名不能为空");
    }


    /**
     * 修改用户的密码
     * @param userId 用户的ID
     * @param oldPassword 原始密码
     * @param newPassword 新密码
     * @param confirmPwd  确认密码
     */
    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String confirmPwd){
        //根据ID查询用户信息
        User user = userMapper.selectByPrimaryKey(userId);
        //验证
        checkUserPasswordParams(user,oldPassword,newPassword,confirmPwd);
        //加密，修改用户密码
        user.setUserPwd(Md5Util.encode(newPassword));
        //判断修改是否成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败了");
    }

    /**
     * 验证用户的密码参数
     * @param user 当前用户对象
     * @param oldPassword
     * @param newPassword
     * @param confirmPwd
     */
    private void checkUserPasswordParams(User user, String oldPassword, String newPassword, String confirmPwd) {
        //当前用户是否存储
        AssertUtil.isTrue(user==null,"用户未登录或者已经注销");
        //原始密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码");
        //原始密码和数据中的密码（加密）一致
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPassword)),"原始密码不正确");
        //新密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"请输入新密码");
        //新密码和原始密码不能相同
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码和原始不能一样");
        //确认密码，不能为空
        AssertUtil.isTrue(StringUtils.isBlank(confirmPwd),"确认密码不能为空");
        //确认密码和新密码要一致
        AssertUtil.isTrue(!confirmPwd.equals(newPassword),"确认密码和新密码必须一致");
    }


    /**
     * 查询所有销售人员
     * @return
     */
    public List<Map<String,Object>> findSales(){
       return userMapper.selectSales();
    }
}
