package com.frieghtfox.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frieghtfox.dto.EquationResponse;
import com.frieghtfox.dto.EvaluateEquationRequest;
import com.frieghtfox.dto.EvaluateEquationResponse;
import com.frieghtfox.dto.RetrieveEquationResponse;
import com.frieghtfox.dto.StoreEquationRequest;
import com.frieghtfox.dto.StoreEquationResponse;
import com.frieghtfox.service.EquationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/equation")
public class EquationController {

	private final EquationService equationService;

	public EquationController(EquationService equationService) {
		this.equationService = equationService;
	}

	@PostMapping("/store")
	public ResponseEntity<StoreEquationResponse> storeEquation(@Valid @RequestBody StoreEquationRequest request) {
		StoreEquationResponse res = equationService.storeEquation(request.getEquation());
		return new ResponseEntity<>(res, HttpStatus.CREATED);
	}

	// retrieve equation
	@GetMapping("")
	public ResponseEntity<RetrieveEquationResponse> retriveEquation() {
		List<EquationResponse> responses = equationService.retrieveExpression();
		return new ResponseEntity<>(new RetrieveEquationResponse(responses), HttpStatus.OK);
	}

	// evaluate equation
	@PostMapping("/{equationId}/evaluate")
	public ResponseEntity<EvaluateEquationResponse> evaluateEquation(@PathVariable("equationId") int equationId,
			@Valid @RequestBody EvaluateEquationRequest request) {
		EvaluateEquationResponse response = equationService.evaluate(equationId, request.getVariables());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
