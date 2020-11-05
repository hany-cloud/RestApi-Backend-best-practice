package net.hka.examples.restapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * To enable the automating JPA auditing feature
 */
@Configuration
@EnableJpaAuditing // automatically handle the insertion and updating of "create" and "last modified" date
public class JpaConfiguration { 

}
