package com.xc.partner.controller;

import com.xc.partner.common.BaseResponse;
import com.xc.partner.common.ErrorCode;
import com.xc.partner.exception.BusinessException;
import com.xc.partner.model.domain.User;
import com.xc.partner.model.domain.request.UserLoginRequest;
import com.xc.partner.model.domain.request.UserRegisterRequest;
import com.xc.partner.service.UserService;
import com.xc.partner.common.ResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.xc.partner.constant.UserConstant.ADMIN_ROLE;
import static com.xc.partner.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUseraccount();
        String userPassword = userRegisterRequest.getUserpassword();
        String checkPassword = userRegisterRequest.getCheckpassword();
        String planetCode = userRegisterRequest.getPlanetcode();
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUseraccount();
        String userPassword = userLoginRequest.getUserpassword();
        if (StringUtils.isAllBlank(userAccount,userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User result = userService.userLongin(userAccount,userPassword,request);
        return ResultUtils.success(result);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long userId= currentUser.getId();
        // 用户校验是否合法
        User user = userService.getById(userId);
        return ResultUtils.success(userService.getSafetyUser(user));
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList = userService.searchUsers(username);
        List<User> result = userList.stream().map(user -> (userService.getSafetyUser(user))).collect(Collectors.toList());
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id,HttpServletRequest request){
        if (!isAdmin(request)){
            return null;
        }
        if(id <= 0){
            return null;
        }
        Boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    private boolean isAdmin(HttpServletRequest request){
        User user = (User)(request.getSession().getAttribute(USER_LOGIN_STATE));
        return user != null || user.getUserrole() == ADMIN_ROLE;
    }
}
