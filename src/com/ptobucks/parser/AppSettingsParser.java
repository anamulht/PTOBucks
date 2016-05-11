package com.ptobucks.parser;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import com.ptobucks.holder.SettingsDataHolder;
import com.ptobucks.model.AppSettings;

import android.content.Context;

public class AppSettingsParser {

	/*
	 * { "responseData": { "status": "success", "data": { "invoice_no": "1",
	 * "invoice_image": "1" } } }
	 */
	public static boolean connect(Context context, String result) throws JSONException, IOException {

		if (result.length() < 1) {
			return false;
		}

		System.out.println("result json: " + result);

		final JSONObject mainJsonObject = new JSONObject(result);

		JSONObject responseData = mainJsonObject.getJSONObject("responseData");

		JSONObject data = responseData.getJSONObject("data");

		String status = responseData.getString("status");

		if (status.equalsIgnoreCase("success")) {

			AppSettings sett = new AppSettings();

			String invoice_no = data.getString("invoice_no");
			String invoice_image = data.getString("invoice_image");

			if (invoice_no.equals("1"))
				sett.setReceiptNo(true);
			else
				sett.setReceiptNo(false);

			if (invoice_image.equals("1"))
				sett.setReceiptImage(true);
			else
				sett.setReceiptImage(false);
			
			SettingsDataHolder.setSettings(sett);

			return true;

		} else {

			return false;
		}
	}
}
