package com.example.jpaelastic;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(
		exclude = {
				//ElasticsearchAutoConfiguration.class,
				ElasticsearchDataAutoConfiguration.class,
				ElasticsearchRepositoriesAutoConfiguration.class
		}
)
public class JpaelasticApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaelasticApplication.class, args);
	}

	/*
	@Bean
	public CommandLineRunner syncToEs(ProductService productService) {
		return args -> {
			productService.syncProductsToElasticsearch();
		};
	}
	*/

}
