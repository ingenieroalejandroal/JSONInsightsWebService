package com.waes.web.service;

import java.util.List;

import javax.json.Json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.waes.data.entity.JsonTracking;

import com.waes.business.service.JSONDiffService;
import com.waes.business.service.utility.Constants;

/**
 * Restful Web Service with 4 end points:
 * 
 * 1. /v1/diff/{id}/left : POST request to create or update a new JsonTracking
 * Object into the H2 database. It reads the path variable {id} and the request
 * parameter base64EncodedJson. The attribute side from the object JsonTracking
 * will have the value 'left'
 * 
 * 2. /v1/diff/{id}/right : POST request to create or update a new JsonTracking
 * Object into the H2 database. It reads the path variable {id} and the request
 * parameter base64EncodedJson. The attribute side from the object JsonTracking
 * will have the value 'right'
 * 
 * 3. /v1/diff/{id} : GET request that returns the insights between the two
 * objects found in database (left and right) corresponding to the path variable
 * {id}. It produces a JSON response according to the following logic:
 * 
 * SAMPLE: - jsonLeft (BASE64 Encoded) =
 * eyJ1c2VyVHlwZSI6ImFkbWluIiwiaWQiOjEyM30= - jsonRight (BASE64 Encoded) =
 * eyJ1c2VyVHlwZSI6InVzZXIiLCJpZCI6MTIzfQ==
 * 
 * Decoded: - jsonLeft = {"userType":"admin","id":123} - jsonRight =
 * {"userType":"user","id":123}
 * 
 * - If jsonLeft is equals jsonRight the JSON response would be:
 * {"isEqual":"true"}
 * 
 * - If jsonLeft is NOT equals jsonRight AND not equal size the JSON response
 * would be: {"isEqual":"false","isEqualSize":"false"}
 * 
 * - If jsonLeft is NOT equals jsonRight AND equal size the JSON response would
 * be: {"isEqual":"false","isEqualSize":"true",
 * "diffs":"[Diff(EQUAL,"{"userType":""), Diff(DELETE,"admin"),
 * Diff(INSERT,"user"), Diff(EQUAL,"","id":123}")]"}
 * 
 * All data is being stored into an in memory H2 database:
 * src\main\resources\schema.sql
 * 
 * @author Alejandro Aguirre
 * @version 1.0
 * @since 2019/02/10
 *
 */
@RestController
@RequestMapping(value = "/v1")
public class WaesTechTestServiceController {

	@Autowired
	private JSONDiffService JSONDiffService;

	@RequestMapping(method = RequestMethod.POST, value = "/diff/{id}/left")
	public JsonTracking createOrUpdateLeft(@PathVariable(value = "id") String jsonId,
			@RequestParam("base64EncodedJson") String base64EncodedJson) {
		return JSONDiffService.createOrUpdate(jsonId, base64EncodedJson, Constants.LEFT_SIDE);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/diff/{id}/right")
	public JsonTracking createOrUpdateRight(@PathVariable(value = "id") String jsonId,
			@RequestParam("base64EncodedJson") String base64EncodedJson) {
		return JSONDiffService.createOrUpdate(jsonId, base64EncodedJson, Constants.RIGHT_SIDE);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/diff/{id}", produces = "application/json")
	public String provideInsights(@PathVariable(value = "id") String jsonId) {
		JsonTracking jsonTrackingLeft = JSONDiffService.findByJsonIdAndSide(jsonId, Constants.LEFT_SIDE);
		JsonTracking jsonTrackingRight = JSONDiffService.findByJsonIdAndSide(jsonId, Constants.RIGHT_SIDE);
		if (jsonTrackingLeft != null && jsonTrackingRight != null) {
			return JSONDiffService
					.provideInsights(jsonTrackingLeft.getBase64EncodedJson(), jsonTrackingRight.getBase64EncodedJson())
					.toString();
		}
		return Json.createObjectBuilder().add("error", "No data found!").build().toString();
	}

	/**
	 * Added new functionally to get All saved records
	 * 
	 * @return List<JsonTracking>
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/diff")
	public List<JsonTracking> findAll() {
		return JSONDiffService.findAll();
	}
}
