package com.fyhao.springwebapps.business;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpDbService {
	private final RestTemplate restTemplate;

	public HttpDbService() { this(new RestTemplate()); }
	HttpDbService(RestTemplate restTemplate) { this.restTemplate = restTemplate; }

	public Map<String,Object> query(Map<String,Object> request) {
		String url = System.getenv("HTTP_DB_URL");
		if(url == null || url.trim().isEmpty()) throw new IllegalStateException("HTTP_DB_URL is not configured");
        ResponseEntity<Map> resp = restTemplate.postForEntity(url, request, Map.class);
		Map<String,Object> body = resp.getBody();
		if (body == null) throw new IllegalStateException("HTTP DB returned an empty response");
		return body;
	}

	private Map<String,Object> execute(String action, String entity, Object data) {
		Map<String,Object> request = new HashMap<>();
		request.put("action", action);
		request.put("entity", entity);
		if (data != null) request.put("data", data);
		return query(request);
	}

	public Map<String,Object> insert(String entity, Map<String,Object> row) { return execute("insert", entity, row); }
	public Map<String,Object> update(String entity, Map<String,Object> row) { return execute("update", entity, row); }
	public Map<String,Object> delete(String entity, Map<String,Object> criteria) { return execute("delete", entity, criteria); }
	public Map<String,Object> insertMany(String entity, java.util.List<Map<String,Object>> rows) { return execute("insertMany", entity, rows); }
	public Map<String,Object> updateMany(String entity, java.util.List<Map<String,Object>> rows) { return execute("updateMany", entity, rows); }

	@SuppressWarnings("unchecked")
	public java.util.List<Map<String,Object>> queryList(String entity, Map<String,Object> criteria) {
		Object data = execute("queryList", entity, criteria).get("data");
		return data == null ? java.util.Collections.emptyList() : (java.util.List<Map<String,Object>>) data;
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
