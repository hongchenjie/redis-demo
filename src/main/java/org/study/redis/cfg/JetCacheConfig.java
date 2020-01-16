package org.study.redis.cfg;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * @author lipo
 * @version v1.0
 * @date 2020-01-16 15:34
 */
@Configuration
@EnableMethodCache(basePackages = "org.study")
@EnableCreateCacheAnnotation
public class JetCacheConfig {
}
