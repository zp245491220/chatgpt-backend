package com.hncboy.chatgpt.base.keycenter;

import cn.hutool.extra.spring.SpringUtil;
import com.hncboy.chatgpt.base.keycenter.service.ChatKeyCenterService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author PQ.MA
 * @date 2023-6-17
 * ChatKeyCenter可用key处理
 */
@Log4j2
@Configuration
@AllArgsConstructor
public class ChatKeyCenterHandler implements ApplicationRunner {

    /**
     * 全局可以使用的chatKey集合
     */
    public static final List<String> GLOBAL_USE_CHAT_KEY_LIST = new LinkedList<>();

    /**
     * 初始化全局key集合
     */
    @Override
    public void run(ApplicationArguments args) {
        ChatKeyCenterService chatKeyCenterService = SpringUtil.getBean(ChatKeyCenterService.class);
        List<String> chatKeys = chatKeyCenterService.listIsUsableChatKey();
        log.info("初始化全局chatKey,可用key数量：{}", chatKeys.size());
        GLOBAL_USE_CHAT_KEY_LIST.addAll(chatKeys);
    }


    /**
     * 检查key是否可用
     *
     * @param chatKey
     */
    public static Boolean checkChatKeyIsUsable(String chatKey) {
        //需不需要用key去请求openai 但是会消耗一次
        return GLOBAL_USE_CHAT_KEY_LIST.contains(chatKey);
    }

    /**
     * 推送可用的chatKey
     */
    public static void pushListUsableChatKey(String chatKey) {
        GLOBAL_USE_CHAT_KEY_LIST.add(chatKey);
        log.warn("推送可用chatKey: {}", chatKey);
    }


    /**
     * 删除可用的chatKey
     */
    public static void deleteListUsableChatKey(String chatKey) {
        //删除全局变量
        GLOBAL_USE_CHAT_KEY_LIST.remove(chatKey);
        //更改数据库状态防止下次部署时构建
        ChatKeyCenterService chatKeyCenterService = SpringUtil.getBean(ChatKeyCenterService.class);
        Boolean isOk = chatKeyCenterService.updateNotUsableChatKey(chatKey);
        log.warn("删除无用chatKey: {},{}", chatKey, isOk);
    }


    /**
     * 随机获取可用的chatKey
     */
    public static String getRandomIsUsableChatKey() {
        //先使用简单的Random获取随机key 考虑多线程情况下使用ThreadLocalRandom
        int keyIndex = new Random().nextInt(GLOBAL_USE_CHAT_KEY_LIST.size());
        String chatKey = GLOBAL_USE_CHAT_KEY_LIST.get(keyIndex);
        log.warn("随机获取chatKey: {}", chatKey);
        return chatKey;
    }

}
