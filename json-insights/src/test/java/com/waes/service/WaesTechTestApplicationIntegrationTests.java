package com.waes.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.test.TestRestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.WaesTechTestApplication;
import com.waes.business.service.utility.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WaesTechTestApplication.class)
@WebIntegrationTest("server.port=0")
public class WaesTechTestApplicationIntegrationTests {

	private final RestTemplate restTemplate = new TestRestTemplate();
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private HttpHeaders headers = new HttpHeaders();
	@Value("${local.server.port}")
	private int port;

	/**
	 * Test the POST /v1/diff/{id}/left API
	 *
	 */
	@Test
	public void testAddJSONs() {
		// 1. make a POST request to insert a new JSON Base 64 Encoded String in left
		String jsonId = "1";
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("base64EncodedJson",
				"eyJ1c2VyVHlwZSI6ImFkbWluIiwiY3JlZGl0Q2FyZE51bWJlciI6MCwiZW1haWwiOiJlbWFpbEBnbWFpbC5jb20iLCJpZCI6MTIzfQ");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		String url = "http://localhost:" + port + "/v1/diff/" + jsonId + "/" + Constants.LEFT_SIDE;
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		String responses = response.getBody();

		assertNotNull(responses);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(responses.startsWith(
				"{\"id\":1,\"jsonId\":\"1\",\"base64EncodedJson\":\"eyJ1c2VyVHlwZSI6ImFkbWluIiwiY3JlZGl0Q2FyZE51bWJlciI6MCwiZW1haWwiOiJlbWFpbEBnbWFpbC5jb20iLCJpZCI6MTIzfQ\",\"side\":\"left\""));

		// 2. make a POST request to insert a new JSON Base 64 Encoded String in right
		map = new LinkedMultiValueMap<String, String>();
		map.add("base64EncodedJson",
				"eyJ1c2VyVHlwZSI6ImFkbWluIiwiY3JlZGl0Q2FyZE51bWJlciI6MCwiZW1haWwiOiJlbWFpbEBnbWFpbC5jb20iLCJpZCI6MTIzfQ");
		request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		url = "http://localhost:" + port + "/v1/diff/" + jsonId + "/" + Constants.RIGHT_SIDE;
		response = restTemplate.postForEntity(url, request, String.class);
		responses = response.getBody();

		assertNotNull(responses);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(responses.startsWith(
				"{\"id\":2,\"jsonId\":\"1\",\"base64EncodedJson\":\"eyJ1c2VyVHlwZSI6ImFkbWluIiwiY3JlZGl0Q2FyZE51bWJlciI6MCwiZW1haWwiOiJlbWFpbEBnbWFpbC5jb20iLCJpZCI6MTIzfQ\",\"side\":\"right\""));
	}

	/**
	 * Test the GET /v1/diff/{id} API
	 */
	@Test
	public void testCompareJSONs() {
		// 1. make a POST request to insert a new JSON Base 64 Encoded String in left
		String jsonId = "1";
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("base64EncodedJson",
				"eyJ1c2VyVHlwZSI6ImFkbWluIiwiY3JlZGl0Q2FyZE51bWJlciI6MCwiZW1haWwiOiJlbWFpbEBnbWFpbC5jb20iLCJpZCI6MTIzfQ");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		String url = "http://localhost:" + port + "/v1/diff/" + jsonId + "/" + Constants.LEFT_SIDE;
		restTemplate.postForEntity(url, request, String.class);

		// 2. make a POST request to insert a new JSON Base 64 Encoded String in right
		map = new LinkedMultiValueMap<String, String>();
		map.add("base64EncodedJson",
				"eyJ1c2VyVHlwZSI6ImFkbWluIiwiY3JlZGl0Q2FyZE51bWJlciI6MCwiZW1haWwiOiJlbWFpbEBnbWFpbC5jb20iLCJpZCI6MTIzfQ");
		request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		url = "http://localhost:" + port + "/v1/diff/" + jsonId + "/" + Constants.RIGHT_SIDE;
		restTemplate.postForEntity(url, request, String.class);

		// 3. Call third end point and compare JSONs
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("id", "1");
		ResponseEntity<String> getResponse = restTemplate.exchange("http://localhost:" + port + "/v1/diff/{id}",
				HttpMethod.GET, entity, String.class, uriVariables);
		assertNotNull(getResponse);
		assertEquals(HttpStatus.OK, getResponse.getStatusCode());
		String getResponses = getResponse.getBody();
		assertNotNull(getResponses);
		assertTrue(getResponses.equals("{\"isEqual\":true}"));
	}

	/**
	 * Test the GET all /v1/diff/ API
	 */
	@Test
	public void testGetAllJSONs() {
		// 1. make a POST request to insert a new JSON Base 64 Encoded String in left
		String jsonId = "1";
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("base64EncodedJson",
				"eyJ1c2VyVHlwZSI6ImFkbWluIiwiY3JlZGl0Q2FyZE51bWJlciI6MCwiZW1haWwiOiJlbWFpbEBnbWFpbC5jb20iLCJpZCI6MTIzfQ");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		String url = "http://localhost:" + port + "/v1/diff/" + jsonId + "/" + Constants.LEFT_SIDE;
		restTemplate.postForEntity(url, request, String.class);

		// 2. make a POST request to insert a new JSON Base 64 Encoded String in right
		map = new LinkedMultiValueMap<String, String>();
		map.add("base64EncodedJson",
				"eyJ1c2VyVHlwZSI6ImFkbWluIiwiY3JlZGl0Q2FyZE51bWJlciI6MCwiZW1haWwiOiJlbWFpbEBnbWFpbC5jb20iLCJpZCI6MTIzfQ");
		request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		url = "http://localhost:" + port + "/v1/diff/" + jsonId + "/" + Constants.RIGHT_SIDE;
		restTemplate.postForEntity(url, request, String.class);

		// 3. Call third end point and compare JSONs
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("id", "1");
		restTemplate.exchange("http://localhost:" + port + "/v1/diff/{id}", HttpMethod.GET, entity, String.class,
				uriVariables);

		// 4. Call extended API to print all records available in database
		ResponseEntity<String> getResponse = restTemplate.exchange("http://localhost:" + port + "/v1/diff/",
				HttpMethod.GET, entity, String.class);
		assertNotNull(getResponse);
		assertEquals(HttpStatus.OK, getResponse.getStatusCode());
		String getResponses = getResponse.getBody();
		assertNotNull(getResponses);
		assertTrue(getResponses.startsWith(
				("[{\"id\":1,\"jsonId\":\"1\",\"base64EncodedJson\":\"eyJ1c2VyVHlwZSI6ImFkbWluIiwiY3JlZGl0Q2FyZE51bWJlciI6MCwiZW1haWwiOiJlbWFpbEBnbWFpbC5jb20iLCJpZCI6MTIzfQ\",\"side\":\"left\",\"createdDate\"")));
	}
}
