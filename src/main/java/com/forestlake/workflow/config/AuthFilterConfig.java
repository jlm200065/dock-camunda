package com.forestlake.workflow.config;

import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Configuration
public class AuthFilterConfig implements ServletContextInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        FilterRegistration.Dynamic authFilter = servletContext.addFilter("camunda-auth", ProcessEngineAuthenticationFilter.class);
        authFilter.setAsyncSupported(true);
        authFilter.setInitParameter("authentication-provider","org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider");
        authFilter.addMappingForUrlPatterns(null,true,"/engine-rest/*");
    }
}
