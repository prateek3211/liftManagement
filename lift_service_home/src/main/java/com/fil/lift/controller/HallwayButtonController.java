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
import com.fil.lift.beans.HallwayButtonRequest;
import com.fil.lift.service.HallwayButtonService;

@RestController
@RequestMapping("/hall")
public class HallwayButtonController {

	private HallwayButtonService hallwayButtonService;

	public HallwayButtonController(HallwayButtonService hallwayButtonService) {
		this.hallwayButtonService = hallwayButtonService;
	}

	@PostMapping(path = "/button", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Boolean> createLiftRequest(@Valid @RequestBody HallwayButtonRequest hallwayButton) {
		return Collections.singletonMap(Constants.SUCCESS, 
				hallwayButtonService.processHallwayLiftRequest(hallwayButton));
	}
}
