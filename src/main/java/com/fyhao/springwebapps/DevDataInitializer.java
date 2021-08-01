package com.fyhao.springwebapps;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevDataInitializer implements CommandLineRunner {

	@Override
    public void run(String... args) throws Exception {
		System.out.println("Run DevDataInitializer");
		
	}
}
