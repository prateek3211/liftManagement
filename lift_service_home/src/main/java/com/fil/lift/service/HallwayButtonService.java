package com.fil.lift.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.fil.lift.beans.Direction;
import com.fil.lift.beans.HallwayButtonRequest;
import com.fil.lift.beans.Lift;
import com.fil.lift.config.EnvironmentProperties;

@Component
public class HallwayButtonService {

	private EnvironmentProperties environment;
	private LRUMap cacheManager;

	public HallwayButtonService(EnvironmentProperties environment, LRUMap cacheManager) {
		this.environment = environment;
		this.cacheManager = cacheManager;
	}

	public boolean processHallwayLiftRequest(HallwayButtonRequest hallwayButton) {
		boolean isLiftAllocated = false;
		boolean isValidFloor = false;
		Map<String, List<String>> liftsFloorMappingData = environment.getLiftGroup().get(hallwayButton.getGroup());
		for (Entry<String, List<String>> entry : liftsFloorMappingData.entrySet()) {
			if (entry.getValue().contains(String.valueOf(hallwayButton.getCurrentFloor()))) {
				isValidFloor = true;
				Lift liftData = (Lift) cacheManager.get(entry.getKey());
				if (liftData == null) {
					liftData = Lift.builder().liftCurrentFloor(0).liftTargetFloor(hallwayButton.getCurrentFloor())
							.targetDirection(hallwayButton.getRequestedTargetDirection())
							.groupId(hallwayButton.getGroup())
							.currrentDirection(Direction.UP).id(entry.getKey()).allowedFloors(entry.getValue())
							.stops(Stream.of(hallwayButton.getCurrentFloor()).collect(Collectors.toList())).build();
					cacheManager.put(entry.getKey(), liftData);
					isLiftAllocated = true;
					break;
				} else {
					if (hallwayButton.getCurrentFloor() > liftData.getLiftCurrentFloor()
							&& StringUtils.equalsIgnoreCase(hallwayButton.getRequestedTargetDirection().toString(),
									liftData.getTargetDirection().toString())) {
						List<Integer> currentStops = liftData.getStops();
						if (StringUtils.equalsIgnoreCase(liftData.getCurrrentDirection().toString(),
								liftData.getTargetDirection().toString())) {
							currentStops.add(hallwayButton.getCurrentFloor());
							Collections.sort(currentStops);
							liftData.setLiftTargetFloor(currentStops.get(currentStops.size() - 1));
							isLiftAllocated = true;
							break;
						} else {
							if (hallwayButton.getCurrentFloor() < liftData.getLiftTargetFloor()) {
								liftData.setLiftTargetFloor(hallwayButton.getCurrentFloor());
								currentStops.add(hallwayButton.getCurrentFloor());
								Collections.sort(currentStops, Collections.reverseOrder());
								isLiftAllocated = true;
								break;
							}
						}
					}
				}
			}

		}
		if (!isLiftAllocated && isValidFloor) {
			List<HallwayButtonRequest> existingQueue = (List<HallwayButtonRequest>) cacheManager
					.get(hallwayButton.getGroup());
			if (CollectionUtils.isEmpty(existingQueue)) {
				existingQueue = Stream.of(hallwayButton).collect(Collectors.toList());
			} else {
				existingQueue.add(hallwayButton);
			}
			cacheManager.put(hallwayButton.getGroup(), existingQueue);
		}
		return isLiftAllocated;
	}
}
