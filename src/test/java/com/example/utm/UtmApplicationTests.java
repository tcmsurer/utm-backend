package com.example.utm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

	@Test
	void contextLoads() {
		// Bu test, Spring Application Context'in başarıyla yüklenip yüklenmediğini kontrol eder.
		// Eğer bir Bean oluşturulamıyorsa veya konfigürasyonda hata varsa bu test başarısız olur.
	}

	@Test
	void whenGetUstalar_thenStatus200() throws Exception {
		// Halka açık olan "ustalar" endpoint'ine bir GET isteği yap
		mockMvc.perform(get("/api/ustalar"))
				// HTTP durum kodunun 200 (OK) olmasını bekle
				.andExpect(status().isOk())
				// Dönen içeriğin tipinin JSON olmasını bekle
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	// Gelecekte buraya yeni testler eklenebilir. Örneğin:
	// - Geçersiz bir kullanıcı ile login olmayı deneyen bir test (401 Unauthorized beklenir).
	// - Yetkisiz bir kullanıcının admin endpoint'ine erişmeye çalıştığı bir test (403 Forbidden beklenir).
}