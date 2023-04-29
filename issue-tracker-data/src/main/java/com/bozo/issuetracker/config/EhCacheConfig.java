package com.bozo.issuetracker.config;

import com.bozo.issuetracker.model.User;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.util.List;

@Configuration
@EnableCaching
public class EhCacheConfig {

    @Bean(name = "cacheManager1")
    public CacheManager cacheManager(){
        List<String> cacheNames = List.of(
                "UserCacheByName");

        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();

        CacheConfigurationBuilder<Long, Object> defaultCacheConfiguration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                        Long.class,
                        Object.class,
                        ResourcePoolsBuilder
                                .heap(100))
                .withExpiry(
                        ExpiryPolicyBuilder
                                .timeToLiveExpiration(
                                        Duration.ofHours(4)));

        CacheConfigurationBuilder<String, User> userByNameCacheConfiguration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                        String.class,
                        User.class,
                        ResourcePoolsBuilder
                                .heap(10))
                .withExpiry(
                        ExpiryPolicyBuilder
                                .timeToLiveExpiration(
                                        Duration.ofHours(1)));


        cacheNames.forEach(cn -> {
            if (cn.equalsIgnoreCase("UserCacheByName")){
                cacheManager.createCache(cn, Eh107Configuration.fromEhcacheCacheConfiguration(userByNameCacheConfiguration));
            }else{
                cacheManager.createCache(cn, Eh107Configuration.fromEhcacheCacheConfiguration(defaultCacheConfiguration));
            }
        });

        return cacheManager;
    }

}
