package com.example.usercentertest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.usercentertest.model.domain.Team;
import com.example.usercentertest.model.domain.User;
import com.example.usercentertest.model.domain.request.TeamJoinRequest;
import com.example.usercentertest.model.domain.request.TeamQuitRequest;
import com.example.usercentertest.model.domain.request.TeamUpdateRequest;
import com.example.usercentertest.model.domain.vo.TeamUserVO;
import com.example.usercentertest.model.dto.TeamQuery;

import java.util.List;

/**
* @author Volder
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2025-12-02 20:07:16
*/
public interface TeamService extends IService<Team> {
    long addTeam(Team team, User loginUser);

    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 解散队伍
     *
     * @param id 队伍id
     * @return 是否成功解散队伍
     */
    boolean deleteTeam(long id,User loginUser);
}
