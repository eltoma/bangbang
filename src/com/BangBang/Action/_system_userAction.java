package com.BangBang.Action;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
			map.put("success", false);
			map.put("msg", "登陆信息不完整");
			
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
		if(htsession.getAttribute("username") == null){
			System.out.println("未登陆");
			htsession.setAttribute("username",username);
		}
		
		System.out.println("登陆用户："+htsession.getAttribute("username"));

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
    
    public void signUp(){
    	String username = request.getParameter("username");
		String password = request.getParameter("password");
		String phoneNumber = request.getParameter("phoneNumber");
		
		if(username == null || password == null || phoneNumber == null){
			map.put("success", false);
			map.put("msg", "注册信息不完整");
			GsonUtil.OnjectToJsonP(map);
			return;
		}
		
		String sqlexist = "select * from userinfo where userName=?";
		String sqladd = "INSERT INTO userinfo(userName, password, phoneNumber,score) VALUES( ?, ?, ?,10)";
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		
		String md5passwd = CipherUtil.generatePassword(password);
		
		try {
			session.beginTransaction();
			Query query = session.createSQLQuery(sqlexist);
			query.setString(0, username);
			
			List list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			if(list.size() != 0) {
				map.put("sucess", true);
				map.put("msg", "用户名已存在,请重试");
				GsonUtil.OnjectToJsonP(map);
				return;
			}
			
			query = session.createSQLQuery(sqladd);
			
			query.setString(0, username);
			query.setString(1, md5passwd);
			query.setString(2, phoneNumber);
			
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();;
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误信息:"+e.getCause());
			GsonUtil.OnjectToJsonP(map);
			return;
		}
		
		map.put("sucess", true);
		map.put("msg", "注册成功");
		GsonUtil.OnjectToJsonP(map);
    }
    
    public void getUertInfo(){
    	String username = request.getParameter("username");
    	
    	String sql = "select * from userinfo where userName='"
    			+ username +"'";
    	
    	Map<String, Object> userinfo = QueryUtil.QueryBySql(sql).get(0);
    	GsonUtil.OnjectToJsonP(userinfo);
    }
    
    public void editUertInfo() {
    	String username = strNullCheck(request.getParameter("username"));
		String password = strNullCheck(request.getParameter("password"));
		String phoneNumber = strNullCheck(request.getParameter("phoneNumber"));
    	
    	String sql = "UPDATE userinfo u set u.password = ? ,u.phoneNumber=? where userName = ?";
    	
    	Map<String, Object> userinfo = QueryUtil.QueryBySql(sql).get(0);
    	GsonUtil.OnjectToJsonP(userinfo);
    }
    
    
    public String strNullCheck(String src){
    	return (src ==null)?"":src;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}

