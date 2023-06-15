package com.hncboy.chatgpt.front.service.impl;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.hncboy.chatgpt.base.config.prop.AliyunEmailProp;
import com.hncboy.chatgpt.base.enums.EmailBizTypeEnum;
import com.hncboy.chatgpt.front.service.EmailService;
import com.hncboy.chatgpt.front.service.SysEmailSendLogService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;

/**
 * @version : 1.0
 * @author: zhangpeng22
 * @date :  2023年06月15 09:08:07
 * @description: :
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AliEmailServiceImpl implements EmailService {

    @Autowired
    private AliyunEmailProp prop;
    @Autowired
    private IAcsClient client;
    @Autowired
    private SysEmailSendLogService emailLogService;

    private SpringTemplateEngine templateEngine;
    private TemplateSpec templateSpec;

    @PostConstruct
    public void init() {
        this.templateEngine = new SpringTemplateEngine();
        // 获取邮件模板
        this.templateEngine.setTemplateResolver(new ClassLoaderTemplateResolver());
        this.templateSpec = new TemplateSpec("templates/register_verify_email.html", StandardCharsets.UTF_8.name());
    }

    @Override
    public void sendForVerifyCode(String targetEmail, String verifyCode) {
        // 设置模板中需要填充的变量
        Context context = new Context();
        context.setVariable("verificationUrl", prop.getVerificationRedirectUrl().concat(verifyCode));
        // 进行渲染
        String renderedTemplate = templateEngine.process(templateSpec, context);

        // 记录日志
        try {
            String sendMsgId = sendEmail("注册", targetEmail, renderedTemplate);
            emailLogService.createSuccessLogBySysLog(sendMsgId, prop.getAccountName(), targetEmail, EmailBizTypeEnum.REGISTER_VERIFY, renderedTemplate);
        } catch (Exception e) {
            // FIXME 发送失败前端仍然显示成功
            emailLogService.createFailedLogBySysLog("", prop.getAccountName(), targetEmail, EmailBizTypeEnum.REGISTER_VERIFY, renderedTemplate, e.getMessage());
        }
    }

    private String sendEmail(String subject, String email, String content) {
        if (BooleanUtils.isNotTrue(prop.getEnable())) {
            log.info("Email is disabled.{}-----{}", subject, email);
            return "force disable";
        }

        SingleSendMailRequest request = new SingleSendMailRequest();
        try {
            //request.setVersion("2017-06-22");// 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
            //邮件服务 发信地址
            request.setAccountName(prop.getAccountName());
            request.setFromAlias(prop.getAlias());//发信人昵称，长度小于15个字符。
            request.setAddressType(1);//0：为随机账号 1：为发信地址
            request.setTagName(prop.getTagName());
            request.setReplyToAddress(true);// 是否启用管理控制台中配置好回信地址（状态须验证通过），取值范围是字符串true或者false
            request.setToAddress(email);
            //可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
            //request.setToAddress("邮箱1,邮箱2");
            request.setSubject(subject);
            //如果采用byte[].toString的方式的话请确保最终转换成utf-8的格式再放入htmlbody和textbody，若编码不一致则会被当成垃圾邮件。
            //注意：文本邮件的大小限制为3M，过大的文本会导致连接超时或413错误
            request.setHtmlBody(content);
            //SDK 采用的是http协议的发信方式, 默认是GET方法，有一定的长度限制。
            //若textBody、htmlBody或content的大小不确定，建议采用POST方式提交，避免出现uri is not valid异常
            request.setMethod(MethodType.POST);
            //开启需要备案，0关闭，1开启
            //request.setClickTrace("0");
            //如果调用成功，正常返回httpResponse；如果调用失败则抛出异常，需要在异常中捕获错误异常码；错误异常码请参考对应的API文档;
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);
            log.info(httpResponse.toString());

            return httpResponse.getRequestId();
        } catch (ServerException e) {
            //捕获错误异常码
            System.out.println("ErrCode : " + e.getErrCode());
            e.printStackTrace();
        } catch (ClientException e) {
            //捕获错误异常码
            System.out.println("ErrCode : " + e.getErrCode());
            e.printStackTrace();
        }

        return "error";
    }

}
