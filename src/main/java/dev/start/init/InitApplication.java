package dev.start.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InitApplication {

	public static void main(String[] args) {
		SpringApplication.run(InitApplication.class, args);
	}

	//http://localhost:5173 to 8080
	//Cross origin request (COR) not allow by default
	//Allow all request only from http://localhost:3000
	/*@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurer(){
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH","OPTIONS")
						.allowCredentials(true)  //// Allow cookies for cross-origin requests
						.allowedHeaders("Content-Type", "Authorization")
						.allowedOrigins("http://localhost:3000/");
			}

		};
	}*/

}
