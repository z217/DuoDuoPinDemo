package com.duoduopin.dao;

import com.duoduopin.bean.TeamMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description 小组成员映射类
 * @author z217
 * @date 2021/01/17
 */
public interface TeamMemberMapper {
  List<TeamMember> getTeamMemberByBillId(long billId);

  List<TeamMember> getTeamByUserId(long userId);

  int joinTeam(@Param("billId") long billId, @Param("userId") long userId);
  
  int quitTeam(@Param("billId") long billId, @Param("userId") long userId);
}
