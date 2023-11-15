package com.example.demo.unit;

import com.example.demo.controller.PersonController;
import com.example.demo.model.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private PersonController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Test
	void createPersonOk() throws Exception {
		Person person = new Person("Jane", "Doe", LocalDate.of(2010, 12, 31));

		mvc.perform(post("/demo/persons")
						.content(asJsonString(person))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName", is(person.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(person.getLastName())))
				.andExpect(jsonPath("$.birthDate", is(person.getBirthDate().toString())));
	}

	@Test
	void createPersonNullInvalid() throws Exception {
		mvc.perform(post("/demo/persons")
						.content(asJsonString(null))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void createPersonFirstNameInvalid() throws Exception {
		Person person = new Person("Janejanejanejanejanejane", "Doe", LocalDate.of(2010, 12, 31));

		mvc.perform(post("/demo/persons")
						.content(asJsonString(person))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void createPersonLastNameInvalid() throws Exception {
		Person person = new Person("Jane", "D", LocalDate.of(2010, 12, 31));

		mvc.perform(post("/demo/persons")
						.content(asJsonString(person))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void createPersonBirthDateInvalid() throws Exception {
		Person person = new Person("Jane", "Doe", LocalDate.of(2030, 12, 31));

		mvc.perform(post("/demo/persons")
						.content(asJsonString(person))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	private static String asJsonString(final Object obj) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
