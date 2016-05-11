package com.ptobucks.holder;

import com.ptobucks.model.AppSettings;

public class SettingsDataHolder {

	public static AppSettings settings = new AppSettings();

	public static AppSettings getSettings() {
		return settings;
	}

	public static void setSettings(AppSettings settings) {
		SettingsDataHolder.settings = settings;
	}

	public static void removeSettingsData() {
		SettingsDataHolder.settings = null;
	}

}
