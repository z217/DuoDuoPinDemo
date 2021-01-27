package com.duoduopin.controller;

import com.alibaba.fastjson.JSONObject;
import com.duoduopin.annotation.Authorization;
import com.duoduopin.bean.ChatMessage;
import com.duoduopin.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author z217
 * @description 聊天WebSocket
 * @date 2021/01/17
 */
@Controller
@Slf4j
@ServerEndpoint(value = "/ws/chat/{id}")
public class ChatWebSocket {
  private static ChatService chatService;
  private static ConcurrentHashMap<Long, List<ChatWebSocket>> webSocketMap = null;
  
  static {
    webSocketMap = new ConcurrentHashMap<>();
  }
  
  private Session session;
  private long billId;
  
  @Autowired
  public void setChatService(ChatService chatService) {
    ChatWebSocket.chatService = chatService;
  }
  
  @OnOpen
  @Authorization
  public void onOpen(@PathParam(value = "id") long billId, Session session) {
    this.billId = billId;
    this.session = session;
    if (webSocketMap.containsKey(billId)) {
      webSocketMap.get(billId).add(this);
    } else {
      List<ChatWebSocket> list = new Vector<>();
      list.add(this);
      webSocketMap.put(billId, list);
    }
    log.info("Team " + billId + " has been created, exec in ChatController.onOpen().");
  }

  @OnClose
  @Authorization
  public void onClose() {
    List<ChatWebSocket> list = webSocketMap.get(billId);
    if (list.size() == 1) webSocketMap.remove(billId);
    else list.remove(this);
    log.info("Team " + billId + " has been remove, exec in ChatController.onClose().");
  }

  @OnMessage
  @Authorization
  public void onMessage(String jsonChatMessage, Session session) {
    ChatMessage chatMessage = JSONObject.parseObject(jsonChatMessage, ChatMessage.class);
    log.info(chatMessage.toString());
    sendToTeam(chatMessage);
  }

  @OnError
  public void onError(Session session, Throwable error) {
    log.warn(Arrays.toString(error.getStackTrace()));
  }

  public boolean isOpen() {
    return session.isOpen();
  }

  public void sendMessage(String message) throws IOException {
    session.getBasicRemote().sendText(message);
  }

  private void sendToTeam(ChatMessage chatMessage) {
    List<ChatWebSocket> list = webSocketMap.get(chatMessage.getBillId());
    chatService.createChatMessage(chatMessage);
    String jsonChatMessage = JSONObject.toJSONString(chatMessage);
    try {
      for (ChatWebSocket controller : list) {
        if (controller.isOpen()) controller.sendMessage(jsonChatMessage);
      }
    } catch (IOException e) {
      log.warn(Arrays.toString(e.getStackTrace()));
    }
  }
}
