package com.fil.lift.beans;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Lift {
	private String id;
	private int liftTargetFloor;
	private Direction targetDirection;
	private Direction currrentDirection;
	private int liftCurrentFloor;
	private List<Integer> stops;
	private List<String> allowedFloors;
	private String groupId;
}
