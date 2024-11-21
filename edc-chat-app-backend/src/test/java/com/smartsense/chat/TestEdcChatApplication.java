package com.smartsense.chat;

import org.springframework.boot.SpringApplication;

public class TestEdcChatApplication {

	public static void main(String[] args) {
		SpringApplication.from(EdcChatApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
