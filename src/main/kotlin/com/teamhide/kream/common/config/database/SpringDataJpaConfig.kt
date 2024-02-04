package com.teamhide.kream.common.config.database

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["com.teamhide.kream.*.adapter.out.persistence.jpa"])
class SpringDataJpaConfig
