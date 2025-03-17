package com.spring_boot_final.metasumer.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.regex.*;

import org.springframework.stereotype.Service;

@Service
public class WeatherService {
	
	// 현재 날짜의 음력 날짜를 얻어옴 : 한국천문연구원_음양력 정보 API 사용
    public HashMap<String, String> getLunarDate(){
    	// 현재 날짜
    	LocalDate now = LocalDate.now();
    	String year = Integer.toString(now.getYear());
    	String month = Integer.toString(now.getMonthValue());
    	if(month.length() == 1) { month = "0" + month; }
    	String day = Integer.toString(now.getDayOfMonth());
    	if(day.length() == 1) { day = "0" + day; }
    	//System.out.println(year + ". " + month + ". " + day);
    	
    	// 반환값 : 음력 날짜
    	HashMap<String, String> lunarDate = new HashMap<String, String>();
    	
        try {
			StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/LrsrCldInfoService/getLunCalInfo"); /*URL*/
			urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=fqS%2BPfxxrGF5dFqOBV3TzLPvPUgFJColfp0gTsV%2BNWtZkwvPi7jdG9Oa3zpaHcBNvKFbBeDYDqWcoaTm8OiyKw%3D%3D"); /*Service Key*/
			
			urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode(year, "UTF-8")); /*연*/
			urlBuilder.append("&" + URLEncoder.encode("solMonth","UTF-8") + "=" + URLEncoder.encode(month, "UTF-8")); /*월*/
			urlBuilder.append("&" + URLEncoder.encode("solDay","UTF-8") + "=" + URLEncoder.encode(day, "UTF-8")); /*일*/
			
			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/json");
			//System.out.println("Response code: " + conn.getResponseCode());
			
			BufferedReader rd;
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
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
			
			//System.out.println(sb.toString());
			lunarDate = dateToString(sb.toString());

	    	lunarDate.put("year", year);
	    	lunarDate.put("month", month);
	    	lunarDate.put("day", day);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return lunarDate;
    } // getLunarDate() 끝
    
    // API 데이터에서 필요한 값만 추출
    public HashMap<String, String> dateToString(String lunarDate) {
    	HashMap<String, String> dateMap = new HashMap<String, String>();
    	
    	String[] searchList = {"lunDay", "lunMonth", "lunYear"};
    	
    	for (int i=0; i<searchList.length; i++) {
    		final String regexWord = "(<" + searchList[i] + ">)(.*?)(</" + searchList[i] + ">)";
    		Pattern pattern = Pattern.compile(regexWord);
    		Matcher matcher = pattern.matcher(lunarDate);
    		
    		if (matcher.find()) {
    			String matchResult = matcher.group(2).trim();
    			dateMap.put(searchList[i], matchResult); // 정규식에서 두 번째 괄호의 값을 가져옴
    		}
    	}
    	return dateMap;
    }

}
