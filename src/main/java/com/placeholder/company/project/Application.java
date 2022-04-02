package com.placeholder.company.project;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.placeholder.company.project.db.model.Admin;
import com.placeholder.company.project.db.repository.AdminRepository;

@SpringBootApplication
public class Application {

	private final AdminRepository adminRepository;

	public Application( @Qualifier( "adminRepository" ) AdminRepository adminRepository ) {
		this.adminRepository = adminRepository;
	}

	public static void main( String[] args ) {
		SpringApplication.run( Application.class, args );
	}

	@Bean
	InitializingBean seedAdmins() {
		return () -> {
			Admin admin = new Admin( "test", "test", "test" );
			admin.setToken( "test" );
			adminRepository.save( admin );
		};
	}
}
