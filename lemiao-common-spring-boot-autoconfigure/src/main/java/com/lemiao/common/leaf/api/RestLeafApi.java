package com.lemiao.common.leaf.api;

import java.text.MessageFormat;

import com.lemiao.common.leaf.properties.LeafProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Leaf API
 *
 * @author lzs
 * @date 2020年07月29日 下午4:44
 */
@Slf4j
public class RestLeafApi implements LeafApi {

    private RestTemplate restTemplate;

    private LeafProperties leafProperties;

    public RestLeafApi(RestTemplate restTemplate, LeafProperties leafProperties) {
        this.restTemplate = restTemplate;
        this.leafProperties = leafProperties;
    }

    /**
     * 获取Leaf号段ID
     *
     * @param bizKey 业务Key
     * @return Leaf ID
     */
    @Override
    public Long getSegmentId(String bizKey) {
        String url = MessageFormat.format("{0}/api/segment/get/{1}", leafProperties.getUrl(), bizKey);
        try {
            return restTemplate.getForObject(url, Long.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取Leaf雪花算法ID
     *
     * @param bizKey 业务Key
     * @return Leaf ID
     */
    @Override
    public Long getSnowflakeId(String bizKey) {
        String url = MessageFormat.format("{0}/api/snowflake/get/{1}", leafProperties.getUrl(), bizKey);
        try {
            return restTemplate.getForObject(url, Long.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
