package com.fil.lift.beans;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class HallwayButtonRequest {
	@NotNull
	private Direction requestedTargetDirection;
	@Min(0)
	private int currentFloor;
	@NotEmpty
	private String group;
}
