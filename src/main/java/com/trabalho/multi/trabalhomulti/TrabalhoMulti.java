package com.trabalho.multi.trabalhomulti;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.datastax.oss.driver.api.core.CqlSession;
import com.trabalho.multi.trabalhomulti.connection.ConnectDatabase;
import com.trabalho.multi.trabalhomulti.connection.DataStaxAstraProperties;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableCassandraRepositories
// @ComponentScan({"controller", "services", "configure"})
// @EnableCassandraRepositories(basePackageClasses = { VehicleRepository.class,
// LastPositionRepository.class,
// DataByVehicleRepository.class }, considerNestedRepositories = false)
// @EnableConfigurationProperties(DataStaxAstraProperties.class)
public class TrabalhoMulti {
	@Autowired
	CqlSession session;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(TrabalhoMulti.class, args);

	}

	@PostConstruct
	public void start() {

	}

	@Bean
	CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

}
