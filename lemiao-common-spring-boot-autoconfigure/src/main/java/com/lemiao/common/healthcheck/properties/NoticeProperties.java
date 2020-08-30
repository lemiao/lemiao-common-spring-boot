package com.lemiao.common.healthcheck.properties;

import java.util.List;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author by catface
 * @date 2020/08/01
 */
@Data
@ConfigurationProperties(prefix = "notice")
public class NoticeProperties {

    /**
     * 应用名称
     */
    @Value("${spring.application.name:unknown}")
    private String app;

    /**
     * 环境信息
     */
    @Value("${spring.profiles.active:local}")
    private String env;

    /**
     * 服务启动后的钉钉通知地址
     */
    private List<String> startDingUrls;

    /**
     * 健康检查超时时间
     */
    private Long healthCheckTimeOutMillis = 6000L;

}
