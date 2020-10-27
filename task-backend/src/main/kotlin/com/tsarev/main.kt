package com.tsarev

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.springframework.scheduling.annotation.EnableScheduling

fun main(vararg args: String) {
    SpringApplication.run(SpringBootApp::class.java, *args)
}

/**
 * Main entry point of application with
 * feature enabling/disabling annotations.
 */
@EnableCaching
@EnableScheduling
@SpringBootApplication
@ImportAutoConfiguration
@EnableConfigurationProperties
open class SpringBootApp

@Configuration
@Profile("dev")
@PropertySource("classpath:application-dev.yml")
open class DevConfig