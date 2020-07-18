package com.bank;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.auth.AuthApplication;
import com.auth.service.TransferService;
import com.auth.vo.TransferRequest;
import com.fasterxml.jackson.databind.ObjectMapper;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes=AuthApplication.class)
@AutoConfigureMockMvc
public class TransferRestTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TransferService tranferService;

	@Test
	void transferControllerTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		TransferRequest tr = TransferRequest.builder()
				.bankAccount("12345678987656")
				.bankCd("W1")
				.accountHolder("라볶이")
				.authenticationRequestSerialNum(1)
				.build();
		mvc.perform(post("/auth/transfer").content(objectMapper.writeValueAsBytes(tr))
				.contentType(MediaType.APPLICATION_JSON));
		
	}
}
