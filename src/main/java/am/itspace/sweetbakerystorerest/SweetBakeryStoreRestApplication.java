package am.itspace.sweetbakerystorerest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = {"am.itspace.sweetbakerystorerest","am.itspace.sweetbakerystorecommon.*"})
@EntityScan(basePackages ="am.itspace.sweetbakerystorecommon.*")
@EnableJpaRepositories(basePackages = "am.itspace.sweetbakerystorecommon.*")
public class SweetBakeryStoreRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SweetBakeryStoreRestApplication.class, args);

    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


}
