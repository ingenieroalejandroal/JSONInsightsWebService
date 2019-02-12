package com.waes.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.waes.business.service.JSONDiffService;
import com.waes.business.service.utility.Constants;
import com.waes.data.entity.JsonTracking;
import com.waes.web.service.WaesTechTestServiceController;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import static org.mockito.BDDMockito.given;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(WaesTechTestServiceController.class)
public class WaesTechTestServiceControllerTest {

	@MockBean
	private JSONDiffService jSONDiffService;

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Check if the POST request /v1/diff/{id}/ from the controller is working
	 * 
	 * @throws Exception
	 */
	@Test
	public void checkAddAJsonTrackingObject() throws Exception {
		JsonTracking jsonTracking = new JsonTracking(1L, "1", "eyJ1c2VyVHlwZSI6ImFkbWluIiwiaWQiOjEyM30=",
				Constants.RIGHT_SIDE, LocalDate.now(), null);
		MockHttpServletResponse response;
		given(jSONDiffService.createOrUpdate(jsonTracking.getJsonId(), jsonTracking.getBase64EncodedJson(),
				jsonTracking.getSide())).willReturn(jsonTracking);
		response = mockMvc
				.perform(post("/v1/diff/{id}/" + Constants.RIGHT_SIDE, jsonTracking.getJsonId())
						.param("base64EncodedJson", jsonTracking.getBase64EncodedJson()))
				.andExpect(status().isOk()).andReturn().getResponse();
		assertTrue(response.getContentAsString().startsWith(
				"{\"id\":1,\"jsonId\":\"1\",\"base64EncodedJson\":\"eyJ1c2VyVHlwZSI6ImFkbWluIiwiaWQiOjEyM30=\",\"side\":\"right\",\"createdDate"));
	}

	/**
	 * Check if the insights between two Base 64 encoded JSONs returns expected data
	 * by calling the end point /v1/diff/{id} from the controller
	 * 
	 * @throws Exception
	 */
	@Test
	public void checkInsightsBetweenTwoJSONs() throws Exception {
		// Expected JSON result for the testing
		JsonObject jsonObject = Json.createObjectBuilder().add("isEqual", Boolean.TRUE).build();

		// 1. Add JSON to left
		JsonTracking jsonTracking1 = new JsonTracking(1L, "1", "eyJ1c2VyVHlwZSI6ImFkbWluIiwiaWQiOjEyM30=",
				Constants.LEFT_SIDE, LocalDate.now(), null);
		given(jSONDiffService.createOrUpdate(jsonTracking1.getJsonId(), jsonTracking1.getBase64EncodedJson(),
				jsonTracking1.getSide())).willReturn(jsonTracking1);
		mockMvc.perform(post("/v1/diff/{id}/" + Constants.LEFT_SIDE, jsonTracking1.getJsonId())
				.param("base64EncodedJson", jsonTracking1.getBase64EncodedJson())).andExpect(status().isOk())
				.andReturn().getResponse();

		// 2. Now add same jsonId to left
		JsonTracking jsonTracking2 = new JsonTracking(2L, "1", "eyJ1c2VyVHlwZSI6ImFkbWluIiwiaWQiOjEyM30=",
				Constants.RIGHT_SIDE, LocalDate.now(), null);
		given(jSONDiffService.createOrUpdate(jsonTracking2.getJsonId(), jsonTracking2.getBase64EncodedJson(),
				jsonTracking2.getSide())).willReturn(jsonTracking2);
		mockMvc.perform(post("/v1/diff/{id}/" + Constants.RIGHT_SIDE, jsonTracking2.getJsonId())
				.param("base64EncodedJson", jsonTracking2.getBase64EncodedJson())).andExpect(status().isOk())
				.andReturn().getResponse();

		// 3. Get Insights
		given(jSONDiffService.findByJsonIdAndSide(jsonTracking1.getJsonId(), Constants.LEFT_SIDE))
				.willReturn(jsonTracking1);
		given(jSONDiffService.findByJsonIdAndSide(jsonTracking2.getJsonId(), Constants.RIGHT_SIDE))
				.willReturn(jsonTracking2);
		given(jSONDiffService.provideInsights(jsonTracking1.getBase64EncodedJson(),
				jsonTracking2.getBase64EncodedJson())).willReturn(jsonObject);
		MockHttpServletResponse response = mockMvc.perform(get("/v1/diff/{id}", jsonTracking1.getJsonId()))
				.andExpect(status().isOk()).andReturn().getResponse();
		assertTrue(response.getContentAsString().equals("{\"isEqual\":true}"));
	}

	/**
	 * Check if by calling the end point /v1/diff returns a list of Objects
	 * 
	 * @throws Exception
	 */
	@Test
	public void checkGetAll() throws Exception {
		MockHttpServletResponse response;
		JsonTracking jsonTracking1 = new JsonTracking(1L, "1", "eyJ1c2VyVHlwZSI6ImFkbWluIiwiaWQiOjEyM30=",
				Constants.RIGHT_SIDE, LocalDate.now(), null);
		JsonTracking jsonTracking2 = new JsonTracking(2L, "2", "uuwy736363ywywywwyyyw88iui==", Constants.LEFT_SIDE,
				LocalDate.now(), null);
		List<JsonTracking> jsonTrackingList = new ArrayList<>();
		jsonTrackingList.add(jsonTracking1);
		jsonTrackingList.add(jsonTracking2);
		given(jSONDiffService.findAll()).willReturn(jsonTrackingList);
		response = mockMvc.perform(get("/v1/diff")).andExpect(status().isOk()).andReturn().getResponse();
		assertTrue(response.getContentAsString().startsWith(
				"[{\"id\":1,\"jsonId\":\"1\",\"base64EncodedJson\":\"eyJ1c2VyVHlwZSI6ImFkbWluIiwiaWQiOjEyM30=\",\"side\":\"right\",\"createdDate"));
	}

}
