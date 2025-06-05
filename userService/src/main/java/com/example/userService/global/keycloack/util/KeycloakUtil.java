package com.example.userService.global.keycloack.util;

import com.example.userService.global.exception.CCommonException;
import com.example.userService.module.user.dto.TokenResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 2025-06-05: 로깅 추가 – Logger 인스턴스 및 주요 메서드 내 요청/응답 로깅

@Service
@RequiredArgsConstructor
public class KeycloakUtil {

    // Logger 인스턴스 추가
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KeycloakUtil.class);

    public static TokenResponseDto getAccessToken(String userId, String password) throws Exception {

        TokenResponseDto dto = new TokenResponseDto();
        OutputStream os = null;
        BufferedReader in = null;

        try {
            URI uri = new URI("http://localhost:8088/realms/test_realm/protocol/openid-connect/token");
            URL url = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // 요청 정보 로깅
            logger.debug("Requesting AccessToken from URL={} with userId={}", uri, userId);

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setDoOutput(true);

            String params = "grant_type=password"
                    + "&client_id=test_portal"
                    + "&username=" + userId
                    + "&password=" + password
                    + "&scope=openid";

            logger.debug("getAccessToken 파라미터: {}", params);

            os = con.getOutputStream();
            os.write(params.getBytes("utf-8"));
            os.flush();

            int responseCode = con.getResponseCode();
            logger.debug("getAccessToken 응답 코드: {}", responseCode);

            if (responseCode >= 200 && responseCode <= 209) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                String responseJson = response.toString();
                logger.debug("getAccessToken 응답 바디: {}", responseJson);

                JsonObject jsonObject = JsonParser.parseString(responseJson).getAsJsonObject();
                dto.setAccessToken(jsonObject.get("access_token").getAsString());
                dto.setRefreshToken(jsonObject.get("refresh_token").getAsString());
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                logger.error("getAccessToken 오류 응답: {}", response.toString());
                throw new CCommonException();
            }

        } catch (IOException e) {
            logger.error("getAccessToken IOException 발생", e);
            throw new CCommonException();
        } finally {
            if (in != null) {
                in.close();
            }
            if (os != null) {
                os.close();
            }
        }

        return dto;
    }

    public static String getAdminAccessToken() throws Exception {

        String token = null;
        OutputStream os = null;
        BufferedReader in = null;

        try {
            URI uri = new URI("http://localhost:8088/realms/test_realm/protocol/openid-connect/token");
            URL url = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // 요청 정보 로깅
            logger.debug("Requesting AdminAccessToken from URL={}", uri);

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setDoOutput(true);

            String params = "grant_type=password"
                    + "&client_id=test_portal"
                    + "&username=test"
                    + "&password=test123!";

            logger.debug("getAdminAccessToken 파라미터: {}", params);

            os = con.getOutputStream();
            os.write(params.getBytes("utf-8"));
            os.flush();

            int responseCode = con.getResponseCode();
            logger.debug("getAdminAccessToken 응답 코드: {}", responseCode);

            if (responseCode >= 200 && responseCode <= 209) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                String responseJson = response.toString();
                logger.debug("getAdminAccessToken 응답 바디: {}", responseJson);

                JsonObject jsonObject = JsonParser.parseString(responseJson).getAsJsonObject();
                token = jsonObject.get("access_token").getAsString();
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                logger.error("getAdminAccessToken 오류 응답: {}", response.toString());
                throw new CCommonException();
            }

        } catch (IOException e) {
            logger.error("getAdminAccessToken IOException 발생", e);
            throw new CCommonException();
        } finally {
            if (in != null) {
                in.close();
            }
            if (os != null) {
                os.close();
            }
        }

        return token;
    }

    public static String createUser(String accessToken, String domaincd, String useruuid, String username, String email, String password, int rolelevel) throws IOException, URISyntaxException {

        OutputStream os = null;
        BufferedReader in = null;
        String error = null;

        try {
            URI uri = new URI("http://localhost:8088/realms/test_realm/protocol/openid-connect/token");
            URL url = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // 요청 정보 로깅
            logger.debug("Requesting createUser to URL={} with username={}, email={}", uri, username, email);

            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + accessToken);
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payload = new HashMap<>();
            payload.put("username", username);
            payload.put("firstName", username);
            payload.put("lastName", username);
            payload.put("email", email);
            payload.put("enabled", true);
            payload.put("emailVerified", true);

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("domain", List.of(domaincd));
            attributes.put("user_uuid", List.of(useruuid));
            attributes.put("role_level", List.of(String.valueOf(rolelevel)));
            payload.put("attributes", attributes);

            Map<String, Object> credentials = new HashMap<>();
            credentials.put("type", "password");
            credentials.put("value", password);
            credentials.put("temporary", false);
            payload.put("credentials", List.of(credentials));

            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
            logger.debug("createUser 페이로드: {}", json);

            os = con.getOutputStream();
            os.write(json.getBytes("utf-8"));
            os.flush();

            int responseCode = con.getResponseCode();
            logger.debug("createUser 응답 코드: {}", responseCode);

            if (responseCode >= 200 && responseCode <= 209) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                logger.debug("createUser 성공 응답 바디: {}", response.toString());
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                String responseJson = response.toString();
                JsonObject jsonObject = JsonParser.parseString(responseJson).getAsJsonObject();
                JsonElement errElem = jsonObject.get("errorMessage");
                if (errElem == null || errElem.isJsonNull()) {
                         error = "Unknown error: " + responseJson;
                     } else {
                         error = errElem.getAsString();
                     }
                logger.error("createUser 오류 응답: {}", responseJson);
                throw new CCommonException();
            }

        } catch (IOException e) {
            logger.error("createUser IOException 발생", e);
            throw new CCommonException();
        } finally {
            if (in != null) {
                in.close();
            }
            if (os != null) {
                os.close();
            }
        }

        return error;
    }
}
