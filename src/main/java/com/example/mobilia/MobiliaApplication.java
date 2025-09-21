package com.example.mobilia;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cloudinary.Cloudinary;


@SpringBootApplication
public class MobiliaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobiliaApplication.class, args);
	}

	@Value("${CLOUDINARY_URL}")
    private String cloudinaryUrl;

	@Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary( cloudinaryUrl);
        cloudinary.config.secure = true;
        return cloudinary;
    }

}
