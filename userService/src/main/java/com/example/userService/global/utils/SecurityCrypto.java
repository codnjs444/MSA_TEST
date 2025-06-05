package com.example.userService.global.utils;

import com.example.userService.global.log.LogMessageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 일부 Spring Security Crypto
 */
@RequiredArgsConstructor
public class SecurityCrypto {

	public static final String AES_KEY = "iljoogns004134112345678123456789";
	//TODO java.lang.IllegalArgumentException: Detected a Non-hex character at 9 or 10 position
	//iljoogns12345678은 위의 에러나감..
	public static final String IV = "12345678abcdef";
	
	/**
	 * 암호화
	 * @param key
	 * @param iv
	 * @param text
	 * @return
	 */
	public static String encryptString(String text) {
		if (text == null) {
            text = "";
        }
		try {
            TextEncryptor encryptor = Encryptors.text(AES_KEY, IV);
            return encryptor.encrypt(text);
        } catch (IllegalArgumentException e) {
        	LogMessageManager.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IllegalArgumentException", e.getMessage(), null);
            return null;
        } catch (IllegalStateException e) {
        	LogMessageManager.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IllegalStateException", e.getMessage(), null);
            return null;
        }
	}
	
	/**
	 * 복호화
	 * @param key
	 * @param iv
	 * @param text
	 * @return
	 */
	public static String decryptString(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        try {
            TextEncryptor encryptor = Encryptors.text(AES_KEY, IV);
            return encryptor.decrypt(text);
        } catch (IllegalArgumentException e) {
        	LogMessageManager.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IllegalArgumentException", e.getMessage(), null);
            return null;
        } catch (IllegalStateException e) {
        	LogMessageManager.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IllegalStateException", e.getMessage(), null);
            return null;
        }
    }
		
	/**
	 * 파일 암호화
	 * @param inputPath
	 * @param outputPath
	 */
	public static void encryptFile(String inputPath, String outputPath) {
		// Spring Security의 Encryptors는 파일 암호화를 직접 지원하지 않기 때문에
        // 별도의 방법으로 구현해야 합니다. (예: File IO와 함께 Encryptors 사용)
	}
	
	/**
     * SHA-256 해시 함수
     * @param msg
     * @return
     * @throws NoSuchAlgorithmException
     */
	public static String sha256(String msg) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(msg.getBytes());		
		return bytesToHex( md.digest() );
	}
	
	/**
     * 바이트 배열을 16진수 문자열로 변환
     * @param bytes
     * @return
     */
	public static String bytesToHex(byte[] bytes){
		StringBuilder builder = new StringBuilder();
		for( byte b:bytes){
			builder.append(String.format("%02x", b));
		}
		return builder.toString();			
	}
	
//	public static String convertIvWithHexTail(String iv) {
//	    String prefix = iv.substring(0, 8);
//	    String tail = iv.substring(8);
//	    String hexTail = bytesToHex(tail.getBytes(StandardCharsets.UTF_8));
//
//	    // 다시 합쳐서 완성된 IV 반환
//	    return prefix + hexTail;
//	}
}
