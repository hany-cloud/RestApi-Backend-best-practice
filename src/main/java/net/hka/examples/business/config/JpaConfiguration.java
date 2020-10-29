package net.hka.examples.business.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 *
 */
@Configuration
@EnableJpaAuditing // to update "last modified date" automatically
public class JpaConfiguration {
}
