package com.example.utm;

import com.example.utm.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UtmApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	// DİKKAT: Bu satır eklendi.
	@MockBean
	private MailService mailService;

	@Test
	void contextLoads() {
		// Bu test artık, MailService'in sahte versiyonuyla birlikte
		// Spring Context'in başarıyla yüklendiğini kontrol edecek.
	}

	@Test
	void whenGetUstalar_thenStatus200() throws Exception {
		mockMvc.perform(get("/api/ustalar"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
}