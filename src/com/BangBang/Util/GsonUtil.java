package com.BangBang.Util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.log4j.Logger;
	/*
	 * json util�?
	 * zhangmeng
	 * 2015-05-20
	 */
public class GsonUtil {
	
	private static Logger logger = Logger.getLogger(GsonUtil.class); 
	/*
	 * 将传入的object转换成json字符串并response到浏览器
	 * 
	 */
	public static void OnjectToJson(Object src) {
		try {
		Gson gson = new GsonBuilder().serializeNulls().create();
        String str = gson.toJson(src);
        str=str.replaceAll("null","\"\"");
        logger.info("返回json数据(来自GsonUtil)" +str);
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");

			ServletActionContext.getResponse().getWriter().write(str);
			ServletActionContext.getResponse().getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}
	
	
	public static void OnjectToJsonP(Object src) {
		HttpServletRequest req = ServletActionContext.getRequest();
		String callback = req.getParameter("callback");
		try {
			Gson gson = new GsonBuilder().serializeNulls().create();
	        String str = gson.toJson(src);
	        str=str.replaceAll("null","\"\"");
	        str = callback + "(" + str + ")";  
	        logger.info("返回json数据(来自GsonUtil)" + str);
	        ServletActionContext.getResponse().setContentType("text/javascript;charset=utf-8");
				ServletActionContext.getResponse().getWriter().write(str);
				ServletActionContext.getResponse().getWriter().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}	
	}
	/*
	 * 将传入的object转换成json字符串并response到浏览器
	 * 作者：ZJB
	 */
	public static String zjbObjectToJson(Object src) {
		Gson gson = new GsonBuilder().serializeNulls().create();
		String str = gson.toJson(src);
		
		return str;
	}
	
	public static List<Map<String, String>> stringToJsonlist(String str) {
		Gson gson = new Gson();
		List<Map<String,String>> list =   gson.fromJson(str,new TypeToken<List<Map<String,String>>>(){}.getType());
	   return list;
	}
	public static Map<String, String> stringToJson(String str) {
		Gson gson = new Gson();
		Map<String,String>  map =   gson.fromJson(str,new TypeToken< Map<String,String>>(){}.getType());
	   return map;
	}
	

}
