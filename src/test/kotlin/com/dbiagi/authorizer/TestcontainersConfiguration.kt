package com.dbiagi.authorizer

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.ComposeContainer
import java.io.File

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {
    val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun postgresContainer() {
        ComposeContainer(File("")).apply {
            withExposedService("postgres-1", 5432)
            start()
            logger.info("Docker compose container started")
        }
    }
}
