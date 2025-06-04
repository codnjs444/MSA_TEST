package com.example.userService.module.otp.service;

import com.example.userService.global.exception.CAuthenticationOTPException;
import com.example.userService.global.exception.CMailSenderException;
import com.example.userService.global.exception.CValidationEmailException;
import com.example.userService.global.redis.util.RedisUtil;
import com.example.userService.global.utils.RegexUtils;
import com.example.userService.module.otp.dto.OtpRequestDto;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.SecureRandom;
import java.util.*;


@Service
@RequiredArgsConstructor
public class OtpService {

	private final RedisUtil redisUtil;

	@Value("${naver-mail.secret-key}")
	private String secretKey;

	@Value("${naver-mail.api-key}")
	private String apiKey;

	@Value("${naver-mail.path}")
	private String naverMailPath;
	
	
	public void reqOtp(String email, String type) throws CMailSenderException, Exception {
		if (!RegexUtils.isEmail(email)) {
			throw new CValidationEmailException(email);
		}

		OtpRequestDto param = new OtpRequestDto();
		String otp = generateRandom6DigitNumber();
		String secretKey = RandomString.make(8);

		param.setEmail(email);
		param.setPassword(otp);
		param.setSecretKey(secretKey);
		
		String res = sendMailNaver(param, type, "");
		 
		if (res != "ok") {
			throw new CMailSenderException(email);
		}else {
			String key = "otp:" + email;
			//TODO DTO 클래스로 변경해야함
			redisUtil.saveOtpToken(key, otp, secretKey, 300);
			return;
		}
		 
	}

	public String sendMailNaver(OtpRequestDto param, String type, String name) throws CMailSenderException, Exception {
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> recipients = new ArrayList<>();
		JSONObject recipient = new JSONObject();
		JSONObject parameters = new JSONObject();
		String body;
		jsonObject.put("senderAddress", "no_reply@i-gns.co.kr");
		jsonObject.put("title", "회원가입 본인 인증 안내");
		jsonObject.put("templateSid", 10490);
		recipient.put("address", param.getEmail().toString());
		recipient.put("type", "R");
		recipients.add(recipient);
		jsonObject.put("recipients", recipients);

		if (type.equals("C")) {
			jsonObject.put("templateSid", 10491);
			jsonObject.put("title", "이메일 변경 본인 인증 안내");
			parameters.put("otp", param.getPassword());
			parameters.put("name", name);
			jsonObject.put("parameters", parameters);
		} else if (type.equals("J")) {
			jsonObject.put("templateSid", 10490);
			jsonObject.put("title", "회원가입 본인 인증 안내");
			parameters.put("otp", param.getPassword());
			jsonObject.put("parameters", parameters);
		} else if (type.equals("P")) {
			jsonObject.put("templateSid", 10489);
			jsonObject.put("title", "비밀번호 찾기 본인 인증 안내");
			parameters.put("otp", param.getPassword());
			parameters.put("name", name);
			jsonObject.put("parameters", parameters);
		} else
			return "";

		body = jsonObject.toJSONString();
		
		OutputStream os = null;
		BufferedReader in = null;
		
		try {
			URI uri = new URI(naverMailPath);
			URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			StringBuilder response = new StringBuilder();
			String inputLine;

			String timestamp = Long.toString(new Date().getTime());
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
			con.setRequestProperty("x-ncp-iam-access-key", apiKey);
			con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(timestamp, apiKey, secretKey));
			con.setDoOutput(true);

			if (!body.isEmpty()) {
				// Map형식 파라미터
				os = con.getOutputStream();
				os.write(body.getBytes("utf-8"));
				os.flush();
				os.close();
			}
			int responseCode = con.getResponseCode();
			if (responseCode >= 200 && responseCode <= 209) {
				in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
			} else {

				in = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (in != null) {
	            in.close();
	        }
	        if (os != null) {
	            os.close();
	        }
		}
		return "ok";
	}

	private String makeSignature(String timestamp, String accessKey, String secretKey) throws Exception {
		String space = " "; // 공백
		String newLine = "\n"; // 줄바꿈
		String method = "POST"; // HTTP 메소드
		String url = "/api/v1/mails"; // 도메인을 제외한 "/" 아래 전체 url (쿼리스트링 포함)

		String message = new StringBuilder().append(method).append(space).append(url).append(newLine).append(timestamp)
				.append(newLine).append(accessKey).toString();

		SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(signingKey);

		byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
		String encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);

		return encodeBase64String;
	}
	
	private String generateRandom6DigitNumber() {
		int min = 100000; // 최소값 (6자리 숫자)
	    int max = 999999; // 최대값 (6자리 숫자)
	    SecureRandom secureRandom = new SecureRandom();
	    int result = secureRandom.nextInt(max - min + 1) + min;
	    String strResult = Integer.toString(result);
	    return strResult;
	}
	
	public String certification(OtpRequestDto param) throws Exception {
		
		String email = param.getEmail();
		String key = "otp:" + email;
		String inputOtp = param.getPassword().trim();
		
		Map<Object, Object> otpData = redisUtil.getOtpToken(key);
		
		if (otpData == null || !inputOtp.equals(otpData.get("otp").toString())) {
			throw new CAuthenticationOTPException(email);
		}
		
		 String secretKey = otpData.get("secretkey").toString();
		 redisUtil.deleteToken(key);
		    
		return secretKey;
	}
}
