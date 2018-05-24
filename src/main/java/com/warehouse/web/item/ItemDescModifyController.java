package com.warehouse.web.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.warehouse.DatatableSet;
import com.warehouse.Util;

@Controller
@RequestMapping("/item")
@SessionAttributes("ITEM_DESC_MODIFY_DATATABLE_SESSION")
public class ItemDescModifyController {
	private final String sessionNameModify = "ITEM_DESC_MODIFY_DATATABLE_SESSION";
	private final String primaryKey = "id";
	
	@ModelAttribute(sessionNameModify)
	public List<HashMap<String, Object>> itemDescData() {
		return new ArrayList<HashMap<String, Object>>();
	}
	
	/**
	 * Inisialisasi session
	 */
	@RequestMapping(value = "/itemDesc/modify/initSession", method = RequestMethod.GET)
	public @ResponseBody String setSessionStatusForNewAddForm(SessionStatus status, HttpServletRequest request) {
		status.setComplete();
		request.getSession();
		return "{status: 'OK'}";
	}
	
	/**
	 * Inisialisasi session
	 */
	@RequestMapping(value = "/itemDesc/modify/addAll", method = RequestMethod.POST)
	public @ResponseBody String addAll(
			@ModelAttribute(sessionNameModify) List<Map<String, Object>> itemDescData,
			@RequestBody Map<String, Object> rowFromDb) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> rows = (List<Map<String, Object>>) rowFromDb.get("rows");
		Util.addAll(itemDescData, rows);
		return "{status: 'OK'}";
	}
	
	@RequestMapping(value = "/itemDesc/modify/getTable", method = RequestMethod.GET)
	public @ResponseBody DatatableSet getItemDescTable(
			@ModelAttribute(sessionNameModify) List<Map<String, Object>> itemDescData,
			@RequestParam(value = "limit", defaultValue="10") Integer limit,
			@RequestParam(value = "offset", defaultValue="0") Integer offset,
			@RequestParam(value = "order", defaultValue="asc") String order,
			@RequestParam(value = "sort", required = false) String sort) {
		return Util.getTableData(itemDescData, limit, offset, order, sort);
	}
	
	@RequestMapping(value = "/itemDesc/modify/getAllData", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getAllData(
			@ModelAttribute(sessionNameModify) List<Map<String, Object>> itemDescData) {
		return itemDescData;
	}
	
	@RequestMapping(value = "/itemDesc/modify/add", method = RequestMethod.POST)
	public @ResponseBody String addRow(
			@ModelAttribute(sessionNameModify) List<HashMap<String, Object>> itemDescData, 
			@RequestBody HashMap<String, Object> row) {
		Util.addRow(itemDescData, row);
		return "{status: 'OK'}";
	}
	
	@RequestMapping(value = "/itemDesc/modify/edit", method = RequestMethod.POST)
	public @ResponseBody String editRow(
			@ModelAttribute(sessionNameModify) List<HashMap<String, Object>> itemDescData, 
			@RequestBody HashMap<String, Object> selectedRow) {
		Util.editRow(itemDescData, selectedRow, primaryKey, selectedRow.get(primaryKey).toString());
		return "{status: 'OK'}";
	}
	
	@RequestMapping(value = "/itemDesc/modify/delete", method = RequestMethod.POST)
	public @ResponseBody String deleteRow(
			@ModelAttribute(sessionNameModify) List<HashMap<String, Object>> itemDescData, 
			@RequestBody List<HashMap<String, Object>> selectedRows) {
		Util.deleteRow(itemDescData, selectedRows, primaryKey);
		return "{status: 'OK'}";
	}
}
