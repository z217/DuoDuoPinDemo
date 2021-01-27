package com.duoduopin.controller;

import com.alibaba.fastjson.JSONObject;
import com.duoduopin.annotation.Authorization;
import com.duoduopin.bean.SystemMessage;
import com.duoduopin.service.SystemMessageService;
import com.duoduopin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @description 系统消息WebSocket，负责向当前在线的用户发送实时系统消息
 * @author z217
 * @date 2021/01/25
 * @see com.duoduopin.service.SystemMessageService
 * @see com.duoduopin.service.UserService
 */
@Controller
@Slf4j
@ServerEndpoint("/ws/system/{id}")
public class SystemMessageWebSocket {
  private static UserService userService;
  private static AtomicLong currentConnection;
  private static ConcurrentHashMap<Long, SystemMessageWebSocket> webSocketMap;

  static {
    webSocketMap = new ConcurrentHashMap<>();
    currentConnection = new AtomicLong(0);
  }

  private Session session;
  private long userId;

  //  广播消息
  public static void broadSystemMessage(SystemMessage systemMessage) {
    String jsonSystemMessage = JSONObject.toJSONString(systemMessage);
    try {
      for (SystemMessageWebSocket webSocket : webSocketMap.values()) {
        if (webSocket.isOpen()) webSocket.sendMessage(jsonSystemMessage);
      }
    } catch (IOException e) {
      log.warn(Arrays.toString(e.getStackTrace()));
    }
  }

  //  向用户发送消息
  public static boolean sendToUser(SystemMessage systemMessage) {
    String jsonSystemMessage = JSONObject.toJSONString(systemMessage);
    SystemMessageWebSocket webSocket = webSocketMap.get(systemMessage.getReceiverId());
    if (webSocket == null || !webSocket.isOpen()) return false;
    try {
      webSocket.sendMessage(jsonSystemMessage);
    } catch (IOException e) {
      log.warn(Arrays.toString(e.getStackTrace()));
    }
    return true;
  }

  public static long getConcurrentConnection() {
    return currentConnection.get();
  }

  @Autowired
  public void setSystemMessageService(SystemMessageService systemMessageService) {}

  @Autowired
  public void setUserService(UserService userService) {
    SystemMessageWebSocket.userService = userService;
  }

  @OnOpen
  @Authorization
  public void onOpen(@PathParam("id") long userId, Session session) {
    this.userId = userId;
    this.session = session;
    webSocketMap.put(userId, this);
    currentConnection.incrementAndGet();
    log.info(userId + " is online, exec in SystemMessageWebSocket.onOpen().");
  }

  @OnClose
  @Authorization
  public void onClose() {
    webSocketMap.remove(userId);
    currentConnection.decrementAndGet();
    userService.updateLastOnlineByUserId(userId);
    log.info(userId + " is offline, exec in SystemMessageWebSocket.onClose().");
  }

  // 单向，客户端不需要向服务器发送消息
  @OnMessage
  @Authorization
  public void onMessage(String jsonSystemMessage, Session session) {}

  public boolean isOpen() {
    return session.isOpen();
  }

  public void sendMessage(String message) throws IOException {
    session.getAsyncRemote().sendText(message);
  }
}
