package org.teacherbucks.holder;

import org.teacherbucks.model.LogInResponse;

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
