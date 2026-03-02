package com.example.demo.enumValue;

public enum EnumData {

	ACTIVE("A"),
	NONACTIVE("N");

	private final String value;

	EnumData(String value) {         //constructor
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
