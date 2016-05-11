package com.ptobucks.parser;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class EmployeeBlockUnblockParser {
	
	public static boolean connect(Context context, String result) throws JSONException, IOException {

		if (result.length() < 1) {
			return false;
		}
		
		System.out.println("result json: " + result);

		final JSONObject mainJsonObject = new JSONObject(result);
		
		JSONObject data = mainJsonObject.getJSONObject("data");

		String status = data.getString("status");
		
		if (status.equalsIgnoreCase("success")) {

			return true;
			
		} else {

			return false;
		}
	}

}
