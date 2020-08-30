package com.lemiao.common.leaf.enums;

import lombok.Getter;

/**
 * Leaf ID 类型
 *
 * @author lzs
 * @date 2020年08月11日 上午10:16
 */
@Getter
public enum LeafIdType {

    /**
     * 号段模式
     */
    SEGMENT("号段模式"),

    /**
     * 雪花算法模式
     */
    SNOWFLAKE("雪花算法模式");

    private String desc;

    LeafIdType(String desc) {
        this.desc = desc;
    }

}
