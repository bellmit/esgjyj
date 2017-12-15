package com.eastsoft.esgjyj.config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.eastsoft.esgjyj.context.MultipleDataSource;

/**
 * 多数据源配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
@PropertySource(value = { "classpath:application-dev.properties",
		                  "classpath:application-test.properties",
		                  "classpath:application-prod.properties" },
				ignoreResourceNotFound = true)
@EnableTransactionManagement
@EnableCaching
public class DataSourcesConfig {
	@Bean
	@ConfigurationProperties(prefix = "escloud.datasource")
	public DruidDataSource escloudDataSource() throws SQLException {
		DruidDataSource escloudDataSource = (DruidDataSource) DataSourceBuilder.create().type(DruidDataSource.class).build();
		this.setDefaultProperties(escloudDataSource);
		return escloudDataSource;
	}
	
	@Bean
	public MultipleDataSource multipleDataSource(@Qualifier("escloudDataSource") DruidDataSource escloudDataSource) throws SQLException {
		Map<Object, Object> map = new HashMap<>();
		map.put("escloudDataSource", escloudDataSource);
		
		MultipleDataSource multipleDataSource = new MultipleDataSource();
		multipleDataSource.setDefaultTargetDataSource(escloudDataSource);
		multipleDataSource.setTargetDataSources(map);
		return multipleDataSource;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate(MultipleDataSource multipleDataSource) {
		return new JdbcTemplate(multipleDataSource);
	}
	
	@Bean
	@Description("事务管理器")
	public PlatformTransactionManager transactionManager(MultipleDataSource multipleDataSource) {
		return new DataSourceTransactionManager(multipleDataSource);
	}
	
	/**
	 * 设置数据源的默认属性。
	 * @param dataSource 数据源。
	 * @throws SQLException
	 */
	private void setDefaultProperties(DruidDataSource dataSource) throws SQLException {
		dataSource.setMaxWait(120000);
		dataSource.setFilters("config,stat");
		dataSource.setConnectionProperties("config.decrypt=true");
		dataSource.setKeepAlive(true);
	}
}
