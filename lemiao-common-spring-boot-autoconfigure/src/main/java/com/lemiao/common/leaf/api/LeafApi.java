package com.lemiao.common.leaf.api;

/**
 * Leaf API
 *
 * @author lzs
 * @date 2020年07月29日 下午4:44
 */
public interface LeafApi {

    /**
     * 获取Leaf号段ID
     *
     * @param bizKey 业务Key
     * @return Leaf ID
     */
    Long getSegmentId(String bizKey);

    /**
     * 获取Leaf雪花算法ID
     *
     * @param bizKey 业务Key
     * @return Leaf ID
     */
    Long getSnowflakeId(String bizKey);

}
