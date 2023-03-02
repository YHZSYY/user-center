package com.example.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenter.common.ErrorCode;
import com.example.usercenter.exception.BusinessException;
import com.example.usercenter.model.User;
import com.example.usercenter.service.UserService;
import com.example.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/**
* @author Lyy
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-02-14 22:19:48
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     *  盐值，混淆密码
     */
    private static final String SALT = "lyy";

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1. 校验
        // 非空
        if(StringUtils.isAllBlank(userAccount, userPassword, checkPassword, planetCode)){
            // return -1;
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空！");
        }
        // 账户不小于四位
        if(userAccount.length() < 4){
            // return -1;
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不小于四位！");
        }
        // 密码不小于8位
        if(userPassword.length() < 8 || checkPassword.length() < 8){
            // return -1;
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不小于8位！");
        }
        // 账户不包含特殊字符
        String validPattern = ".*[\\s`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\]+.*";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            // return -1;
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不包含特殊字符！");
        }
        // 密码和校验密码是否一致
        if(!userPassword.equals(checkPassword)){
            // return -1;
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不一致！");
        }
        // 星球编号不大于5位
        if(planetCode.length() > 5){
            // return -1;
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号不大于5位！");
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccout", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            // return -1;
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该账户已被注册！");
        }
        // 星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            // return -1;
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号已存在！");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 插入数据库
        User user = new User();
        user.setUserAccout(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean result = this.save(user);
        if(!result){
            throw new BusinessException(ErrorCode.DATA_OPERATION_ERROR, "注册失败！");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccout, String userPassword, HttpServletRequest request) {
        // 1. 校验
        // 非空
        if(StringUtils.isAllBlank(userAccout, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空！");
        }
        // 账户不小于四位
        if(userAccout.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不小于四位！");
        }
        // 密码不小于8位
        if(userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不小于8位！");
        }
        // 账户不包含特殊字符
        String validPattern = ".*[\\s`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\]+.*";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccout);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不包含特殊字符！");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 判断用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccout", userAccout);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            log.info("user login failed, userAccout cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码不正确！");
        }
        // 判断密码是否输入
        // 3. 用户信息脱敏
        User safetyUser = getSafetyUser(user);
        // 4. 记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public Integer userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }


    @Override
    public User getSafetyUser(User user){
        if (user == null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserName(user.getUserName());
        safetyUser.setUserAccout(user.getUserAccout());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setPlanetCode(user.getPlanetCode());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());

        return safetyUser;
    }
}




