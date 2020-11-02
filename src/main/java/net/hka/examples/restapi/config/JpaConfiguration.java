package net.hka.examples.restapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * To enable the automating JPA auditing feature 
 */
@Configuration
@EnableJpaAuditing // to handle the insertion and updating of "create" "last modified" date automatically
public class JpaConfiguration {
}
