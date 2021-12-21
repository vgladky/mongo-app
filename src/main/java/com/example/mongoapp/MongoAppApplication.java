package com.example.mongoapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class MongoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoAppApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(StudentRepository repository, MongoTemplate mongoTemplate) {
        return args -> {
            Address address = new Address("USA",
                    "New York",
                    "NY123");

            String email = "john@doe.com";
            Student student = new Student("John",
                    "Doe",
                    email,
                    Gender.MALE,
                    address,
                    List.of("Computer Science", "Maths"),
                    BigDecimal.TEN,
                    LocalDateTime.now());

            Query query = new Query();
            query.addCriteria(Criteria.where("email").is(email));

            List<Student> students = mongoTemplate.find(query, Student.class);

            if (students.size() > 1) {
                throw new IllegalStateException("Found many students with email " + email);
            }

            if (students.isEmpty()) {
                System.out.println("Inserting student " + student);
                repository.insert(student);
            } else {
                System.out.println(student + " already exists");
            }
        };
    }
}
