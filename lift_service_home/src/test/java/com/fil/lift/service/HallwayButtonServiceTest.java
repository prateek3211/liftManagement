package com.fil.lift.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.fil.lift.beans.HallwayButtonRequest;
import com.fil.lift.beans.Lift;
import com.fil.lift.config.EnvironmentProperties;

@RunWith(MockitoJUnitRunner.class)
public class HallwayButtonServiceTest {

	private HallwayButtonService hallwayButtonService;
	@Mock
	private EnvironmentProperties environment;
	@Mock
	private LRUMap cacheManager;

	@Before
	public void setup() {
		hallwayButtonService = new HallwayButtonService(environment, cacheManager);
	}

	/**
	 * Test should pass and add floor as requested
	 */
	@Test
	public void tesAddtValidFloor() {
		Map<String, Map<String, List<String>>> dummyGroupData = new HashMap<>();
		Map<String, List<String>> dummyGroupToLiftMapping = new HashMap<>();
		List<String> dummyLiftToFloorMapping = new ArrayList<String>();
		dummyLiftToFloorMapping.add("0");
		dummyLiftToFloorMapping.add("1");
		dummyLiftToFloorMapping.add("3");
		dummyLiftToFloorMapping.add("5");
		dummyLiftToFloorMapping.add("7");
		dummyGroupToLiftMapping.put("L1", dummyLiftToFloorMapping);
		dummyGroupData.put("G1", dummyGroupToLiftMapping);
		Mockito.when(environment.getLiftGroup()).thenReturn(dummyGroupData);
		Assert.assertTrue(hallwayButtonService.processHallwayLiftRequest(
				HallwayButtonRequest.builder().group("G1")
				.currentFloor(5)
				.requestedTargetDirection(Direction.UP).build()));
	}

	/**
	 * In-valid floor should not be added
	 * Negative test case
	 */
	@Test
	public void testAddInValidFloor() {
		Map<String, Map<String, List<String>>> dummyGroupData = new HashMap<>();
		Map<String, List<String>> dummyGroupToLiftMapping = new HashMap<>();
		List<String> dummyLiftToFloorMapping = new ArrayList<String>();
		dummyLiftToFloorMapping.add("0");
		dummyLiftToFloorMapping.add("1");
		dummyLiftToFloorMapping.add("3");
		dummyLiftToFloorMapping.add("5");
		dummyLiftToFloorMapping.add("7");
		dummyGroupToLiftMapping.put("L1", dummyLiftToFloorMapping);
		dummyGroupData.put("G1", dummyGroupToLiftMapping);
		Mockito.when(environment.getLiftGroup()).thenReturn(dummyGroupData);
		Assert.assertFalse(hallwayButtonService.processHallwayLiftRequest(
				HallwayButtonRequest.builder().group("G1")
				.currentFloor(2)
				.requestedTargetDirection(Direction.UP).build()));
	}

	/**
	 * Adding floor when lift current direction, lift target direction and 
	 * requested direction are in same direction
	 */
	@Test
	public void testAddFloorForSameDirection() {
		Map<String, Map<String, List<String>>> dummyGroupData = new HashMap<>();
		Map<String, List<String>> dummyGroupToLiftMapping = new HashMap<>();
		List<String> dummyLiftToFloorMapping = new ArrayList<String>();
		dummyLiftToFloorMapping.add("0");
		dummyLiftToFloorMapping.add("1");
		dummyLiftToFloorMapping.add("3");
		dummyLiftToFloorMapping.add("5");
		dummyLiftToFloorMapping.add("7");
		dummyGroupToLiftMapping.put("L1", dummyLiftToFloorMapping);
		dummyGroupData.put("G1", dummyGroupToLiftMapping);
		Mockito.when(environment.getLiftGroup()).thenReturn(dummyGroupData);
		Mockito.when(cacheManager.get("L1"))
				.thenReturn(Lift.builder()
						.currrentDirection(Direction.UP)
						.liftCurrentFloor(0).liftTargetFloor(3)
						.targetDirection(Direction.UP)
						.stops(Stream.of(3).collect(Collectors.toList())).build());
		Assert.assertTrue(hallwayButtonService.processHallwayLiftRequest(
				HallwayButtonRequest.builder().group("G1").currentFloor(5).requestedTargetDirection(Direction.UP).build()));
	}

	/**
	 * Adding floor when lift target direction and 2nd request target direction are
	 * in the same direction but current direction is opposite
	 */
	@Test
	public void testAddFloorForDifferentDirection() {
		Map<String, Map<String, List<String>>> dummyGroupData = new HashMap<>();
		Map<String, List<String>> dummyGroupToLiftMapping = new HashMap<>();
		List<String> dummyLiftToFloorMapping = new ArrayList<String>();
		dummyLiftToFloorMapping.add("0");
		dummyLiftToFloorMapping.add("1");
		dummyLiftToFloorMapping.add("3");
		dummyLiftToFloorMapping.add("5");
		dummyLiftToFloorMapping.add("7");
		dummyGroupToLiftMapping.put("L1", dummyLiftToFloorMapping);
		dummyGroupData.put("G1", dummyGroupToLiftMapping);
		Mockito.when(environment.getLiftGroup()).thenReturn(dummyGroupData);
		Mockito.when(cacheManager.get("L1"))
				.thenReturn(Lift.builder()
						.currrentDirection(Direction.UP)
						.liftCurrentFloor(0)
						.liftTargetFloor(7)
						.targetDirection(Direction.DOWN)
						.stops(Stream.of(7).collect(Collectors.toList())).build());
		Assert.assertTrue(hallwayButtonService.processHallwayLiftRequest(
				HallwayButtonRequest.builder().group("G1")
				.currentFloor(5)
				.requestedTargetDirection(Direction.DOWN).build()));
	}

	/**
	 * When lift has already passed the floor i.e
	 * you are on 11th floor but list if on 9th floor & already going down
	 * Negative test case
	 */
	@Test
	public void testAddFloorForLateRequestInSameDirection() {
		Map<String, Map<String, List<String>>> dummyGroupData = new HashMap<>();
		Map<String, List<String>> dummyGroupToLiftMapping = new HashMap<>();
		List<String> dummyLiftToFloorMapping = new ArrayList<String>();
		dummyLiftToFloorMapping.add("0");
		dummyLiftToFloorMapping.add("1");
		dummyLiftToFloorMapping.add("3");
		dummyLiftToFloorMapping.add("5");
		dummyLiftToFloorMapping.add("7");
		dummyLiftToFloorMapping.add("9");
		dummyLiftToFloorMapping.add("11");
		dummyGroupToLiftMapping.put("L1", dummyLiftToFloorMapping);
		dummyGroupData.put("G1", dummyGroupToLiftMapping);
		Mockito.when(environment.getLiftGroup()).thenReturn(dummyGroupData);
		Mockito.when(cacheManager.get("L1"))
				.thenReturn(Lift.builder().currrentDirection(Direction.UP)
						.liftCurrentFloor(0)
						.liftTargetFloor(7)
						.targetDirection(Direction.DOWN)
						.stops(Stream.of(7).collect(Collectors.toList())).build());
		Assert.assertFalse(hallwayButtonService.processHallwayLiftRequest(
				HallwayButtonRequest.builder().group("G1")
				.currentFloor(11).requestedTargetDirection(Direction.DOWN).build()));
	}

}
