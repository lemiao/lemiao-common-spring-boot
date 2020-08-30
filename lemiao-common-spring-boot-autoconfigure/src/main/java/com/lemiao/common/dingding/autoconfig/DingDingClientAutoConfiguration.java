package com.lemiao.common.dingding.autoconfig;

import com.lemiao.common.dingding.client.DingDingClient;
import com.lemiao.common.dingding.client.DingDingClientImpl;
import com.lemiao.common.rest.autoconfig.RestTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

/**
 * DingDingClient自动配置类
 *
 * @author lzs
 * @date 2020年07月29日 下午9:30
 */
@Configuration
@Import(RestTemplateAutoConfiguration.class)
public class DingDingClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RestTemplate.class)
    public DingDingClient dingDingClient(RestTemplate restTemplate) {
        return new DingDingClientImpl(restTemplate);
    }

}
