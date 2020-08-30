package com.lemiao.common.leaf.autoconfig;

import com.lemiao.common.leaf.api.LeafApi;
import com.lemiao.common.leaf.api.RestLeafApi;
import com.lemiao.common.leaf.properties.LeafProperties;
import com.lemiao.common.rest.autoconfig.RestTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

/**
 * Leaf自动配置类
 *
 * @author lzs
 * @date 2020年07月29日 下午4:42
 */
@Configuration
@Import(RestTemplateAutoConfiguration.class)
@EnableConfigurationProperties(LeafProperties.class)
public class LeafAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RestTemplate.class)
    @ConditionalOnProperty(name = "leaf.server.url")
    public LeafApi leafApi(RestTemplate restTemplate, LeafProperties leafProperties) {
        return new RestLeafApi(restTemplate, leafProperties);
    }

}
