package com.example.usercentertest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercentertest.common.ErrorCode;
import com.example.usercentertest.constant.TeamStatusEnum;
import com.example.usercentertest.exception.BusinessException;
import com.example.usercentertest.model.domain.Team;
import com.example.usercentertest.mapper.TeamMapper;
import com.example.usercentertest.model.domain.User;
import com.example.usercentertest.model.domain.UserTeam;
import com.example.usercentertest.model.domain.request.TeamJoinRequest;
import com.example.usercentertest.model.domain.request.TeamUpdateRequest;
import com.example.usercentertest.model.domain.vo.TeamUserVO;
import com.example.usercentertest.model.domain.vo.UserVO;
import com.example.usercentertest.model.dto.TeamQuery;
import com.example.usercentertest.service.TeamService;
import com.example.usercentertest.service.UserService;
import com.example.usercentertest.service.UserTeamService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
* @author Volder
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2025-12-02 20:07:16
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{
    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;


    public TeamServiceImpl(UserTeamService userTeamService) {
        this.userTeamService = userTeamService;
    }

    @Override
    public long addTeam(Team team, User loginUser) {
        // 请求参数是否为空
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 是否登录
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        final Long userId = loginUser.getId();
        // 校验信息
        //  队伍人数大于1 且小于20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if(maxNum < 1 || maxNum > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求");
        }
        // 队伍标题 <= 20
        String description = team.getDescription();
        if(StringUtils.isNotBlank(description) && description.length() <= 512){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题长度不符合要求");
        }
        // status是否公开 不传默认为0 (公开)
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.getTeamStatusEnum(status);
        if(teamStatusEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍长度不满足要求");
        }
        // 如果 status是加密状态，一定要有密码, 密码<=32
        String password = team.getPassword();
        if(TeamStatusEnum.SECRET.equals(teamStatusEnum)){
            if (StringUtils.isNotBlank(password) || password.length() > 32){
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不正确");
            }
        }
        // 超时时间 > 当前时间
        Date expireTime = team.getExpireTime();
        if(new Date().after(expireTime)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间 > 当前时间");
        }
        // 校验用户最多创建五支队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long hasTeamNum = this.count(queryWrapper);
        if(hasTeamNum >= 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍大于5");
        }
        // 插入用户队伍表
        team.setId(null);
        boolean result = this.save(team);
        Long teamId = team.getId();
        if (!result || teamId == null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }

        // 插入用户  => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(team.getId());
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        return teamId;
    }

    @Override
    public List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        // 组合查询条件
        if (teamQuery != null) {
            Long id = teamQuery.getId();
            if (id != null && id > 0) {
                queryWrapper.eq("id", id);
            }
            List<Long> idList = teamQuery.getIdList();
            if (!CollectionUtils.isEmpty(idList)) {
                queryWrapper.in("id", idList);
            }
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
            }
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name)) {
                queryWrapper.like("name", name);
            }
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)) {
                queryWrapper.like("description", description);
            }
            Integer maxNum = teamQuery.getMaxNum();
            // 查询最大人数相等的
            if (maxNum != null && maxNum > 0) {
                queryWrapper.eq("maxNum", maxNum);
            }
            Long userId = teamQuery.getUserId();
            // 根据创建人来查询
            if (userId != null && userId > 0) {
                queryWrapper.eq("userId", userId);
            }
            // 根据状态来查询
            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getTeamStatusEnum(status);
            if (statusEnum == null) {
                statusEnum = TeamStatusEnum.PUBLIC;
            }
            if (!isAdmin && statusEnum.equals(TeamStatusEnum.PRIVATE)) {
                throw new BusinessException(ErrorCode.NOT_AUTH);
            }
            queryWrapper.eq("status", statusEnum.getValue());
        }
        // 不展示已过期的队伍
        // expireTime is null or expireTime > now()
        queryWrapper.and(qw -> qw.gt("expireTime", new Date()).or().isNull("expireTime"));
        List<Team> teamList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(teamList)) {
            return new ArrayList<>();
        }
        List<TeamUserVO> teamUserVOList = new ArrayList<>();
        // 关联查询创建人的用户信息
        for (Team team : teamList) {
            Long userId = team.getUserId();
            if (userId == null) {
                continue;
            }
            User user = userService.getById(userId);
            TeamUserVO teamUserVO = new TeamUserVO();
            BeanUtils.copyProperties(team, teamUserVO);
            // 脱敏用户信息
            if (user != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                teamUserVO.setCreateUser(userVO);
            }
            teamUserVOList.add(teamUserVO);
        }
        return teamUserVOList;
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = teamUpdateRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team oldTeam = this.getById(id);
        if (oldTeam == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        // 只有管理员或者队伍的创建者可以修改
        if (oldTeam.getUserId() != loginUser.getId() && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        TeamStatusEnum statusEnum = TeamStatusEnum.getTeamStatusEnum(teamUpdateRequest.getStatus());
        if (statusEnum.equals(TeamStatusEnum.SECRET)) {
            if (StringUtils.isBlank(teamUpdateRequest.getPassword())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "加密房间必须要设置密码");
            }
        }
        Team updateTeam = new Team();
        BeanUtils.copyProperties(teamUpdateRequest, updateTeam);
        return this.updateById(updateTeam);
    }

    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
        if(teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = teamJoinRequest.getTeamId();
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在");
        }
        Date expireTime = team.getExpireTime();
        if(expireTime != null && expireTime.before(new Date())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期");
        }
        Integer status = team.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getTeamStatusEnum(status);
        if(TeamStatusEnum.PRIVATE.equals(statusEnum)) {
            throw new BusinessException(ErrorCode.NOT_AUTH, "禁止加入私有队伍");
        }
        String password = teamJoinRequest.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || !password.equals(team.getPassword())) {
                throw new BusinessException(ErrorCode.NOT_AUTH, "密码错误");
            }
        }
        long userId = loginUser.getId();
        // 用户不能加入自己的队伍

        // 已加入的队伍数量

        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long hasJoinNum = userTeamService.count(queryWrapper);
        if(hasJoinNum > 5){
            throw new BusinessException(ErrorCode.NOT_AUTH, "最多加入五个队伍");
        }
        // 不能加入已加入的队伍
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("teamId", teamId);
        long hasUserJoinTeam = userTeamService.count(queryWrapper);
        if(hasUserJoinTeam > 0){
            throw new BusinessException(ErrorCode.NOT_AUTH, "无法加入已加入的队伍");
        }
        // 加入队伍已满
        queryWrapper = new  QueryWrapper<>();
        queryWrapper.eq("teamId", teamId);
        long teamHasJoinNum = userTeamService.count(queryWrapper);
        if(teamHasJoinNum >= team.getMaxNum()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已满");
        }
        // 修改队伍信息
        UserTeam userTeam = new UserTeam();
        userTeam.setTeamId(teamId);
        userTeam.setUserId(userId);
        userTeam.setJoinTime(new Date());
        return userTeamService.save(userTeam);
    }
}




