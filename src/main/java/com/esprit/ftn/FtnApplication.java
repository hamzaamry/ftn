package com.esprit.ftn;

import com.esprit.ftn.entities.User;
import com.esprit.ftn.entities.User.UserType;
import com.esprit.ftn.Repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FtnApplication {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public FtnApplication(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(FtnApplication.class, args);
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setEmail("admin@ftn.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setType(UserType.SUPER_ADMIN); // Utilise ton enum UserType
                userRepository.save(admin);
                System.out.println("Super admin créé !");
            }
        };
    }
}
