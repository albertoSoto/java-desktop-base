package com.deicos.desktop.config;

import com.deicos.desktop.AppParams;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {AppParams.BASE_PACKAGE})
public class MvcConfig extends WebMvcAutoConfiguration{


}
