package com.example.usercentertest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercentertest.common.ErrorCode;
import com.example.usercentertest.constant.UserConstant;
import com.example.usercentertest.exception.BusinessException;
import com.example.usercentertest.service.UserService;
import com.example.usercentertest.model.domain.User;
import com.example.usercentertest.mapper.UserMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.usercentertest.constant.UserConstant.ADMIN_ROLE;
import static com.example.usercentertest.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author Volder
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2025-09-28 14:21:02
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    private static final String SALT = "xcstar";
    @Autowired
    private UserMapper userMapper;
    @Override
    public long userRegister(String userAccount, String password, String checkPassword, String planetCode) {
        // 1、校验
        if (StringUtils.isAllBlank(userAccount,password,checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户长度过短");
        }
        if(password.length() < 8 || checkPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过短");
        }
        if (planetCode.length()>5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编码长度过长");
        }

        // 账户不能含有特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            return -1;
        }
        // 二次密码输入是否正确
        if(!password.equals(checkPassword)){
            return -1;
        }

        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);

        long count = userMapper.selectCount(queryWrapper);
        if (count > 0){
            return -1;
        }
        // 星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode",planetCode);
        count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            return -1;
        }

        // 2、对密码进行加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT+password).getBytes());

        // 3、向用户插入数据
        User user = new User();
        user.setUseraccount(userAccount);
        user.setUserpassword(newPassword);
        user.setPlanetcode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult){
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLongin(String userAccount, String password, HttpServletRequest request) {
        if (StringUtils.isAllBlank(userAccount,password)) {
            return null;
        }
        if(userAccount.length()<4){
            return null;
        }
        if(password.length() < 8){
            return null;
        }
        // 账户不能含有特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            return null;
        }
        // 2、对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+password).getBytes());
        // 3、插入数据
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }
        // 用户脱敏
        getSafetyUser(user);

        // 记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE,user);
        return user;
    }

    @Override
    public User getSafetyUser(User user){
        if (user == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUseraccount(user.getUseraccount());
        safetyUser.setAvatarurl(user.getAvatarurl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setPlanetcode(user.getPlanetcode());
        safetyUser.setUserstatus(user.getUserstatus());
        safetyUser.setCreatetime(user.getCreatetime());
        safetyUser.setUserrole(user.getUserrole());
        safetyUser.setTags(user.getTags());
        return safetyUser;
    }

    @Override
    public List<User> searchUsers(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNoneBlank(username)) {
            queryWrapper.like("username",username);
        }
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public boolean removeById(long id) {
        return false;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public List<User> searchUsersByTag(List<String> tagNameList) {
        /*
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        for (String tagName : tagNameList) {
            queryWrapper.like("tags",tagName);
        }
        List<User> userList = userMapper.selectList(queryWrapper);

        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
         */
        //先查询所有用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        return userList.stream().filter(user -> {
            String tags = user.getTags();
            Set<String> tempTagNameList = gson.fromJson(tags,new TypeToken<Set<String>>(){}.getType());
            tempTagNameList = Optional.ofNullable(tempTagNameList).orElse(new HashSet<>());
            for (String tagName : tagNameList) {
                if (!tempTagNameList.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
    }

    @Override
    public int updateUser(User user,User loginUser) {
        // 判断权限，仅管理员和自己可以修改
        Long userId = user.getId();
        if(userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 如果是管理员可以更新所有用户
        // 如果是普通用户只可以更新自己
        if(!isAdmin(user) && userId.equals(loginUser.getId())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User oldUser = userMapper.selectById(userId);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userMapper.updateById(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return (User) userObj;
    }

    /**
     * 是否为管理员
     *
     * @param request 请求
     * @return 0为管理员 1为普通用户
     */
    @Override
    public boolean isAdmin(HttpServletRequest request){
        User user = (User)(request.getSession().getAttribute(USER_LOGIN_STATE));
        return user != null || user.getUserrole() == UserConstant.ADMIN_ROLE;
    }
    /**
     * 是否为管理员
     *
     * @param loginUser 登录请求
     * @return 0为管理员 1为普通用户
     */
    public boolean isAdmin(User loginUser){
        return loginUser != null && loginUser.getUserrole() == UserConstant.ADMIN_ROLE;
    }

}




