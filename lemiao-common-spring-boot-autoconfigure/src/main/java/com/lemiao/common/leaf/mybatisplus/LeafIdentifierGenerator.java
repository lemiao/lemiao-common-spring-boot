package com.lemiao.common.leaf.mybatisplus;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.lemiao.common.leaf.annotation.LeafId;
import com.lemiao.common.leaf.api.LeafApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Leaf ID生成器
 *
 * @author lzs
 * @date 2020年07月29日 下午3:18
 */
@Slf4j
public class LeafIdentifierGenerator implements IdentifierGenerator {

    private IdentifierGenerator defaultIdGenerator;

    private LeafApi leafApi;

    public LeafIdentifierGenerator(LeafApi leafApi) {
        this.defaultIdGenerator = new DefaultIdentifierGenerator();
        this.leafApi = leafApi;
    }

    public LeafIdentifierGenerator(IdentifierGenerator defaultIdGenerator, LeafApi leafApi) {
        this.defaultIdGenerator = defaultIdGenerator;
        this.leafApi = leafApi;
    }

    @Override
    public Number nextId(Object entity) {
        // 获取Leaf ID
        LeafId leafId = entity.getClass().getAnnotation(LeafId.class);
        if (leafId != null && !StringUtils.isEmpty(leafId.key())) {
            Long id;
            switch (leafId.idType()) {
                case SNOWFLAKE:
                    id = leafApi.getSnowflakeId(leafId.key());
                    break;
                case SEGMENT:
                default:
                    id = leafApi.getSegmentId(leafId.key());
                    break;
            }

            if (id != null) {
                return id;
            }
        }

        // 获取默认使用 mybatis-plus Snowflake ID
        return defaultIdGenerator.nextId(entity);
    }

}
