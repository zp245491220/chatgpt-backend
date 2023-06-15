package com.hncboy.chatgpt.base.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.hncboy.chatgpt.base.config.prop.AliyunEmailProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunEmailConfig {

    @Autowired
    private AliyunEmailProp prop;

    @Bean
    public IAcsClient emailClient() {
        // 如果是除杭州region外的其它region（如新加坡、澳洲Region），需要将下面的"cn-hangzhou"替换为"ap-southeast-1"、或"ap-southeast-2"。
        IClientProfile profile = DefaultProfile.getProfile(
                prop.getRegion(),
                prop.getAccessKey(),
                prop.getSecret()
        );
        return new DefaultAcsClient(profile);
    }

}
