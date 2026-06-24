package com.uit.se104.feedback_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// automatically update created_at and updated_at
@Configuration
@EnableJpaAuditing
public class JpaConfig {

}
