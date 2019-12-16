package com.fil.lift.service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.map.LRUMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fil.lift.beans.Direction;
import com.fil.lift.beans.Lift;
import com.fil.lift.beans.LiftCarButtonRequest;

@RunWith(MockitoJUnitRunner.class)
public class LiftCarButtonServiceTest {

	private LiftCarButtonService liftCarButtonService;
	@Mock
	private LRUMap cacheManager;

	@Before
	public void setup() {
		liftCarButtonService = new LiftCarButtonService(cacheManager);
	}

	/**
	 * Testing for scenario where request lift has arrived and after entering lift
	 * user presses the destination floor in correct direction
	 */
	@Test
	public void testUpDirectionRequestForSameDirection() {
		Mockito.when(cacheManager.get("L1"))
				.thenReturn(Lift.builder().targetDirection(Direction.UP).liftCurrentFloor(5)
						.stops(Stream.of(7, 9).collect(Collectors.toList()))
						.allowedFloors(Stream.of("7", "9").collect(Collectors.toList()))
						.build());
		Assert.assertTrue(liftCarButtonService
				.processLiftCarRequest(LiftCarButtonRequest.builder()
						.targetFloor(9).liftId("L1").build()));
	}
	
	/**
	 * Negative test case
	 */
	@Test
	public void testUpDirectionRequestForReverseDirection() {
		Mockito.when(cacheManager.get("L1"))
				.thenReturn(Lift.builder().targetDirection(Direction.UP).liftCurrentFloor(5)
						.stops(Stream.of(7, 9).collect(Collectors.toList()))
						.allowedFloors(Stream.of("7", "9").collect(Collectors.toList()))
						.build());
		Assert.assertFalse(liftCarButtonService
				.processLiftCarRequest(LiftCarButtonRequest.builder()
						.targetFloor(3).liftId("L1").build()));
	}
	
	/**
	 * Testing for scenario where request lift has arrived and after entering lift
	 * user presses the destination floor in correct direction
	 */
	@Test
	public void testDownDirectionRequestForSameDirection() {
		Mockito.when(cacheManager.get("L1"))
				.thenReturn(Lift.builder().targetDirection(Direction.DOWN).liftCurrentFloor(9)
						.stops(Stream.of(5, 7).collect(Collectors.toList()))
						.allowedFloors(Stream.of("5", "7").collect(Collectors.toList()))
						.build());
		Assert.assertTrue(liftCarButtonService
				.processLiftCarRequest(LiftCarButtonRequest.builder()
						.targetFloor(5).liftId("L1").build()));
	}
	
}
