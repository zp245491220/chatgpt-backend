package com.hncboy.chatgpt.base.keycenter.service;

import java.util.List;

/**
 * @author PQ.MA
 * @date 2023-6-17
 * ChatKeyCenter表相关接口
 */
public interface ChatKeyCenterService {


    /**
     * 列出可用聊天密钥
     *
     * @return 可用聊天密钥集合
     */
    List<String> listIsUsableChatKey();


    /**
     * 更新chatKey为可用状态
     *
     * @param chatKey
     */
    Boolean updateIsUsableChatKey(String chatKey);


    /**
     * 更新chatKey为不可用状态
     *
     * @param chatKey
     */
    Boolean updateNotUsableChatKey(String chatKey);

}
