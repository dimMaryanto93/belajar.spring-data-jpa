package com.hotmail.dimmaryanto.software;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 *
 */
@Configuration
@EnableJpaRepositories(basePackages = { "com.hotmail.dimmaryanto.software" })
@ComponentScan(basePackages = { "com.hotmail.dimmaryanto.software" })
@EnableTransactionManagement
public class App {

	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(App.class);
	}

	@Bean
	public DataSource dataSource() {
		BasicDataSource dbcp = new BasicDataSource();

		dbcp.setDriverClassName("org.postgresql.Driver");
		dbcp.setUrl("jdbc:postgresql://localhost:5432/belajar_spring_data_jpa");
		dbcp.setUsername("belajar_spring_data_jpa");
		dbcp.setPassword("admin");

		return dbcp;
	}

	@Bean
	@Autowired
	public EntityManagerFactory EntityManager(DataSource ds) {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.hotmail.dimmaryanto.software");
		factory.setDataSource(ds);
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	@Autowired
	public LocalSessionFactoryBean sessionFactory(DataSource ds) {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(ds);
		sessionFactory.setHibernateProperties(hibernateProperties());
		sessionFactory.setPackagesToScan("com.hotmail.dimmaryanto.software");
		return sessionFactory;
	}

	private Properties hibernateProperties() {
		Properties prop = new Properties() {
			{
				put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
				put("hibernate.hbm2ddl.auto", "update");
			}
		};
		return prop;
	}

	@Bean
	@Autowired
	public JpaTransactionManager transactionManager(SessionFactory session) {
		JpaTransactionManager jpaTM = new JpaTransactionManager(session);
		return jpaTM;
	}

}
