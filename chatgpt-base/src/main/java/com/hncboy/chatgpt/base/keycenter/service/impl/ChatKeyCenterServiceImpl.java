package com.hncboy.chatgpt.base.keycenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hncboy.chatgpt.base.domain.entity.ChatKeyCenterDO;
import com.hncboy.chatgpt.base.keycenter.service.ChatKeyCenterService;
import com.hncboy.chatgpt.base.mapper.ChatKeyCenterMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author PQ.MA
 * @date 2023-6-17
 * ChatKeyCenter表业务实现
 */
@Service
public class ChatKeyCenterServiceImpl extends ServiceImpl<ChatKeyCenterMapper, ChatKeyCenterDO> implements ChatKeyCenterService {


    /**
     * 列出可用聊天密钥
     *
     * @return 可用聊天密钥集合
     */
    @Override
    public List<String> listIsUsableChatKey() {
        List<ChatKeyCenterDO> list = list(new LambdaQueryWrapper<ChatKeyCenterDO>().eq(ChatKeyCenterDO::getIsUsable, Boolean.TRUE));
        return list.stream().map(ChatKeyCenterDO::getChatKey).collect(Collectors.toList());
    }

    @Override
    public Boolean updateIsUsableChatKey(String chatKey) {
        return updateUsableChatKey(chatKey, Boolean.TRUE);
    }

    @Override
    public Boolean updateNotUsableChatKey(String chatKey) {
        return updateUsableChatKey(chatKey, Boolean.FALSE);
    }

    /**
     * 更改chatKey可用状态
     *
     * @param chatKey
     * @param isUsable
     * @return
     */
    private Boolean updateUsableChatKey(String chatKey, Boolean isUsable) {
        return update(new LambdaUpdateWrapper<ChatKeyCenterDO>().set(ChatKeyCenterDO::getIsUsable, isUsable).eq(ChatKeyCenterDO::getChatKey, chatKey));
    }

}
