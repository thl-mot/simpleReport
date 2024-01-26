package com.lauerbach.pdf;

import java.util.Arrays;

public enum TextFormat {
	left, right, center, block;
	public static TextFormat fromName(String text) {
		return Arrays.stream(values()).filter(bl -> bl.name().equalsIgnoreCase(text)).findFirst().get();
	}
}
