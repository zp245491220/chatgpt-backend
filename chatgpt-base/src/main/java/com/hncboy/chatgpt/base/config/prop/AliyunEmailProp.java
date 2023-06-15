package com.hncboy.chatgpt.base.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zp
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.email")
public class AliyunEmailProp {

    private Boolean enable;

    private String region;

    private String accessKey;

    private String secret;

    private String accountName;

    private String alias;

    private String tagName;

    private String verificationRedirectUrl;


}
