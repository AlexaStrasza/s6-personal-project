package com.alexstrasza.auctioning.configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends GlobalMethodSecurityConfiguration {
}
