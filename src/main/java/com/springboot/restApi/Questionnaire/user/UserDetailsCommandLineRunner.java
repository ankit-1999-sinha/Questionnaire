package com.springboot.restApi.Questionnaire.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserDetailsCommandLineRunner implements CommandLineRunner {

   private Logger logger = LoggerFactory.getLogger(getClass());


   private UserDetailsRepository repository;

    public UserDetailsCommandLineRunner(UserDetailsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        repository.save(new UserDetails("Ankit","Admin"));
        repository.save(new UserDetails("Ravi","Admin"));

        List<UserDetails> users = repository.findByRole("Admin");

        users.forEach(user -> logger.info(user.toString()));
    }
}
