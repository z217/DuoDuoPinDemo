package com.duoduopin.dao;

import com.duoduopin.bean.SystemMessage;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * @description 系统消息映射类
 * @author z217
 * @date 2021/01/26
 */
public interface SystemMessageMapper {
  SystemMessage getSystemMessageByMessageId(long messageId);
  
  Integer isApplySystemMessageExistsByUserIdAndBillId(@Param("userId") long userId, @Param("billId") long billId);
  
  List<SystemMessage> getSystemMessageByUserIdAndLastOnline(@Param("userId") long userId, @Param("lastOnline") Timestamp lastOnline);
  
  int insertSystemMessage(SystemMessage systemMessage);
  
  int deleteSystemMessageByMessageId(long messageId);
}
