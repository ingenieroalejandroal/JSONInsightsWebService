package com.waes.business.service.utility;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

import name.fraser.neil.plaintext.diff_match_patch;

/**
 * The program CompareJSONsUtil has the business logic to receive two BASE64
 * encoded JSONs (jsonLeft and jsonRight) in the method compareJSONs and and
 * produces a JSON response according to the following logic:
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
 * @author Alejandro Aguirre
 * @version 1.0
 * @since 2019/02/10
 *
 */
public class CompareJSONsUtil {

	/**
	 * Method that compares the two JSON structures
	 * 
	 * @param jsonLeft
	 * @param jsonRight
	 * @return JsonObject
	 */
	public JsonObject compareJSONs(String jsonLeft, String jsonRight) {
		JsonObject jsonObject = null;
		if (isValidBase64EncodedJSON(jsonLeft) && isValidBase64EncodedJSON(jsonRight)) {
			if (jsonLeft.equals(jsonRight)) {
				// left and right are equals
				jsonObject = Json.createObjectBuilder().add("isEqual", Boolean.TRUE).build();
			} else if (jsonLeft.length() == jsonRight.length()) {
				// left and right are different but the size is the same
				jsonObject = Json.createObjectBuilder().add("isEqual", Boolean.FALSE).add("isEqualSize", Boolean.TRUE)
						.add("diffs",
								String.valueOf(getDiffs(fromBase64ToString(jsonLeft), fromBase64ToString(jsonRight))))
						.build();
			} else {
				// left and right are different and the size is also different
				jsonObject = Json.createObjectBuilder().add("isEqual", Boolean.FALSE).add("isEqualSize", Boolean.FALSE)
						.build();
			}
		} else {
			// Any or both of the JSONs are not encoded in Base64
			jsonObject = Json.createObjectBuilder().add("isEqual", Boolean.FALSE).add("isBase64", Boolean.FALSE)
					.build();
		}
		return jsonObject;
	}

	/**
	 * Method that make uses of diff_match_patch-current .jar to find the difference
	 * between two given Strings. The LinkedList returned object is a key value
	 * representation of what is EQUAL, what should be DELETE, and also what should
	 * be INSERT to make the two strings them the same.
	 * 
	 * @param str1
	 * @param str2
	 * @return LinkedList<diff_match_patch.Diff>
	 */
	public LinkedList<diff_match_patch.Diff> getDiffs(String str1, String str2) {
		diff_match_patch dmp = new diff_match_patch(null);
		return dmp.diff_main(str1, str2);
	}

	/**
	 * Validate if the given String is a Base64 JSON structure
	 * 
	 * @param jsonBase64
	 * @return boolean
	 */
	public boolean isValidBase64EncodedJSON(String jsonBase64) {
		try {
			String decodedJson = fromBase64ToString(jsonBase64);
			return isAValidJSONStructure(decodedJson);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method that converts from Base64 String to Normal String
	 * 
	 * @param jsonBase64
	 * @return String
	 */
	public String fromBase64ToString(String strBase64) {
		byte[] base64decodedBytes = Base64.getDecoder().decode(strBase64);
		return new String(base64decodedBytes, StandardCharsets.UTF_8);
	}

	/**
	 * Validate if the given String is a JSON
	 * 
	 * @param json
	 * @return
	 */
	public static boolean isAValidJSONStructure(String json) {
		boolean isAValidJSON = false;
		isAValidJSON = isAValidJSONObject(json);
		if (!isAValidJSON) {
			isAValidJSON = isAValidJSONArray(json);
			if (!isAValidJSON) {
				isAValidJSON = isAValidJSONValue(json);
			}
		}
		return isAValidJSON;
	}

	/**
	 * Validate if the JSON is a JSON Object
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static boolean isAValidJSONObject(String jsonObject) {
		try {
			JsonReader jsonReader = Json.createReader(new StringReader(jsonObject));
			jsonReader.readObject();
			return true;
		} catch (JsonParsingException jpe) {
			return false;
		}
	}

	/**
	 * Validate if the JSON is a JSON Array
	 * 
	 * @param jsonArray
	 * @return boolean
	 */
	public static boolean isAValidJSONArray(String jsonArray) {
		try {
			JsonReader jsonReader = Json.createReader(new StringReader(jsonArray));
			jsonReader.readArray();
			return true;
		} catch (JsonParsingException jpe) {
			return false;
		}
	}

	/**
	 * Validate if the JSON is a JSON Value like: null, true or false
	 * 
	 * @param jsonValue
	 * @return boolean
	 */
	public static boolean isAValidJSONValue(String jsonValue) {
		try {
			JsonReader jsonReader = Json.createReader(new StringReader(jsonValue));
			jsonReader.readValue();
			return true;
		} catch (JsonParsingException jpe) {
			return false;
		}
	}
}
