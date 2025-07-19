package com.LibraryManagement.LibraryUserManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class 	LibraryUserManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryUserManagementApplication.class, args);
	}

}
