package com.shen.utils;

public class GlassLanguageInfo {
	
	public String language_name;
	public String language_country;
	public String language_codes;

	public GlassLanguageInfo() {
		super();
	}

	public String getLanguage_name() {
		return language_name;
	}

	public void setLanguage_name(String language_name) {
		this.language_name = language_name;
	}

	public String getLanguage_country() {
		return language_country;
	}

	public void setLanguage_country(String language_country) {
		this.language_country = language_country;
	}

	public String getLanguage_codes() {
		return language_codes;
	}

	public void setLanguage_codes(String language_codes) {
		this.language_codes = language_codes;
	}

	@Override
	public String toString() {
		return "GlassLanguageInfo [language_name=" + language_name + ", language_country=" + language_country
				+ ", language_codes=" + language_codes + "]";
	}
}
