package com.yhealthy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryData {
	public static  List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("title", "好友/组");
		map.put("img", R.drawable.arrow);

		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "猛烈摇晃");
		map.put("img", R.drawable.arrow);

		list.add(map);

		map = new HashMap<String, Object>();

		map.put("title", "乐子墙");
		map.put("img", R.drawable.arrow);

		list.add(map);
		return list;
	}
}
