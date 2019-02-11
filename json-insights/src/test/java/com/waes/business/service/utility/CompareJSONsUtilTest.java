package com.waes.business.service.utility;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import org.junit.Test;

import com.waes.business.service.utility.CompareJSONsUtil;

public class CompareJSONsUtilTest {

	public static final String INPUT_PATH_TESTING_FILES = "src\\test\\files";

	/**
	 * Check an invalid Base64 JSON Encoding String
	 */
	@Test
	public void checkAnInvalidBase64JsonEncoding() {
		CompareJSONsUtil businessLogicTemp = new CompareJSONsUtil();
		// Plain text with strange content
		assertFalse(businessLogicTemp.isValidBase64EncodedJSON(
				"{A \"quoted\" segment; & (entity); wrapped in quotes 6565774 中文 Zhōngwén 漢字  "));
		try {
			// Plain JSON (no Base 64 encoded)
			String plainJson = new String(
					Files.readAllBytes(FileSystems.getDefault().getPath(INPUT_PATH_TESTING_FILES, "plainJson.txt")));
			assertFalse(businessLogicTemp.isValidBase64EncodedJSON(plainJson));

			// Plain JSONArray (no Base 64 encoded)
			String plainJsonArray = new String(Files
					.readAllBytes(FileSystems.getDefault().getPath(INPUT_PATH_TESTING_FILES, "plainJsonArray.txt")));
			assertFalse(businessLogicTemp.isValidBase64EncodedJSON(plainJsonArray));
		} catch (IOException e) {
			e.printStackTrace(); // this should dever happen
		}

		// Plain JSON Value with true content (no Base64 encoded)
		assertFalse(businessLogicTemp.isValidBase64EncodedJSON("true"));

		// Plain JSON Value with false content (no Base64 encoded)
		assertFalse(businessLogicTemp.isValidBase64EncodedJSON("false"));

		// Plain JSON Value with null content (no Base64 encoded)
		assertFalse(businessLogicTemp.isValidBase64EncodedJSON("null"));

		// Base 64 encoded string which is not a JSON structure
		assertFalse(businessLogicTemp.isValidBase64EncodedJSON(
				"TXkgc3VwZXIgdGV4dCB3aGljaCBpcyBiYXNlIDY0IGVuY29kZWQgYnV0IGlzIG5vdCBhIEpTT04gc3RydWN0dXJl"));

		// Empty String
		assertFalse(businessLogicTemp.isValidBase64EncodedJSON(""));

		// Null value
		assertFalse(businessLogicTemp.isValidBase64EncodedJSON(null));
	}

	/**
	 * Check a valid Base 64 Encoded JSON String
	 */
	@Test
	public void checkAValidBase64JsonEncoding() {
		CompareJSONsUtil businessLogicTemp = new CompareJSONsUtil();
		// Base 64 JSON String
		assertTrue(businessLogicTemp.isValidBase64EncodedJSON(
				"eyJ1c2VyVHlwZSI6ImFkbWluIiwiY3JlZGl0Q2FyZE51bWJlciI6MCwiZW1haWwiOiJlbWFpbEBnbWFpbC5jb20iLCJpZCI6MTIzfQ"));
		// Base 64 JSONArray String
		assertTrue(businessLogicTemp.isValidBase64EncodedJSON(
				"W3siaWQiOiIwMDAxIiwidHlwZSI6ImRvbnV0IiwibmFtZSI6IkNha2UiLCJwcHUiOjAuNTUsImJhdHRlcnMiOnsiYmF0dGVyIjpbeyJpZCI6IjEwMDEiLCJ0eXBlIjoiUmVndWxhciJ9LHsiaWQiOiIxMDAyIiwidHlwZSI6IkNob2NvbGF0ZSJ9LHsiaWQiOiIxMDAzIiwidHlwZSI6IkJsdWViZXJyeSJ9LHsiaWQiOiIxMDA0IiwidHlwZSI6IkRldmlsJ3NGb29kIn1dfSwidG9wcGluZyI6W3siaWQiOiI1MDAxIiwidHlwZSI6Ik5vbmUifSx7ImlkIjoiNTAwMiIsInR5cGUiOiJHbGF6ZWQifSx7ImlkIjoiNTAwNSIsInR5cGUiOiJTdWdhciJ9LHsiaWQiOiI1MDA3IiwidHlwZSI6IlBvd2RlcmVkU3VnYXIifSx7ImlkIjoiNTAwNiIsInR5cGUiOiJDaG9jb2xhdGV3aXRoU3ByaW5rbGVzIn0seyJpZCI6IjUwMDMiLCJ0eXBlIjoiQ2hvY29sYXRlIn0seyJpZCI6IjUwMDQiLCJ0eXBlIjoiTWFwbGUifV19LHsiaWQiOiIwMDAyIiwidHlwZSI6ImRvbnV0IiwibmFtZSI6IlJhaXNlZCIsInBwdSI6MC41NSwiYmF0dGVycyI6eyJiYXR0ZXIiOlt7ImlkIjoiMTAwMSIsInR5cGUiOiJSZWd1bGFyIn1dfSwidG9wcGluZyI6W3siaWQiOiI1MDAxIiwidHlwZSI6Ik5vbmUifSx7ImlkIjoiNTAwMiIsInR5cGUiOiJHbGF6ZWQifSx7ImlkIjoiNTAwNSIsInR5cGUiOiJTdWdhciJ9LHsiaWQiOiI1MDAzIiwidHlwZSI6IkNob2NvbGF0ZSJ9LHsiaWQiOiI1MDA0IiwidHlwZSI6Ik1hcGxlIn1dfSx7ImlkIjoiMDAwMyIsInR5cGUiOiJkb251dCIsIm5hbWUiOiJPbGRGYXNoaW9uZWQiLCJwcHUiOjAuNTUsImJhdHRlcnMiOnsiYmF0dGVyIjpbeyJpZCI6IjEwMDEiLCJ0eXBlIjoiUmVndWxhciJ9LHsiaWQiOiIxMDAyIiwidHlwZSI6IkNob2NvbGF0ZSJ9XX0sInRvcHBpbmciOlt7ImlkIjoiNTAwMSIsInR5cGUiOiJOb25lIn0seyJpZCI6IjUwMDIiLCJ0eXBlIjoiR2xhemVkIn0seyJpZCI6IjUwMDMiLCJ0eXBlIjoiQ2hvY29sYXRlIn0seyJpZCI6IjUwMDQiLCJ0eXBlIjoiTWFwbGUifV19XQ"));

		// Base 64 JSON Value with true content
		assertTrue(businessLogicTemp.isValidBase64EncodedJSON("dHJ1ZQ=="));

		// Base 64 JSON Value with false content
		assertTrue(businessLogicTemp.isValidBase64EncodedJSON("ZmFsc2U="));

		// Base 64 JSON Value with null content
		assertTrue(businessLogicTemp.isValidBase64EncodedJSON("bnVsbA=="));
	}

	/**
	 * Check the response by giving two non equal JSON Base 64 Encoded Strings and
	 * having non equal size
	 */
	@Test
	public void checkJSONResponseGivenTwoNonEqualJSONBase64HavingNonEqualSize() {
		try {
			String isEqualFalseIsEqualSizeFalseResult = new String(Files.readAllBytes(FileSystems.getDefault()
					.getPath(INPUT_PATH_TESTING_FILES, "isEqualFalseIsEqualSizeFalse_Json.txt")));
			CompareJSONsUtil businessLogicTemp = new CompareJSONsUtil();

			// Two non equal JSON Base64 encoded with different size
			assertEquals(isEqualFalseIsEqualSizeFalseResult,
					businessLogicTemp.compareJSONs("dHJ1ZQ==", "ZmFsc2U").toString());

			// Two non equal JSON Base64 encoded with different size
			assertEquals(isEqualFalseIsEqualSizeFalseResult, businessLogicTemp.compareJSONs(
					"eyJ1c2VyVHlwZSI6ImFkbWluIiwiY3JlZGl0Q2FyZE51bWJlciI6MCwiZW1haWwiOiJlbWFpbEBnbWFpbC5jb20iLCJpZCI6MTIzfQ",
					"W3siaWQiOiIwMDAxIiwidHlwZSI6ImRvbnV0IiwibmFtZSI6IkNha2UiLCJwcHUiOjAuNTUsImJhdHRlcnMiOnsiYmF0dGVyIjpbeyJpZCI6IjEwMDEiLCJ0eXBlIjoiUmVndWxhciJ9LHsiaWQiOiIxMDAyIiwidHlwZSI6IkNob2NvbGF0ZSJ9LHsiaWQiOiIxMDAzIiwidHlwZSI6IkJsdWViZXJyeSJ9LHsiaWQiOiIxMDA0IiwidHlwZSI6IkRldmlsJ3NGb29kIn1dfSwidG9wcGluZyI6W3siaWQiOiI1MDAxIiwidHlwZSI6Ik5vbmUifSx7ImlkIjoiNTAwMiIsInR5cGUiOiJHbGF6ZWQifSx7ImlkIjoiNTAwNSIsInR5cGUiOiJTdWdhciJ9LHsiaWQiOiI1MDA3IiwidHlwZSI6IlBvd2RlcmVkU3VnYXIifSx7ImlkIjoiNTAwNiIsInR5cGUiOiJDaG9jb2xhdGV3aXRoU3ByaW5rbGVzIn0seyJpZCI6IjUwMDMiLCJ0eXBlIjoiQ2hvY29sYXRlIn0seyJpZCI6IjUwMDQiLCJ0eXBlIjoiTWFwbGUifV19LHsiaWQiOiIwMDAyIiwidHlwZSI6ImRvbnV0IiwibmFtZSI6IlJhaXNlZCIsInBwdSI6MC41NSwiYmF0dGVycyI6eyJiYXR0ZXIiOlt7ImlkIjoiMTAwMSIsInR5cGUiOiJSZWd1bGFyIn1dfSwidG9wcGluZyI6W3siaWQiOiI1MDAxIiwidHlwZSI6Ik5vbmUifSx7ImlkIjoiNTAwMiIsInR5cGUiOiJHbGF6ZWQifSx7ImlkIjoiNTAwNSIsInR5cGUiOiJTdWdhciJ9LHsiaWQiOiI1MDAzIiwidHlwZSI6IkNob2NvbGF0ZSJ9LHsiaWQiOiI1MDA0IiwidHlwZSI6Ik1hcGxlIn1dfSx7ImlkIjoiMDAwMyIsInR5cGUiOiJkb251dCIsIm5hbWUiOiJPbGRGYXNoaW9uZWQiLCJwcHUiOjAuNTUsImJhdHRlcnMiOnsiYmF0dGVyIjpbeyJpZCI6IjEwMDEiLCJ0eXBlIjoiUmVndWxhciJ9LHsiaWQiOiIxMDAyIiwidHlwZSI6IkNob2NvbGF0ZSJ9XX0sInRvcHBpbmciOlt7ImlkIjoiNTAwMSIsInR5cGUiOiJOb25lIn0seyJpZCI6IjUwMDIiLCJ0eXBlIjoiR2xhemVkIn0seyJpZCI6IjUwMDMiLCJ0eXBlIjoiQ2hvY29sYXRlIn0seyJpZCI6IjUwMDQiLCJ0eXBlIjoiTWFwbGUifV19XQ")
					.toString());
		} catch (IOException e) {
			e.printStackTrace(); // this should never happen
		}
	}

	/**
	 * Check the JSON response by giving two non equal JSON Base 64 Strings having
	 * equal size
	 */
	@Test
	public void checkJSONResponseGivenTwoNonEqualJSONBase64HavingEqualSize() {
		try {
			String sample1ExpectedResult = new String(Files.readAllBytes(FileSystems.getDefault()
					.getPath(INPUT_PATH_TESTING_FILES, "isEqualFalseIsEqualSizeTrue_Json_Sample1.txt")));
			String sample2ExpectedResult = new String(Files.readAllBytes(FileSystems.getDefault()
					.getPath(INPUT_PATH_TESTING_FILES, "isEqualFalseIsEqualSizeTrue_Json_Sample2.txt")));
			CompareJSONsUtil businessLogicTemp = new CompareJSONsUtil();

			// Two non equal JSON Base64 encoded with same size (this should return the
			// diffs)
			assertEquals(sample1ExpectedResult,
					businessLogicTemp.compareJSONs("eyJ1c2VyVHlwZSI6ImFkbWluIiwiaWQiOjEyM30=",
							"eyJ1c2VyVHlwZSI6InVzZXIiLCJpZCI6MTIzfQ==").toString());

			// Two non equal JSON Base64 encoded with same size (this should return the
			// diffs)
			assertEquals(sample2ExpectedResult, businessLogicTemp.compareJSONs(
					"eyJ1c2VyVHlwZSI6ImFkbWluIiwiY3JlZGl0Q2FyZE51bWJlciI6NDY0NywiZW1haWwiOiJlbWFpbEBnbWFpbC5jb20iLCJpZCI6MTIzfQ==",
					"eyJ1c2VyVHlwZSI6ImFkbWluIiwiY3JlZGl0Q2FyZE51bWJlciI6NDQ0MywiZW1haWwiOiJlbWFpbEBnbWFpbC5jb20iLCJpZCI6MTIzfQ==")
					.toString());
		} catch (IOException e) {
			e.printStackTrace(); // this should never happen
		}
	}

	/**
	 * Check JSON response by giving two non JSON Base 64 Encoded Strings
	 */
	@Test
	public void checkJSONResponseGivenTwoNonJSONBase64Encoded() {
		try {
			String isEqualFalseIsBase64FalseResult = new String(Files.readAllBytes(
					FileSystems.getDefault().getPath(INPUT_PATH_TESTING_FILES, "isEqualFalseIsBase64False_Json.txt")));
			CompareJSONsUtil businessLogicTemp = new CompareJSONsUtil();

			// Two non equal non Base64 Strings
			assertEquals(isEqualFalseIsBase64FalseResult, businessLogicTemp
					.compareJSONs("khjgkjgkjhgkjashhshshsah=", "uuwy736363ywywywwyyyw88iui==").toString());

			// Two non equal non Base64 Strings
			assertEquals(isEqualFalseIsBase64FalseResult,
					businessLogicTemp.compareJSONs(
							"{\"userType\":\"admin\",\"creditCardNumber\":0,\"email\":\"email@gmail.com\",\"id\":123}",
							"{\"userType\":\"admin\",\"creditCardNumber\":0,\"email\":\"email@waes.com\",\"id\":123}")
							.toString());
		} catch (IOException e) {
			e.printStackTrace(); // this should never happen
		}
	}

	/**
	 * Check JSON response by giving two JSON Base 64 Encoded Strings wich are equal
	 */
	@Test
	public void checkJSONResponseGivenTwoJSONBase64AreEqual() {
		try {
			String expectedIsEqualResult = new String(Files
					.readAllBytes(FileSystems.getDefault().getPath(INPUT_PATH_TESTING_FILES, "isEqualTrueJson.txt")));
			CompareJSONsUtil businessLogicTemp = new CompareJSONsUtil();

			// Same JSON Value encoded in Base64
			assertEquals(expectedIsEqualResult, businessLogicTemp.compareJSONs("bnVsbA==", "bnVsbA==").toString());

			// Same JSON Array encoded in Base64
			assertEquals(expectedIsEqualResult, businessLogicTemp.compareJSONs(
					"W3siaWQiOiIwMDAxIiwidHlwZSI6ImRvbnV0IiwibmFtZSI6IkNha2UiLCJwcHUiOjAuNTUsImJhdHRlcnMiOnsiYmF0dGVyIjpbeyJpZCI6IjEwMDEiLCJ0eXBlIjoiUmVndWxhciJ9LHsiaWQiOiIxMDAyIiwidHlwZSI6IkNob2NvbGF0ZSJ9LHsiaWQiOiIxMDAzIiwidHlwZSI6IkJsdWViZXJyeSJ9LHsiaWQiOiIxMDA0IiwidHlwZSI6IkRldmlsJ3NGb29kIn1dfSwidG9wcGluZyI6W3siaWQiOiI1MDAxIiwidHlwZSI6Ik5vbmUifSx7ImlkIjoiNTAwMiIsInR5cGUiOiJHbGF6ZWQifSx7ImlkIjoiNTAwNSIsInR5cGUiOiJTdWdhciJ9LHsiaWQiOiI1MDA3IiwidHlwZSI6IlBvd2RlcmVkU3VnYXIifSx7ImlkIjoiNTAwNiIsInR5cGUiOiJDaG9jb2xhdGV3aXRoU3ByaW5rbGVzIn0seyJpZCI6IjUwMDMiLCJ0eXBlIjoiQ2hvY29sYXRlIn0seyJpZCI6IjUwMDQiLCJ0eXBlIjoiTWFwbGUifV19LHsiaWQiOiIwMDAyIiwidHlwZSI6ImRvbnV0IiwibmFtZSI6IlJhaXNlZCIsInBwdSI6MC41NSwiYmF0dGVycyI6eyJiYXR0ZXIiOlt7ImlkIjoiMTAwMSIsInR5cGUiOiJSZWd1bGFyIn1dfSwidG9wcGluZyI6W3siaWQiOiI1MDAxIiwidHlwZSI6Ik5vbmUifSx7ImlkIjoiNTAwMiIsInR5cGUiOiJHbGF6ZWQifSx7ImlkIjoiNTAwNSIsInR5cGUiOiJTdWdhciJ9LHsiaWQiOiI1MDAzIiwidHlwZSI6IkNob2NvbGF0ZSJ9LHsiaWQiOiI1MDA0IiwidHlwZSI6Ik1hcGxlIn1dfSx7ImlkIjoiMDAwMyIsInR5cGUiOiJkb251dCIsIm5hbWUiOiJPbGRGYXNoaW9uZWQiLCJwcHUiOjAuNTUsImJhdHRlcnMiOnsiYmF0dGVyIjpbeyJpZCI6IjEwMDEiLCJ0eXBlIjoiUmVndWxhciJ9LHsiaWQiOiIxMDAyIiwidHlwZSI6IkNob2NvbGF0ZSJ9XX0sInRvcHBpbmciOlt7ImlkIjoiNTAwMSIsInR5cGUiOiJOb25lIn0seyJpZCI6IjUwMDIiLCJ0eXBlIjoiR2xhemVkIn0seyJpZCI6IjUwMDMiLCJ0eXBlIjoiQ2hvY29sYXRlIn0seyJpZCI6IjUwMDQiLCJ0eXBlIjoiTWFwbGUifV19XQ",
					"W3siaWQiOiIwMDAxIiwidHlwZSI6ImRvbnV0IiwibmFtZSI6IkNha2UiLCJwcHUiOjAuNTUsImJhdHRlcnMiOnsiYmF0dGVyIjpbeyJpZCI6IjEwMDEiLCJ0eXBlIjoiUmVndWxhciJ9LHsiaWQiOiIxMDAyIiwidHlwZSI6IkNob2NvbGF0ZSJ9LHsiaWQiOiIxMDAzIiwidHlwZSI6IkJsdWViZXJyeSJ9LHsiaWQiOiIxMDA0IiwidHlwZSI6IkRldmlsJ3NGb29kIn1dfSwidG9wcGluZyI6W3siaWQiOiI1MDAxIiwidHlwZSI6Ik5vbmUifSx7ImlkIjoiNTAwMiIsInR5cGUiOiJHbGF6ZWQifSx7ImlkIjoiNTAwNSIsInR5cGUiOiJTdWdhciJ9LHsiaWQiOiI1MDA3IiwidHlwZSI6IlBvd2RlcmVkU3VnYXIifSx7ImlkIjoiNTAwNiIsInR5cGUiOiJDaG9jb2xhdGV3aXRoU3ByaW5rbGVzIn0seyJpZCI6IjUwMDMiLCJ0eXBlIjoiQ2hvY29sYXRlIn0seyJpZCI6IjUwMDQiLCJ0eXBlIjoiTWFwbGUifV19LHsiaWQiOiIwMDAyIiwidHlwZSI6ImRvbnV0IiwibmFtZSI6IlJhaXNlZCIsInBwdSI6MC41NSwiYmF0dGVycyI6eyJiYXR0ZXIiOlt7ImlkIjoiMTAwMSIsInR5cGUiOiJSZWd1bGFyIn1dfSwidG9wcGluZyI6W3siaWQiOiI1MDAxIiwidHlwZSI6Ik5vbmUifSx7ImlkIjoiNTAwMiIsInR5cGUiOiJHbGF6ZWQifSx7ImlkIjoiNTAwNSIsInR5cGUiOiJTdWdhciJ9LHsiaWQiOiI1MDAzIiwidHlwZSI6IkNob2NvbGF0ZSJ9LHsiaWQiOiI1MDA0IiwidHlwZSI6Ik1hcGxlIn1dfSx7ImlkIjoiMDAwMyIsInR5cGUiOiJkb251dCIsIm5hbWUiOiJPbGRGYXNoaW9uZWQiLCJwcHUiOjAuNTUsImJhdHRlcnMiOnsiYmF0dGVyIjpbeyJpZCI6IjEwMDEiLCJ0eXBlIjoiUmVndWxhciJ9LHsiaWQiOiIxMDAyIiwidHlwZSI6IkNob2NvbGF0ZSJ9XX0sInRvcHBpbmciOlt7ImlkIjoiNTAwMSIsInR5cGUiOiJOb25lIn0seyJpZCI6IjUwMDIiLCJ0eXBlIjoiR2xhemVkIn0seyJpZCI6IjUwMDMiLCJ0eXBlIjoiQ2hvY29sYXRlIn0seyJpZCI6IjUwMDQiLCJ0eXBlIjoiTWFwbGUifV19XQ")
					.toString());
		} catch (IOException e) {
			e.printStackTrace(); // this should never happen
		}
	}

}
