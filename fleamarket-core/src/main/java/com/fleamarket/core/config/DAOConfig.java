package com.fleamarket.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by Gss in 2018/3/17.
 */
@Configuration
public class DAOConfig {
	@Bean
	@ConfigurationProperties("spring.datasource")
	public DataSource druidDataSource(){
		return new DruidDataSource();
	}
	@Bean
	public DataSourceTransactionManager getDataSourceTransactionManager(){
		return new DataSourceTransactionManager(druidDataSource());
	}
}
