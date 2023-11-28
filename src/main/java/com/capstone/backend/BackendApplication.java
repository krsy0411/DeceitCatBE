package com.capstone.backend;

import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@SpringBootApplication
public class BackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
//	public static void main(String[] args) {
//		String message = "선생님, 우리 아이 왜 혼내세요? 이거 아동학대 아닌가요?";
//		String requestUrl = "http://43.202.161.139:8888/" + message;
//
//		HttpHeaders header = new HttpHeaders();
//		HttpEntity<String> entity = new HttpEntity<>(header);
//		ResponseEntity<JSONObject> responseEntity = new RestTemplate().exchange (
//				requestUrl, HttpMethod.GET, entity, JSONObject. class
//		) ;
//		JSONObject j = new JSONObject (responseEntity.getBody());
//		boolean i = (Integer)((ArrayList) j.get("model_result")).get(0) == 1;
//		System.out.println(message + Boolean.toString(i));
//	}
}

