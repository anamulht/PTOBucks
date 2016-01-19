package org.teacherbucks.parser;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class VoucherDeliveryParser {
	
	public static boolean connect(Context context, String result) throws JSONException, IOException {

		if (result.length() < 1) {
			return false;
		}

		final JSONObject mainJsonObject = new JSONObject(result);

		String status = mainJsonObject.getString("status");
		
		if (status.equalsIgnoreCase("success")) {

			return true;
			
		} else {

			return false;
		}
	}

}
