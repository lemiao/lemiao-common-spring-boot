package com.lemiao.common.healthcheck.autoconfig;

import java.util.concurrent.Executor;

import com.lemiao.common.dingding.autoconfig.DingDingClientAutoConfiguration;
import com.lemiao.common.dingding.client.DingDingClient;
import com.lemiao.common.healthcheck.listener.StarterNoticeListener;
import com.lemiao.common.healthcheck.properties.NoticeProperties;
import com.lemiao.common.thread.autoconfig.ThreadPoolAutoConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointAutoConfiguration;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 应用启动通知自动配置类
 *
 * @author lzs
 * @date 2020年08月29日 12:19 上午
 */
@Configuration
@Import(
    {DingDingClientAutoConfiguration.class, ThreadPoolAutoConfiguration.class, HealthEndpointAutoConfiguration.class})
@EnableConfigurationProperties(NoticeProperties.class)
public class StarterNoticeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(value = {DingDingClient.class, HealthEndpoint.class, Executor.class})
    @ConditionalOnProperty(name = "notice.start-ding-urls[0]")
    public StarterNoticeListener starterNoticeListener(NoticeProperties noticeProperties, DingDingClient dingDingClient,
                                                       HealthEndpoint healthEndpoint,
                                                       @Qualifier(value = "threadPool") Executor executor) {
        return new StarterNoticeListener(noticeProperties, dingDingClient, healthEndpoint, executor);
    }

}