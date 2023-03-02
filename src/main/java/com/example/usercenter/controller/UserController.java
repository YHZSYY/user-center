package com.example.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usercenter.common.BaseResponse;
import com.example.usercenter.common.ErrorCode;
import com.example.usercenter.common.ResultUtils;
import com.example.usercenter.exception.BusinessException;
import com.example.usercenter.model.User;
import com.example.usercenter.model.request.UserLoginRequest;
import com.example.usercenter.model.request.UserRegisterRequest;
import com.example.usercenter.service.UserService;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.usercenter.common.ErrorCode.*;
import static com.example.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/*
 * @author : LYY
 * @date : 2023/2/15 17:07
 * @description
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if(request == null){
            throw new BusinessException(PARAMS_ERROR, "请求request为null！");
        }
        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);

    }

    @RequestMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest registerRequest){
        if(registerRequest == null){
            throw new BusinessException(PARAMS_ERROR, "请求request为null！");
        }
        String userAccout = registerRequest.getUserAccout();
        String userPassword = registerRequest.getUserPassword();
        String checkPassword = registerRequest.getCheckPassword();
        String planetCode = registerRequest.getPlanetCode();

        if(StringUtils.isAllBlank(userAccout, userPassword, checkPassword, planetCode)){
            throw new BusinessException(NULL_ERROR);
        }
        long result = userService.userRegister(userAccout, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request){
        if (loginRequest == null){
            throw new BusinessException(PARAMS_ERROR, "请求request为null！");
        }
        String userAccout = loginRequest.getUserAccout();
        String userPassword = loginRequest.getUserPassword();
        if(StringUtils.isAllBlank(userAccout, userPassword)){
            throw new BusinessException(NULL_ERROR);
        }
        User user = userService.userLogin(userAccout, userPassword, request);
        return ResultUtils.success(user);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request){
        if(!isAdmin(request)){
            throw new BusinessException(NO_AUTH, "非管理员权限");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("userName", username);
        }
        List<User> list = userService.list(queryWrapper);
        List<User> collect = list.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(collect);

    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrenntUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    @PostMapping("/detele")
    public BaseResponse<Boolean> deteleUser(@RequestBody long id, HttpServletRequest request){
        if(!isAdmin(request)){
            throw new BusinessException(NO_AUTH, "无法删除，需要管理员权限");
        }
        if(id < 0){
            throw new BusinessException(DATA_OPERATION_ERROR, "id错误，删除失败！");
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    public boolean isAdmin(HttpServletRequest request){
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User)userObj;
        return user != null && user.getUserRole() == 1;
    }



}
