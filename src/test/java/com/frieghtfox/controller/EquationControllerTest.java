package com.frieghtfox.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frieghtfox.dto.EquationResponse;
import com.frieghtfox.dto.EvaluateEquationRequest;
import com.frieghtfox.dto.EvaluateEquationResponse;
import com.frieghtfox.dto.StoreEquationRequest;
import com.frieghtfox.dto.StoreEquationResponse;
import com.frieghtfox.service.EquationService;

@WebMvcTest(EquationController.class)
public class EquationControllerTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private EquationService equationService;

	@Test
	void testStoreEquation() throws Exception {
		StoreEquationRequest request = new StoreEquationRequest("3x + 2y - z");

		StoreEquationResponse response = new StoreEquationResponse(1, "Equation stored");
		when(equationService.storeEquation(request.getEquation())).thenReturn(response);

		mvc.perform(post("/api/equation/store").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.equationId").value(1)).andExpect(jsonPath("$.message").value("Equation stored"));
	}

	@Test
	void testRetrieveAPI() throws Exception {
		List<EquationResponse> mockResponses = List.of(new EquationResponse(1, "3x+2y-z"));

		when(equationService.retrieveExpression()).thenReturn(mockResponses);

		mvc.perform(get("/api/equation")).andExpect(status().isOk()).andExpect(jsonPath("$.equations").isArray())
				.andExpect(jsonPath("$.equations[0].equationId").value(1))
				.andExpect(jsonPath("$.equations[0].equation").value("3x+2y-z"));
	}

	@Test
	void testEvaluateEquationAPI() throws Exception {
		EvaluateEquationResponse mockResponse = new EvaluateEquationResponse(1, "3x+2y-z",
				Map.of("x", 2, "y", 2, "z", 2), 8);
		when(equationService.evaluate(1, Map.of("x", 2, "y", 2, "z", 2))).thenReturn(mockResponse);
		mvc.perform(post("/api/equation/1/evaluate")
				.content(asJsonString(new EvaluateEquationRequest(Map.of("x", 2, "y", 2, "z", 2))))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.result").value(8));
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
