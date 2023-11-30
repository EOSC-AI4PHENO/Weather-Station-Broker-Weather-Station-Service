package com.siseth.weatherStation;

import com.siseth.weatherStation.constants.AppProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class})
@EnableEurekaClient
@OpenAPIDefinition(
		servers = {
				@Server(url = "/", description = "Default Server URL")
		}
)
@EnableFeignClients
public class WeatherStationApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherStationApplication.class, args);
	}

}
