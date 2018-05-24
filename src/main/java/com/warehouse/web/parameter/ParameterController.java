package com.warehouse.web.parameter;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/parameter")
public class ParameterController {
	@Value("${warehouse.core.address}")
	private String coreAddress;
	
//	@RequestMapping("/parameter")
//	public String inquiryParameter(Model model) {
//		return "parameter/inquiry-parameter";
//	}
//	
//	@RequestMapping("/create")
//	public String createParameter(Model model) {
//		return "parameter/create-parameter";
//	}
//	
//	@RequestMapping("/modify")
//	public String modifyParameter(Model model) {
//		return "parameter/modify-parameter";
//	}
	
	@RequestMapping("/getParameter")
	public ResponseEntity<String> getParameter(@RequestParam("name") String name) {
		HashMap<String, Object> query = new HashMap<>();
		query.put("paramName", name);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> ret = rt.postForEntity(coreAddress.concat("/parameter/getParameter"), query, String.class);
		return ret;
	}
	
	@RequestMapping("/getParameterWithAll")
	public ResponseEntity<String> getParameterWithAll(@RequestParam("name") String name) {
		HashMap<String, Object> query = new HashMap<>();
		query.put("paramName", name);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> ret = rt.postForEntity(coreAddress.concat("/parameter/getParameterWithAll"), query, String.class);
		return ret;
	}
}
