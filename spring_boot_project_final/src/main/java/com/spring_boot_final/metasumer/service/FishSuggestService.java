package com.spring_boot_final.metasumer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FishSuggestService implements IFishSuggestService {

    private static final String ES_URL = "http://localhost:9200/fishinfo/_search";

    @Override
    public List<String> getSuggestions(String keyword) {
        // 1. JSON 쿼리 (match + fuzziness)
        String requestJson = """
        {
          "query": {
            "match": {
              "fishName": {
                "query": "%s",
                "fuzziness": "AUTO"
              }
            }
          }
        }
        """.formatted(keyword);

        // 2. 요청 헤더 설정 (Content-Type)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. 요청 바디 + 헤더 묶기
        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        // 4. RestTemplate 사용
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(ES_URL, requestEntity, Map.class);
        Map<String, Object> response = responseEntity.getBody();

        // 5. 결과 파싱
        List<String> suggestions = new ArrayList<>();

        try {
            Map hitsObj = (Map) response.get("hits");
            List<Map<String, Object>> hitList = (List<Map<String, Object>>) hitsObj.get("hits");

            for (Map<String, Object> hit : hitList) {
                Map<String, Object> source = (Map<String, Object>) hit.get("_source");
                String fishName = (String) source.get("fishName");
                suggestions.add(fishName);
            }

        } catch (Exception e) {
            // 실패해도 무시
        }

        return suggestions;
    }
}
