package com.hncboy.chatgpt.base.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 用来维护多个体chatKey
 *
 * @author PQ.MA
 * @date 2023-6-17
 * ChatKeyCenter表实体类
 */

@Data
@TableName("chat_key_center")
public class ChatKeyCenterDO {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * chat密钥
     */
    private String chatKey;

    /**
     * 是否可用，false 否，true 是
     */
    private Boolean isUsable;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
