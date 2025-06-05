package com.example.userService.global.mail;

import com.example.userService.global.exception.CMailSenderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.util.Date;

@Component
public class NaverCloudMail {
	
	@Value("${naver-mail.secret-key}")
	private String SECRET_KEY;
	
	@Value("${naver-mail.api-key}")
	private String API_KEY;
	
	@Value("${naver-mail.path}")
	private String NAVER_MAIL_PATH;
	
	/**
	 * 메일 전송
	 */
	public boolean sendMailNaver(String body) throws CMailSenderException, Exception {
		
		OutputStream os = null;
		BufferedReader in = null;
		
		try {
			URI uri = new URI(NAVER_MAIL_PATH);
			URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			StringBuilder response = new StringBuilder();
			String inputLine;

			String timestamp = Long.toString(new Date().getTime());
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
			con.setRequestProperty("x-ncp-iam-access-key", API_KEY);
			con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(timestamp, API_KEY, SECRET_KEY));
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
		return true;
	}
	
	/**
	* 인증서 작성
	*
	* @param String : 현재 시간
	* @param String : http 메서드
	* @param String : url 정보
	* @return String
	*/
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
}
