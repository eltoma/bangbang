package com.BangBang.Action;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.expr.NewArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.BangBang.Util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class _system_userAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	ActionContext Context = ActionContext.getContext();
	HttpServletRequest request = ServletActionContext.getRequest();
	
	HttpServletResponse response = ServletActionContext.getResponse();
	HttpSession htsession  = request.getSession();	
	
	Map<String, Object> map = new HashMap<String, Object>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void Login() {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if(username == null || password == null){
			map.put("success", "1");
			map.put("msg", "登陆信息不完整");
			
			
			/*	String output = callback + "(" +GsonUtil.zjbObjectToJson(map) + ")";  
			
			
			response.setContentType("text/javascript;charset=utf-8");
			try {
				ServletActionContext.getResponse().getWriter().write(output);
				ServletActionContext.getResponse().getWriter().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			//String callback = request.getParameter("callback");
			GsonUtil.OnjectToJsonP(map);
			
			return;
		}
		
		String md5passwd = CipherUtil.generatePassword(password);
		
		String sqlString = "select * from userinfo where userName=? and password=?";
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List datalist = null;
		try {
			session.beginTransaction();
			Query query = session.createSQLQuery(sqlString);
			
			query.setString(0, username);
			query.setString(1, md5passwd);
			
			datalist = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getCause());
			
			GsonUtil.OnjectToJsonP(map);
			return;
		}
		
		if(datalist.size() == 0) {
			map.put("success", false);
			map.put("msg", "用户名或者密码错误");
			
			GsonUtil.OnjectToJsonP(map);
			return;
		}
		String uname = (String) htsession.getAttribute("username");
		if(htsession.getAttribute("username") == null){
			System.out.println("未登陆");
			htsession.setAttribute("username",username);
		}
		
		System.out.println("登陆用户："+htsession.getAttribute("username"));
		/*Map<String, Object> app = Context.getApplication();
		
		if(app.get("onlineUers") == null){
			app.put("onlineUers", new HashSet<String>()); 
		}
		
		if(uname == null) {
			htsession.setAttribute("username", username);
			
			Set<String> onlineUers = (Set<String>) app.get("onlineUers");
			try {
				onlineUers.add(username);
				app.put("onlineUers", onlineUers);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Set<String> onlineUers = (Set<String>) app.get("onlineUers");
		System.out.println("在线用户列表：" + onlineUers.toString());
		*/
		map.put("success", true);
		map.put("msg", "登录成功");
		GsonUtil.OnjectToJsonP(map);
    }
	
	public boolean isLogined(String username){
		return (null != htsession.getAttribute(username));
	}
	
	   public void logout()
	    {
	    	HttpSession session  = request.getSession();  
	    	if(session != null)
	    	{
	    		try {
	    			session.invalidate();
	    			map.put("success", true);
	    			map.put("msg","退出成功！");
				} catch (Exception e) {
					map.put("success", false);
				}
	    	}
	    	
	    	GsonUtil.OnjectToJson(map);
	    }
}

