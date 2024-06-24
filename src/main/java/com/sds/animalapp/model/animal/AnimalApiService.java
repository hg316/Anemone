package com.sds.animalapp.model.animal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sds.animalapp.domain.Animal;

@Service
public class AnimalApiService {

	@Value("${animal.api.key}")
	private String key;

	public List<Animal> call() throws IOException {
		List<Animal> allAnimals = new ArrayList<>();
		int pageNo = 1;
		int numOfRows = 100; // 필요한 경우 조정 가능
		boolean hasMoreData = true;

		while (hasMoreData) {
			String animalCdResponse = null;
			List<Animal> animalList = null;

			try {
				StringBuilder urlBuilder = new StringBuilder(
						"http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic");
				urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + key);
				urlBuilder.append("&" + URLEncoder.encode("bgnde", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
				urlBuilder.append("&" + URLEncoder.encode("endde", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
				urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
				urlBuilder.append("&" + URLEncoder.encode("kind", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
				urlBuilder.append("&" + URLEncoder.encode("upr_cd", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
				urlBuilder.append("&" + URLEncoder.encode("org_cd", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
				urlBuilder
						.append("&" + URLEncoder.encode("care_reg_no", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
				urlBuilder.append("&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
				urlBuilder.append("&" + URLEncoder.encode("neuter_yn", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
				urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "="
						+ URLEncoder.encode(String.valueOf(pageNo), "UTF-8"));
				urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
						+ URLEncoder.encode(String.valueOf(numOfRows), "UTF-8"));
				urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
				URL url = new URL(urlBuilder.toString());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-type", "application/json");
				System.out.println("Response code: " + conn.getResponseCode());
				BufferedReader rd;
				if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
					rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				} else {
					rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				}
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();
				conn.disconnect();
				System.out.println(sb.toString());
				animalCdResponse = sb.toString();

				JSONParser parser = new JSONParser();
				JSONObject json;
				json = (JSONObject) parser.parse(animalCdResponse);
				json = (JSONObject) json.get("response");
				json = (JSONObject) json.get("body");
				long totalCount = (long) json.get("totalCount"); // 총 데이터 수
				json = (JSONObject) json.get("items");
				JSONArray array = (JSONArray) json.get("item");

				Gson gson = new Gson();
				Type shelterArray = new TypeToken<List<Animal>>() {
				}.getType();
				animalList = gson.fromJson(array.toJSONString(), shelterArray);

				if (animalList != null) {
					allAnimals.addAll(animalList);
				}

				if (animalList.size() < numOfRows || allAnimals.size() >= totalCount) {
					hasMoreData = false; // 더 이상 데이터가 없거나 총 개수를 초과하면 중지
				} else {
					pageNo++;
				}

			} catch (ParseException e) {
				e.printStackTrace();
				break;
			}
		}

		return allAnimals;
	}
}
