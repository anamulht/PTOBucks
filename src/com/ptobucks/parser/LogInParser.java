package com.ptobucks.parser;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.ptobucks.holder.LogInDataHolder;
import com.ptobucks.holder.PromotionHolder;
import com.ptobucks.model.Company;
import com.ptobucks.model.LogInResponse;
import com.ptobucks.model.Promotion;
import com.ptobucks.model.User;

import android.content.Context;

public class LogInParser {

	public static boolean connect(Context context, String result, boolean isSync, String tokenParam) throws JSONException, IOException {

		LogInDataHolder.removeLogInData();
		PromotionHolder.removePromotionList();
		// System.out.println("json string:" + result);

		if (result.length() < 1) {
			return false;
		}

		final JSONObject mainJsonObject = new JSONObject(result);

		if (mainJsonObject.has("data")) {

			JSONObject data = mainJsonObject.getJSONObject("data");
			
			String token;
			
			if(isSync) token = tokenParam;
			else token = data.getString("token");
			
			Promotion promotionModel;
			User userModel;
			Company companyModel;

			JSONObject user = data.getJSONObject("user");
			JSONObject company = data.getJSONObject("company");

			userModel = new User();
			userModel.setEmail(user.getString("email"));
			userModel.setFirst_name(user.getString("first_name"));
			userModel.setLast_name(user.getString("last_name"));
			userModel.setAddress(user.getString("address"));
			userModel.setPhone(user.getString("phone"));
			userModel.setType(user.getString("type"));
			userModel.setImage(user.getString("image"));

			companyModel = new Company();
			companyModel.setEmail(company.getString("email"));
			companyModel.setTitle(company.getString("title"));
			companyModel.setAddress(company.getString("address"));
			companyModel.setWebsite(company.getString("website"));
			companyModel.setPhone(company.getString("phone"));
			companyModel.setImage(company.getString("image"));

			JSONArray promotionArray = data.getJSONArray("promotions");

			for (int i = 0; i < promotionArray.length(); i++) {
				JSONObject promotionObject = promotionArray.getJSONObject(i);
				promotionModel = new Promotion();

				promotionModel.setId(promotionObject.getString("id"));
				promotionModel.setTitle(promotionObject.getString("title"));
				promotionModel.setDescription(promotionObject.getString("description"));
				promotionModel.setType_of_offer(promotionObject.getString("type_of_offer"));
				promotionModel.setPer_offer_amount(promotionObject.getString("per_offer_amount"));
				promotionModel.setRemaining_amount(promotionObject.getString("remaining_amount"));
				promotionModel.setAmount(promotionObject.getString("amount"));
				promotionModel.setImage(promotionObject.getString("image"));

				PromotionHolder.setPromotionList(promotionModel);
				promotionModel = null;
			}

			LogInResponse loginData = new LogInResponse();
			loginData.setToken(token);
			loginData.setUser(userModel);
			loginData.setCompany(companyModel);
			loginData.setPromotionList(PromotionHolder.getAllPromotionList());
			loginData.setTokenTime(System.currentTimeMillis());

			LogInDataHolder.setLogInData(loginData);

			return true;

		} else {
			return false;
		}

	}

}
