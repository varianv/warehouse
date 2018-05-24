package com.warehouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.web.CustomComparator;

public class Util {
	/**
	 * Konversi JSON menjadi {@link HashMap} yang key-nya <code>total</code> dan <code>rows</code>, kemudian dikonversi lagi menjadi JSON.
	 */
	public static String convertPageToString(String content) {
		try {
			Map<String, Object> returnMap = new HashMap<>();
			Map<String, Object> contentMap = new ObjectMapper().readValue(content, new TypeReference<Map<String, Object>>(){});
			returnMap.put("total", contentMap.get("totalElements"));
			returnMap.put("rows", contentMap.get("content"));
			if (!returnMap.isEmpty()) return new ObjectMapper().writeValueAsString(returnMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getParameter(String coreAddress, String name) {
		HashMap<String, Object> query = new HashMap<>();
		query.put("paramName", name);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> ret = rt.postForEntity(coreAddress.concat("/parameter/getParameter"), query, String.class);
		return ret.getBody();
	}
	
	/**
	 * Konversi JSON menjadi sesuai dengan inputan className-nya.
	 */
	public static <T> T readValue(String str, Class<T> className) {
		try {
			return new ObjectMapper().readValue(str, className);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Konversi object menjadi JSON.
	 */
	public static String toString(Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get data dari session kemudian di-filter sesuai kriteria yang diberikan dari GUI.
	 */
	public static DatatableSet getTableData(List<Map<String, Object>> listDataOnSession, Integer limit, Integer offset, String order, String sort) {
		Collections.sort(listDataOnSession, new CustomComparator(sort));
		if (order.equals("desc")) Collections.reverse(listDataOnSession);
		int fromIndex = offset;
		int toIndex = offset+limit;
		List<Map<String, Object>> rows = null;
		if(toIndex < listDataOnSession.size()) {
			rows = listDataOnSession.subList(fromIndex, toIndex);
		} else {
			rows = listDataOnSession.subList(fromIndex, listDataOnSession.size());
		}
		return new DatatableSet(listDataOnSession.size(), rows);
	}
	
	/**
	 * Add data ke list session.
	 */
	public static List<HashMap<String, Object>> addRow(List<HashMap<String, Object>> listDataOnSession, HashMap<String, Object> row) {
		listDataOnSession.add(row);
		return listDataOnSession;
	}
	
	/**
	 * Add list data ke list session.
	 */
	public static List<Map<String, Object>> addAll(List<Map<String, Object>> listDataOnSession, List<Map<String, Object>> rows) {
		for (Map<String, Object> map : rows) {
			listDataOnSession.add(map);
		}
		return listDataOnSession;
	}
	
	/**
	 * Edit data dari list session menggunakan primary key-nya.
	 */
	public static List<HashMap<String, Object>> editRow(List<HashMap<String, Object>> listDataOnSession, HashMap<String, Object> selectedRow, String keyEdit, String keyValue) {
		for (int i = 0; i < listDataOnSession.size(); i++) {
			if (listDataOnSession.get(i).get(keyEdit).toString().equals(keyValue)) {
				listDataOnSession.remove(listDataOnSession.get(i));
				listDataOnSession.add(selectedRow);
			}
		}
		return listDataOnSession;
	}
	
	/**
	 * Delete data dari list session menggunakan primary key-nya.
	 */
	public static List<HashMap<String, Object>> deleteRow(List<HashMap<String, Object>> listDataOnSession, List<HashMap<String, Object>> selectedRows, String keyToBeRemove) {
		List<HashMap<String, Object>> listDataOnSessionTemp = new ArrayList<>();
		listDataOnSessionTemp.addAll(listDataOnSession);
		for(HashMap<String, Object> selectedRow : selectedRows){
			String idToBeRemoved = selectedRow.get(keyToBeRemove).toString();
			for (HashMap<String, Object> singleData : listDataOnSessionTemp) {
				String idFromData = singleData.get(keyToBeRemove).toString();
				if (idToBeRemoved.equals(idFromData)) {
					listDataOnSession.remove(singleData);
				}
			}
		}
		return listDataOnSession;
	}
}
