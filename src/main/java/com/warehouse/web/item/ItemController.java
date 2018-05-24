package com.warehouse.web.item;

import java.util.HashMap;

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

import com.warehouse.Util;

@Controller
@RequestMapping("/item")
public class ItemController {
	@Value("${warehouse.core.address}")
	private String coreAddress;
	
	@RequestMapping("/inquiry")
	public String inquiryItem(Model model) {
		return "item/inquiry-item";
	}
	
	@RequestMapping("/create")
	public String createItem(Model model) {
		return "item/create-item";
	}
	
	@RequestMapping("/modify")
	public String modifyItem(Model model) {
		return "item/modify-item";
	}
	
	@RequestMapping("/inquiry/getItem")
	public ResponseEntity<String> getItem(
			@RequestParam("article") String article,
			@RequestParam("name") String name,
			@RequestParam("unit") String unit,
			@RequestParam(value = "limit", defaultValue = "10") Integer limit,
			@RequestParam(value = "offset",  defaultValue = "0") Integer offset,
			@RequestParam(value = "order", defaultValue = "asc") String order,
			@RequestParam(value = "sort", defaultValue = "id") String sort) {
		HashMap<String, Object> query = new HashMap<>();
		query.put("article", article);
		query.put("name", name);
		query.put("unit", unit);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("page", offset/limit);
		param.put("limit", limit);
		param.put("direction", order);
		param.put("orderBy", sort);
		param.put("query", query);
		
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> ret = rt.postForEntity(coreAddress.concat("/item/getItem"), param, String.class);
		return new ResponseEntity<String>(Util.convertPageToString(ret.getBody()), ret.getStatusCode());
	}
	
	@RequestMapping("/inquiry/getItemDesc")
	public ResponseEntity<String> getItemDesc(
			@RequestParam("id") String id,
			@RequestParam(value = "limit", defaultValue = "10") Integer limit,
			@RequestParam(value = "offset",  defaultValue = "0") Integer offset,
			@RequestParam(value = "order", defaultValue = "asc") String order,
			@RequestParam(value = "sort", defaultValue = "id") String sort) {
		HashMap<String, Object> query = new HashMap<>();
		query.put("id", id);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("page", offset/limit);
		param.put("limit", limit);
		param.put("direction", order);
		param.put("orderBy", sort);
		param.put("query", query);
		
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> ret = rt.postForEntity(coreAddress.concat("/itemDesc/getItemDesc"), param, String.class);
		return new ResponseEntity<String>(Util.convertPageToString(ret.getBody()), ret.getStatusCode());
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/createItem")
	public ResponseEntity<String> createItem(@RequestBody HashMap<String, Object> data) {
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> ret = rt.postForEntity(coreAddress.concat("/item/createItem"), data, String.class);
		return ret;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/deleteItem")
	public ResponseEntity<String> deleteItem(@RequestBody HashMap<String, Object> data) {
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> ret = rt.postForEntity(coreAddress.concat("/item/deleteItem"), data, String.class);
		return ret;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/isArticleExist")
	public ResponseEntity<Boolean> isArticleExist(@RequestParam("article") String article) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("article", article);
		
		try {
			RestTemplate rt = new RestTemplate();
			ResponseEntity<String> result = rt.postForEntity(coreAddress.concat("/item/isArticleExist"), param, String.class);
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
}
