package com.openai;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ServletComponentScan
@EnableAsync
public class OpenAiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(OpenAiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		

	}

}
