package com.busfare.calculate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CalculatorApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(CalculatorApplication.class, args);
		
		FareProcessor service = applicationContext.getBean(FareProcessor.class);
	    service.readTaps();
		
	}

}

