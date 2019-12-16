package com.fil.lift.service;

import java.util.Collections;

import org.apache.commons.collections4.map.LRUMap;
import org.springframework.stereotype.Component;

import com.fil.lift.beans.Direction;
import com.fil.lift.beans.Lift;
import com.fil.lift.beans.LiftCarButtonRequest;
import com.fil.lift.config.EnvironmentProperties;

@Component
public class LiftCarButtonService {

	private LRUMap cacheManager;

	public LiftCarButtonService(LRUMap cacheManager) {
		this.cacheManager = cacheManager;
	}

	public boolean processLiftCarRequest(LiftCarButtonRequest liftCarButton) {
		Lift occupiedLift = (Lift) cacheManager.get(liftCarButton.getLiftId());
		boolean isLiftAllocated = false;
		if (Direction.UP.equals(occupiedLift.getTargetDirection())
				&& liftCarButton.getTargetFloor() > occupiedLift.getLiftCurrentFloor()
				&& occupiedLift.getAllowedFloors().contains(String.valueOf(liftCarButton.getTargetFloor()))) {
			occupiedLift.getStops().add(liftCarButton.getTargetFloor());
			Collections.sort(occupiedLift.getStops());
			isLiftAllocated = true;
		} else if (Direction.DOWN.equals(occupiedLift.getTargetDirection())
				&& liftCarButton.getTargetFloor() < occupiedLift.getLiftCurrentFloor()
				&& occupiedLift.getAllowedFloors().contains(String.valueOf(liftCarButton.getTargetFloor()))) {
			occupiedLift.getStops().add(liftCarButton.getTargetFloor());
			Collections.sort(occupiedLift.getStops(), Collections.reverseOrder());
			isLiftAllocated = true;
		}
		return isLiftAllocated;
	}

}
