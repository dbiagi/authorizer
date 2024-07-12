package com.dbiagi.authorizer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

@ActiveProfiles("test")
@SpringBootTest(classes = [AuthorizerApplication::class], webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
abstract class AuthorizerApplicationTests {
    @Autowired
    lateinit var mockMvc: MockMvc
}
