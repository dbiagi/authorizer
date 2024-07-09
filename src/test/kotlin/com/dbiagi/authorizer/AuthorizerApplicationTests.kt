package com.dbiagi.authorizer

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(classes = [AuthorizerApplication::class], webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
abstract class AuthorizerApplicationTests
