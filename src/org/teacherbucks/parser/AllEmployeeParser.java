package org.teacherbucks.parser;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.teacherbucks.holder.EmployeeHolder;
import org.teacherbucks.model.Employee;

import android.content.Context;

public class AllEmployeeParser {
	
	public static boolean connect(Context context, String result) throws JSONException, IOException {
		
		EmployeeHolder.removeEmployeeList();
		
		if (result.length() < 1) {
			return false;
		}
		System.out.println("employee json: " + result);
		final JSONArray employeeJsonArray = new JSONArray(result);
		Employee empModel;
		
		for (int i = 0; i < employeeJsonArray.length(); i++){
			JSONObject empJsonObj = employeeJsonArray.getJSONObject(i);
			empModel = new Employee();
			
			empModel.setId(empJsonObj.getString("id"));
			empModel.setName(empJsonObj.getString("name"));
			empModel.setEmail(empJsonObj.getString("email"));
			empModel.setPhone(empJsonObj.getString("phone"));
			empModel.setImage(empJsonObj.getString("image"));
			empModel.setStatus(empJsonObj.getInt("active"));
			
			EmployeeHolder.setEmployeeList(empModel);
			empModel = null;
		}
		
		return false;
	}

}
