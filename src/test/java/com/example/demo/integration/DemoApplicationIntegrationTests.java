package com.example.demo.integration;

import com.example.demo.DemoApplication;
import com.example.demo.controller.PersonController;
import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class DemoApplicationIntegrationTests {

    @Deployment()
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addClasses(Person.class, PersonController.class, PersonRepository.class, DemoApplication.class)
                .addAsManifestResource("applicationContext.xml");
    }

    @Inject
    private PersonRepository personRepository;

    @Test
    public void createPerson() {
//        personRepository.save(new Person("Jane", "Doe", LocalDate.of(2000, 12, 30)));
//        Assertions.assertTrue(personRepository.existsById(1L));
    }
}
