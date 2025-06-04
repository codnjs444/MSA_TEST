package com.example.userService.global.utils;

import java.util.regex.Pattern;

public class RegexUtils {

	/**
	 * 패스워드 정규식 (8자리 이상 20자리 이하 영문+숫자+특수문자)
	 * 허용 특수문자 : ! @ # $ % ^ & * ( ) _ +
	 * @param password
	 * @return
	 */
	public static boolean isPassword(String password) {
		boolean isValid = Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[a-zA-Z\\d!@#$%^&*()_+]{8,20}$", password);
		return isValid;
	}

	/**
	 * 이메일 정규식
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String regex = "^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9-]+(\\.[a-zA-Z]+){1,2}$";
		boolean isValid = Pattern.matches(regex, email);		
		return isValid;
	}

	/**
	 * 유저 아이디 조건 (영문 + 숫자 3~16자리)
	 * 허용 특수문자 : _
	 * @param userId
	 * @return
	 */
	public static boolean isUserId(String userId) {
		boolean isValid = Pattern.matches(
				"^[a-zA-Z0-9_]{3,16}$", userId);
		return isValid;
	}
	
	/**
	 * 휴대전화번호
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhone(String phoneNumber) {
		boolean isValid = Pattern.matches("^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", phoneNumber);
		return isValid;
	}
	
	/**
	 * 주민등록번호
	 * @param rrn
	 * @return
	 */
	public static boolean isRRN(String rrn) {
		boolean isValid = Pattern.matches("(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4][0-9]{6}$", rrn);
		return isValid;
	}
	
	/**
	 * IP 주소 (IPv4)
	 * @param ip
	 * @return
	 */
	public static boolean isIP(String ip) {
		boolean isValid = Pattern.matches("^([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})$", ip);
		return isValid;
	}
	
	/**
	 * 웹 URL
	 * @param url
	 * @return
	 */
	public static boolean isURL(String url) {
		boolean isValid = Pattern.matches("^(https?|ftp):\\/\\/[^\\s/$.?#].[^\\s]*$", url);
		return isValid;
	}
	
	/**
	 * 날짜 (YYYY-MM-DD)
	 * @param date
	 * @return
	 */
	public static boolean isDate_yyyy_mm_dd(String date) {
		boolean isValid = Pattern.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", date);
		return isValid;
	}
	
	/**
	 * 신용카드 번호
	 * @param cardNo
	 * @return
	 */
	public static boolean isCreditCard(String cardNo) {
		boolean isValid = Pattern.matches("^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|6(?:011|5[0-9]{2})[0-9]{12}|(?:2131|1800|35\\d{3})\\d{11})$", cardNo);
		return isValid;
	}
}
