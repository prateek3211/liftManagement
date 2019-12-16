package com.fil.lift.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder
@AllArgsConstructor
@Getter
public class LiftCarButtonRequest {
	private int targetFloor;
	private String liftId;
}
