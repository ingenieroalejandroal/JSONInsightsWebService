package com.waes.business.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.waes.business.service.utility.CompareJSONsUtil;
import com.waes.data.entity.JsonTracking;
import com.waes.data.repository.JsonTrackingRepository;

import com.waes.business.service.utility.Constants;

/**
 * Service class JSONDiffService marked @Service annotation with an instance
 * variable from our repository jsonTrackingRepository to serve the different
 * CRUD operations
 * 
 * @author Alejandro Aguirre
 * @version 1.0
 * @since 2019/02/10
 */
@Service
public class JSONDiffService {
	private JsonTrackingRepository jsonTrackingRepository;

	@Autowired
	public JSONDiffService(JsonTrackingRepository jsonTrackingRepository) {
		this.jsonTrackingRepository = jsonTrackingRepository;
	}

	/**
	 * This method should return a JsonTracking Object if found by jsonId and side
	 * 
	 * @param jsonId
	 * @param side
	 * @return JsonTracking
	 */
	public JsonTracking findByJsonIdAndSide(String jsonId, String side) {
		return jsonTrackingRepository.findByJsonIdAndSide(jsonId, side);
	}

	/**
	 * Get all objects JsonTracking from database
	 * 
	 * @return List<JsonTracking>
	 */
	public List<JsonTracking> findAll() {
		return (List<JsonTracking>) jsonTrackingRepository.findAll();
	}

	/**
	 * Method to update or create a new JsonTracking. If the Object already exists
	 * it will update base64EncodedJson, side and updatedDate attributes. The
	 * attributes base64EncodedJson and jsonId will persist only if its length is
	 * less than 255 chars and 64 chars respectively.
	 * 
	 * @param jsonId
	 * @param base64EncodedJson
	 * @param side
	 * @return JsonTracking
	 */
	public JsonTracking createOrUpdate(String jsonId, String base64EncodedJson, String side) {
		JsonTracking jsonTracking = jsonTrackingRepository.findByJsonIdAndSide(jsonId, side);
		if (jsonTracking != null) {
			if (base64EncodedJson.length() < Constants.BASE64_ENCODED_JSON_MAX_LENGTH)
				jsonTracking.setBase64EncodedJson(base64EncodedJson);
			jsonTracking.setSide((side != null && Arrays.asList(Constants.SIDES).contains(side) ? side.toLowerCase()
					: Constants.LEFT_SIDE));
			jsonTracking.setUpdatedDate(LocalDate.now());
		} else {
			jsonTracking = new JsonTracking();
			if (base64EncodedJson.length() < Constants.BASE64_ENCODED_JSON_MAX_LENGTH)
				jsonTracking.setBase64EncodedJson(base64EncodedJson);
			jsonTracking.setCreatedDate(LocalDate.now());
			if (jsonId.length() < Constants.JSON_ID_MAX_LENGTH)
				jsonTracking.setJsonId(jsonId);
			jsonTracking.setSide((side != null && Arrays.asList(Constants.SIDES).contains(side) ? side.toLowerCase()
					: Constants.LEFT_SIDE));
		}
		jsonTrackingRepository.save(jsonTracking);
		return jsonTracking;
	}

	/**
	 * The program CompareJSONsUtil has the business logic to receive two BASE64
	 * encoded JSONs (jsonLeft and jsonRight) in the method compareJSONs and and
	 * produces a JSON response according to the following logic:
	 * 
	 * SAMPLE: 
	 * - jsonLeft (BASE64 Encoded) = eyJ1c2VyVHlwZSI6ImFkbWluIiwiaWQiOjEyM30= 
	 * - jsonRight (BASE64 Encoded) = eyJ1c2VyVHlwZSI6InVzZXIiLCJpZCI6MTIzfQ==
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
	 * @param base64EncodedJsonLeft
	 * @param base64EncodedJsonRight
	 * @return
	 */
	public JsonObject provideInsights(String base64EncodedJsonLeft, String base64EncodedJsonRight) {
		CompareJSONsUtil compareJSONsUtil = new CompareJSONsUtil();
		return compareJSONsUtil.compareJSONs(base64EncodedJsonLeft, base64EncodedJsonRight);
	}
}
