package com.hortonworks.utils;

public class Util {
	public static String normalizeEmail(String email) {
		return email.replaceAll("[<,>]", "");
	}
}
