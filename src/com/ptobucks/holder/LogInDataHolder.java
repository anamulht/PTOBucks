package com.ptobucks.holder;

import com.ptobucks.model.LogInResponse;

public class LogInDataHolder {
	
	public static LogInResponse logInResponse = new LogInResponse();
	
	public static LogInResponse getLogInData(){
		return logInResponse;
	}
	
	public static void setLogInData(LogInResponse logInResponse){
		LogInDataHolder.logInResponse = logInResponse;
	}
	
	public static void removeLogInData(){
		LogInDataHolder.logInResponse = null;
	}

}
