package com.tsarev

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit


/**
 * Configuration bean for creating caches.
 */
@Configuration
open class CachesFactory {

    @Bean("currenciesCache")
    open fun currenciesCache() = CaffeineCache(
        "currenciesCache",
        Caffeine.newBuilder()
            .expireAfterWrite(4, TimeUnit.HOURS)
            .maximumSize(1)
            .build()
    )

    @Bean("monthCurrenciesRateCache")
    open fun monthCurrenciesRateCache() = CaffeineCache(
        "monthCurrenciesRateCache",
        Caffeine.newBuilder()
            .expireAfterWrite(4, TimeUnit.HOURS)
            .maximumSize(10 * 12 * 100) // 10 years caching 100 currencies is well enough.
            .build()
    )

}