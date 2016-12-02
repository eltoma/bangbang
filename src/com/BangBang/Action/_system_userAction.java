package com.BangBang.Action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;



import com.BangBang.Util.GsonUtil;
import com.BangBang.Util.QueryUtil;
import com.opensymphony.xwork2.ActionSupport;

public class _system_userAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	public void Login() {
		
		String sqlString = "select 1 ";
		
		QueryUtil queryUtil = new QueryUtil();
		List<Map<String, Object>> datalist = queryUtil.QueryBySql(sqlString);
		
		
		System.out.println("被访问了！");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		GsonUtil.OnjectToJson(datalist);
	}
}
