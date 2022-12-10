package am.itspace.sweetbakerystorerest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"am.itspace.sweetbakerystorerest","am.itspace.sweetbakerystorecommon.*"})
@EntityScan(basePackages ="am.itspace.sweetbakerystorecommon.*")
@EnableJpaRepositories(basePackages = "am.itspace.sweetbakerystorecommon.*")
public class SweetBakeryStoreRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SweetBakeryStoreRestApplication.class, args);
    }

}
