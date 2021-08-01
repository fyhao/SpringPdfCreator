package com.fyhao.springwebapps.business;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpDbService {

	public Map<String,Object> query(Map<String,Object> request) {
		String url = System.getenv("HTTP_DB_URL");
		if(url == null) return null;
		RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> resp = restTemplate.postForEntity(url, request, Map.class);
        return resp.getBody();
	}
	
	public Map<String,Object> queryRowByField(String entity, String field, String id) {
		Map<String,Object> req = new HashMap<String,Object>();
		req.put("action", "queryRowByField");
		req.put("entity", entity);
		req.put("field", field);
		req.put("id", id);
		Map<String,Object> res = query(req);
		Map<String,Object> row = (Map<String,Object>)res.get("data");
		return row;
	}
}
