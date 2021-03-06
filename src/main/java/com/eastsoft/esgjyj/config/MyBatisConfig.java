package com.eastsoft.esgjyj.config;

import java.io.IOException;

import org.apache.ibatis.session.ExecutorType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.eastsoft.esgjyj.context.MultipleDataSource;

/**
 * MyBatis 配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
@MapperScan(basePackages = "com.eastsoft.esgjyj.dao")
public class MyBatisConfig {
	@Bean
	public SqlSessionFactoryBean sqlSessionFactory(MultipleDataSource multipleDataSource) throws IOException {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(multipleDataSource);
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mybatisConfig.xml"));
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
		return sqlSessionFactoryBean;
	}

//	@Bean
//	@Description("支持批量操作的 sqlSession")
//	public SqlSessionTemplate sqlSession(SqlSessionFactoryBean sqlSessionFactory) throws Exception {
//		return new SqlSessionTemplate(sqlSessionFactory.getObject(), ExecutorType.SIMPLE);
//	}
}
