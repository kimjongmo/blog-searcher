package com.example.blogsearcher.infra.persistence.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["com.example.blogsearcher.infra.persistence.repository"])
@EntityScan(basePackages = ["com.example.blogsearcher.infra.persistence.entity"])
class JpaConfig
