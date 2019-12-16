package com.fil.lift.service;

import org.apache.commons.collections4.map.LRUMap;
import org.springframework.stereotype.Component;

import com.fil.lift.beans.Lift;
import com.fil.lift.beans.LiftRequest;

@Component
public class LiftService {

	private LRUMap cacheManager;

	public LiftService(LRUMap cacheManager) {
		this.cacheManager = cacheManager;
	}

	public boolean updateLiftLocation(LiftRequest liftRequest) {
		Lift lift = (Lift) cacheManager.get(liftRequest.getLiftId());
		lift.setLiftCurrentFloor(liftRequest.getLiftCurrentFloor());
		if (lift.getStops().contains(liftRequest.getLiftCurrentFloor())) {
			lift.getStops().remove(liftRequest.getLiftCurrentFloor());
		}
		// add logic to pick queue request
		return true;
	}

}
