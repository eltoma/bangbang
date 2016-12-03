package com.BangBang.Action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.core.ApplicationContext;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.components.Select;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import com.BangBang.Util.*;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class _system_userAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	Map<String, Object> map = new HashMap<String, Object>();
	
	@SuppressWarnings("rawtypes")
	public void Login() {
		
		ActionContext Context = ActionContext.getContext();
		HttpServletRequest request = ServletActionContext.getRequest();

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		String md5passwd = CipherUtil.generatePassword(password);
		
		String sqlString = "select * from userinfo where userName=? and password=?";
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List datalist = null;
		try {
			session.beginTransaction();
			Query query = session.createQuery(sqlString);
			
			query.setString(0, username);
			query.setString(1, md5passwd);
			
			datalist = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getCause());
			GsonUtil.OnjectToJson(map);
			return;
		}
		
		if(datalist.size() == 0) {
			map.put("success", false);
			map.put("msg", "用户名或者密码错误");
			
			GsonUtil.OnjectToJson(map);
			return;
		}
		Map<String, Object> app = Context.getApplication();
		Integer onlineNum = (Integer) app.get("OnlineNum");
		if(onlineNum == null){
			app.put("OnlineNum", 0);
		} else {
			onlineNum ++;
		}
		System.out.println((Integer) app.get("OnlineNum"));
		
		HttpSession htsession  = request.getSession();	
		htsession.setAttribute("username", username);
		map.put("success", true);
		map.put("msg", "登录成功");
    }
}
