package com.sige.web.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.sige.core.dao.SQLUtil;
import com.sige.core.dao.SQLUtilImpl;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan({"com.sige","com.sige.dao" })
//@EnableWebMvc
//@EnableGlobalMethodSecurity(prePostEnabled=true)
//@EnableWebSecurity
@EnableTransactionManagement
//@PropertySource(value = { "classpath:application.properties" })
//public class WebConfig extends WebSecurityConfigurerAdapter {
public class WebConfig extends WebMvcConfigurerAdapter {
	
	
	/*
	 * private final JwtAuthenticationProvider authenticationProvider; private final
	 * JwtAuthenticationEntryPoint entryPoint;
	 * 
	 * public WebConfig(JwtAuthenticationProvider authenticationProvider,
	 * JwtAuthenticationEntryPoint entryPoint) { this.authenticationProvider =
	 * authenticationProvider; this.entryPoint = entryPoint; }
	 */

	/*
	@Bean
	public SQLUtil getSQLUtil() {
		return new SQLUtilImpl(dataSource());
	}
	*/
}