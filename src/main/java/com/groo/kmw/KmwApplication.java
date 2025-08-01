package com.groo.kmw;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.io.File;

@EnableJpaAuditing
@SpringBootApplication
public class KmwApplication extends SpringBootServletInitializer {

    @Value("${file.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        try {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(KmwApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(KmwApplication.class, args);
	}

}
