package com.dbiagi.authorizer

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<AuthorizerApplication>().with(TestcontainersConfiguration::class).run(*args)
}
