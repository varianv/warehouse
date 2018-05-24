package com.warehouse.web;

import java.util.Comparator;
import java.util.Map;

public class CustomComparator implements Comparator<Map<String, Object>> {
	String sort;
	
	public CustomComparator(String sort){
		this.sort = sort;
	}
	
	@Override
	public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		if (o1.get(this.sort) instanceof String) {
			return o1.get(this.sort).toString().compareTo(o2.get(this.sort).toString());
		}
		else if (o1.get(this.sort) instanceof Integer) {
			return new Integer(o1.get(this.sort).toString()).compareTo(new Integer(o2.get(this.sort).toString()));
		}
		return 0;
	}
}
