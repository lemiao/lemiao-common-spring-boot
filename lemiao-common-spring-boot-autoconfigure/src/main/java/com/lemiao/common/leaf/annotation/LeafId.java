package com.lemiao.common.leaf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lemiao.common.leaf.enums.LeafIdType;

/**
 * Leaf Id 业务key配置
 *
 * @author lzs
 * @date 2020年07月29日 下午4:12
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LeafId {

    /**
     * Leaf业务Key
     *
     * @return 业务Key
     */
    String key();

    /**
     * Id生成模式，默认号段模式
     *
     * @return Id生成模式
     */
    LeafIdType idType() default LeafIdType.SEGMENT;

}
