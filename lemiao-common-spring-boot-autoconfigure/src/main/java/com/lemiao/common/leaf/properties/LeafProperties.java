package com.lemiao.common.leaf.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Leaf属性
 *
 * @author lzs
 * @date 2020年07月29日 下午4:42
 */
@Data
@ConfigurationProperties(prefix = "leaf.server")
public class LeafProperties {

    /**
     * Leaf服务地址
     */
    private String url;

}
