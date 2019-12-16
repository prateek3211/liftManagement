package com.fil.lift.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
/**
 * This class is only used to demonstrate/update lift current floor 
 *
 */
public class LiftRequest {
	private String liftId;
	private int liftCurrentFloor;
}
