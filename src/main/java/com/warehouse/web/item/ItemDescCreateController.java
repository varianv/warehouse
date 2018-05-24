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
@SessionAttributes("ITEM_DESC_CREATE_DATATABLE_SESSION")
public class ItemDescCreateController {
	private final String sessionNameCreate = "ITEM_DESC_CREATE_DATATABLE_SESSION";
	private final String primaryKey = "id";
	
	@ModelAttribute(sessionNameCreate)
	public List<HashMap<String, Object>> itemDescData() {
		return new ArrayList<HashMap<String, Object>>();
	}
	
	/**
	 * Inisialisasi session
	 */
	@RequestMapping(value = "/itemDesc/create/initSession", method = RequestMethod.GET)
	public @ResponseBody String setSessionStatusForNewAddForm(SessionStatus status, HttpServletRequest request) {
		status.setComplete();
		request.getSession();
		return "{status: 'OK'}";
	}
	
	@RequestMapping(value = "/itemDesc/create/getTable", method = RequestMethod.GET)
	public @ResponseBody DatatableSet getItemDescTable(
			@ModelAttribute(sessionNameCreate) List<Map<String, Object>> itemDescData,
			@RequestParam(value = "limit", defaultValue="10") Integer limit,
			@RequestParam(value = "offset", defaultValue="0") Integer offset,
			@RequestParam(value = "order", defaultValue="asc") String order,
			@RequestParam(value = "sort", required = false) String sort) {
		return Util.getTableData(itemDescData, limit, offset, order, sort);
	}
	
	@RequestMapping(value = "/itemDesc/create/getAllData", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getAllData(
			@ModelAttribute(sessionNameCreate) List<Map<String, Object>> itemDescData) {
		return itemDescData;
	}
	
	@RequestMapping(value = "/itemDesc/create/add", method = RequestMethod.POST)
	public @ResponseBody String addRow(
			@ModelAttribute(sessionNameCreate) List<HashMap<String, Object>> itemDescData, 
			@RequestBody HashMap<String, Object> row) {
		Util.addRow(itemDescData, row);
		return "{status: 'OK'}";
	}
	
	@RequestMapping(value = "/itemDesc/create/edit", method = RequestMethod.POST)
	public @ResponseBody String editRow(
			@ModelAttribute(sessionNameCreate) List<HashMap<String, Object>> itemDescData, 
			@RequestBody HashMap<String, Object> selectedRow) {
		Util.editRow(itemDescData, selectedRow, primaryKey, selectedRow.get(primaryKey).toString());
		return "{status: 'OK'}";
	}
	
	@RequestMapping(value = "/itemDesc/create/delete", method = RequestMethod.POST)
	public @ResponseBody String deleteRow(
			@ModelAttribute(sessionNameCreate) List<HashMap<String, Object>> itemDescData, 
			@RequestBody List<HashMap<String, Object>> selectedRows) {
		Util.deleteRow(itemDescData, selectedRows, primaryKey);
		return "{status: 'OK'}";
	}
}
