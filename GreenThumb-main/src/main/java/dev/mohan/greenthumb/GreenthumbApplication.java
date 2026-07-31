package dev.mohan.greenthumb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing          // ← turns on @CreatedDate / @LastModifiedDate
public class GreenthumbApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenthumbApplication.class, args);
	}

}
