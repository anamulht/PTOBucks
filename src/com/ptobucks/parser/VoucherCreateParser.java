package com.ptobucks.parser;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import com.ptobucks.holder.PromotionHolder;
import com.ptobucks.holder.VoucherHolder;
import com.ptobucks.model.Voucher;
import com.ptobucks.utils.Utils;

import android.content.Context;

public class VoucherCreateParser {

	public static boolean connect(Context context, String result) throws JSONException, IOException {

		VoucherHolder.removeVoucherData();

		if (result.length() < 1) {
			return false;
		}
		
		/*if (result.equalsIgnoreCase("{"\\error"\\: "\\token_expired"\\}")) {
			return false;
		}*/

		System.out.println("Voucher JSON: " + result);
		
		final JSONObject mainJsonObject = new JSONObject(result);
		
		/*if(mainJsonObject.has("error") && mainJsonObject.getString("error").equalsIgnoreCase("token_expired")){
			Utils.setTokenExpired(true);
			return false;
		}*/

		String status = mainJsonObject.getString("status");
		Voucher voucher;

		if (status.equalsIgnoreCase("success")) {

			JSONObject voucherJsonObject = mainJsonObject.getJSONObject("voucher");
			JSONObject promotionJsonObject = mainJsonObject.getJSONObject("promotion");

			voucher = new Voucher();

			voucher.setId(voucherJsonObject.getString("id").toString());
			voucher.setUrl(voucherJsonObject.getString("url").toString());
			voucher.setPromotion(PromotionHolder.findPromotionWithId(promotionJsonObject.getString("id").toString()));

			VoucherHolder.setVoucher(voucher);

			return true;
		} else {

			return false;
		}
	}

}
