package com.example.usercentertest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercentertest.model.domain.Team;
import com.example.usercentertest.mapper.TeamMapper;
import com.example.usercentertest.service.TeamService;
import org.springframework.stereotype.Service;

/**
* @author Volder
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2025-12-02 20:07:16
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

}




