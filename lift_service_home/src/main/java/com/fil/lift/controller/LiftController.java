package com.fil.lift.controller;

import org.apache.commons.collections4.map.LRUMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fil.lift.beans.LiftRequest;
import com.fil.lift.service.LiftService;

@RestController
@RequestMapping("/lift")
public class LiftController {

	private LiftService liftService;
	private LRUMap cacheManager;

	public LiftController(LiftService liftService, LRUMap cacheManager) {
		this.liftService = liftService;
		this.cacheManager = cacheManager;
	}

	@GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getStatusOfLifts() {
		return cacheManager.entrySet();
	}

	@PostMapping(path = "/currentLocation", produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean updateLiftLocation(@RequestBody LiftRequest liftRequest) {
		return liftService.updateLiftLocation(liftRequest);
	}

}
