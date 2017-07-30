package com.psu.sweng500.team4.parkpal_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ParkpalBackendApplication {

	public static void main(String[] args) {
	    SpringApplication.run(ParkpalBackendApplication.class, args);


	}
}
