package com.example.domaintask

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.util.concurrent.TimeUnit
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.cache.CacheManager

@Configuration
@EnableCaching
class AppConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplateBuilder().build()
    }

    @Bean
    fun caffeineConfig(): Caffeine<Any, Any> {
        return Caffeine.newBuilder().expireAfterWrite(24, TimeUnit.HOURS)
    }

    @Bean
    fun cacheManager(caffeine: Caffeine<Any, Any>): CacheManager? {
        val caffeineCacheManager = CaffeineCacheManager()
        caffeineCacheManager.setCaffeine(caffeine)
        caffeineCacheManager.setCacheNames(listOf("prices"))
        return caffeineCacheManager
    }
}