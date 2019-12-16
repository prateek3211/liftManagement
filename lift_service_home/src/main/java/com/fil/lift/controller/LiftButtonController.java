package com.fil.lift.controller;

import java.util.Collections;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fil.lift.beans.Constants;
import com.fil.lift.beans.LiftCarButtonRequest;
import com.fil.lift.service.LiftCarButtonService;

@RestController
@RequestMapping("/lift")
public class LiftButtonController {

	private LiftCarButtonService liftCarButtonService;
	
	public LiftButtonController(LiftCarButtonService liftCarButtonService) {
		this.liftCarButtonService = liftCarButtonService;
	}

	@PostMapping(path = "/button", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> createLiftRequest(@Valid @RequestBody LiftCarButtonRequest liftCarButton) {
		return Collections.singletonMap(Constants.SUCCESS, 
				liftCarButtonService.processLiftCarRequest(liftCarButton));
	}

}
