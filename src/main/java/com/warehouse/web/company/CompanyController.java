package com.warehouse.web.company;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.warehouse.Util;

@Controller
@RequestMapping("/company")
public class CompanyController {
	@Value("${warehouse.core.address}")
	private String coreAddress;
	
	@RequestMapping("/inquiry")
	public String inquiryCompany(Model model) {
		return "company/inquiry-company";
	}
	
	@RequestMapping("/create")
	public String createCompany(Model model) {
		return "company/create-company";
	}
	
	@RequestMapping("/modify")
	public String modifyCompany(Model model) {
		return "company/modify-company";
	}
	
	@RequestMapping("/inquiry/getCompany")
	public ResponseEntity<String> getCompany(
			@RequestParam("name") String name,
			@RequestParam("address") String address,
			@RequestParam("noPhone") String noPhone,
			@RequestParam("status") String status,
			@RequestParam(value = "limit", defaultValue = "10") Integer limit,
			@RequestParam(value = "offset",  defaultValue = "0") Integer offset,
			@RequestParam(value = "order", defaultValue = "asc") String order,
			@RequestParam(value = "sort", defaultValue = "id") String sort) throws JsonParseException, JsonMappingException, IOException {
		HashMap<String, Object> query = new HashMap<>();
		query.put("name", name);
		query.put("address", address);
		query.put("noPhone", noPhone);
		query.put("status", status);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("page", offset/limit);
		param.put("limit", limit);
		param.put("direction", order);
		param.put("orderBy", sort);
		param.put("query", query);
		
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> ret = rt.postForEntity(coreAddress.concat("/company/getCompany"), param, String.class);
		return new ResponseEntity<String>(convertStatus(ret.getBody()), ret.getStatusCode());
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/createCompany")
	public ResponseEntity<String> createCompany(@RequestBody HashMap<String, Object> data) {
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> ret = rt.postForEntity(coreAddress.concat("/company/createCompany"), data, String.class);
		return ret;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/deleteCompany")
	public ResponseEntity<String> deleteCompany(@RequestBody HashMap<String, Object> data) {
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> ret = rt.postForEntity(coreAddress.concat("/company/deleteCompany"), data, String.class);
		return ret;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/isCompanyExist")
	public ResponseEntity<Boolean> isCompanyExist(@RequestParam("name") String name) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("name", name);
		
		try {
			RestTemplate rt = new RestTemplate();
			ResponseEntity<String> result = rt.postForEntity(coreAddress.concat("/company/isCompanyExist"), param, String.class);
			if(result.getStatusCode() == HttpStatus.OK) {
				if (result.getBody().toString().equals("true")) {
					return new ResponseEntity<Boolean>(false, result.getStatusCode());
				} else if (result.getBody().toString().equals("false")) {
					return new ResponseEntity<Boolean>(true, result.getStatusCode());
				} else {
					return null;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	/**
	 * Konversi kolom <code>status</code> menjadi sesuai dengan Parameter <code>COMPANY_STATUS</code>.
	 * Konversi menggunakan huruf pertama, karena di tabel <code>COMPANY</code> kolom <code>status</code> hanya terdiri dari 1 huruf.
	 * @return body yang kolom <code>status</code>nya sudah diubah
	 */
	@SuppressWarnings("unchecked")
	private String convertStatus(String body) {
		HashMap<String, Object> map = Util.readValue(body, HashMap.class);
		List<HashMap<String, Object>> mapContent = (List<HashMap<String, Object>>) map.get("content");
		List<HashMap<String, Object>> mapParameter = Util.readValue(Util.getParameter(coreAddress, "COMPANY_STATUS"), List.class);
		for (HashMap<String, Object> content : mapContent) {
			for (HashMap<String, Object> parameter : mapParameter) {
				if (parameter.get("value").toString().toUpperCase().substring(0, 1).equals(content.get("status").toString())) {
					content.put("status", parameter.get("value").toString());
					break;
				}
			}
		}
		HashMap<String, Object> returnNew = new HashMap<>();
		returnNew.put("total", mapContent.size());
		returnNew.put("rows", mapContent);
		return Util.toString(returnNew);
	}
}
