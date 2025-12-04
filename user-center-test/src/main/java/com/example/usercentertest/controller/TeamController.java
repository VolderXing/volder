package com.example.usercentertest.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usercentertest.common.BaseResponse;
import com.example.usercentertest.common.ErrorCode;
import com.example.usercentertest.exception.BusinessException;
import com.example.usercentertest.model.domain.Team;
import com.example.usercentertest.model.dto.TeamQuery;
import com.example.usercentertest.service.TeamService;
import com.example.usercentertest.service.UserTeamService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import com.example.usercentertest.common.ResultUtils;

import java.util.List;

@RestController
@RequestMapping("/team")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class TeamController {
    @Resource
    private UserTeamService userTeamService;
    @Resource
    private TeamService teamService;

    @PostMapping("/add")
    public BaseResponse<Team> addTeam(@RequestBody Team team) {
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.save(team);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"插入失败");
        }
        return ResultUtils.success(team);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeamById(@RequestBody long id) {
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除失败");
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeamById(@RequestBody long id, @RequestBody Team team) {
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.updateById(team);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("get")
    public BaseResponse<Team> getTeamById(@RequestParam long id) {
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"获取失败");
        }
        return ResultUtils.success(team);
    }

    @GetMapping("list")
    public BaseResponse<List<Team>> listTeams(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(team, teamQuery);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        List<Team> teamList = teamService.list(queryWrapper);
        return ResultUtils.success(teamList);
    }
}
