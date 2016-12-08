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

import com.BangBang.DAO.MissionInfoDAO;
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
	
	/********************************************************************
	 *                       用户信息相关接口(开始)
	 *******************************************************************/
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
		String sqladd = "INSERT INTO userinfo(userName, password, phoneNumber,score) VALUES( ?, ?, ?,50)";
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		
		String md5passwd = CipherUtil.generatePassword(password);
		
		try {
			session.beginTransaction();
			Query query = session.createSQLQuery(sqlexist);
			query.setString(0, username);
			
			@SuppressWarnings("rawtypes")
			List list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			if(list.size() != 0) {
				map.put("success", false);
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
			map.put("msg", e.getCause().toString());
			GsonUtil.OnjectToJsonP(map);
			return;
		}
		
		map.put("success", true);
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
		if(username.equals("")){
			map.put("success", false);
	    	map.put("msg", "用户名字段不能为空");
	    	
	    	GsonUtil.OnjectToJsonP(map);
	    	return;
		}
    	
    	String password = strNullCheck(request.getParameter("password"));
		String phoneNumber = strNullCheck(request.getParameter("phoneNumber"));
    	
		
		
		String md5passwd = CipherUtil.generatePassword(password);
    	String sql = "{call u_userinfo('"
    			+ username +"','"
    			+ md5passwd +"','"
    			+ phoneNumber +  "')}";
    	
    	String res = QueryUtil.ExecUpdateSql(sql);
    	map.put("success", res.startsWith("1"));
    	map.put("msg", res.substring(1));
    	GsonUtil.OnjectToJsonP(map);
    }
    
    
  
    
	/****用户信息相关接口(结束)**********************************************/
    
    /*********************************************************************
     *                   空参数检验函数
     *********************************************************************/
    private String strNullCheck(String src){
    	return (src ==null)?"":src;
    }
    
    private int intIsNull(String str, int rpl) {
    	return (str == null || str == "")? rpl : Integer.parseInt(str); 
    }
    private String strIsNull(String str, String rpl) {
    	return (str == null || str == "")? rpl : str; 
    }   
    
    
	/********************************************************************
	 *                       任务信息相关接口(开始)
	 ********************************************************************/
    public void listMissions() {
		String MissionCop = strNullCheck(request.getParameter("MissionCop"));
		String MissionStatus = strNullCheck(request.getParameter("MissionStatus"));
		String MissionRecerName = strNullCheck(request.getParameter("MissionRecerName"));
		
		int offset = intIsNull(request.getParameter("offset"), 0);
		int size = intIsNull(request.getParameter("size"), 3);
		int relsuid = intIsNull(request.getParameter("relsuid"), -1);
		int recvuid = intIsNull(request.getParameter("recvuid"), -1);
		int hasExpired = intIsNull(request.getParameter("hasExpired"), 0);
		
    	MissionInfoDAO missionInfoDAO = new MissionInfoDAO();
    	List<Map<String, Object>> datalist = null;
    	Integer total = 0;
    	
    	try {
    		 datalist = missionInfoDAO.listMissionInfo(offset, size, hasExpired, relsuid, recvuid, 
    				 MissionCop, MissionStatus, MissionRecerName);
    		 total = missionInfoDAO.getMissionInfoTotal(hasExpired, relsuid, recvuid, 
    				 MissionCop, MissionStatus, MissionRecerName);
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", "查询时出错");
			GsonUtil.OnjectToJsonP(map);
			return;
		}
    	map.put("success", true);
    	map.put("rows", datalist);
    	map.put("total", total);
    	
    	GsonUtil.OnjectToJsonP(map);
    }
    /****任务信息相关接口(结束)**********************************************/
    
    
    
    /**
     * ====================================hao==================================================
     */  
    /**
     * 任务发布 数据插入
     */
    public void mission_I_bangbang()
    {
    	String username = strNullCheck(request.getParameter("username"));
		String MissionRcecerName = strNullCheck(request.getParameter("MissionRcecerName"));
		String MissionRecerPhone = strNullCheck(request.getParameter("MissionRecerPhone"));
		String MissionCop = strNullCheck(request.getParameter("MissionCop"));
		String MissionCA = strNullCheck(request.getParameter("MissionCA"));
		String giveScore = strNullCheck(request.getParameter("giveScore"));
		String BeTime = strNullCheck(request.getParameter("BeTime"));
		
		String sql = "call ZH_I_mission ('"+username+"','"+MissionRcecerName+"','"+MissionRecerPhone+"','"+MissionCop+"','"+MissionCA+"',"+giveScore+",'"+BeTime +"')";
		try {
			QueryUtil.ExecUpdate(sql);
			map.put("success", true);
			map.put("msg", "发布成功！");
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", "错误信息:"+e.getCause());
		}
		GsonUtil.OnjectToJsonP(map);
    }
    
    /**
     * 任务接单
     */
    public void mission_u_jiedan()
    {
    	String username = strNullCheck(request.getParameter("username"));//接单人
		String MissionNo = strNullCheck(request.getParameter("MissionNo"));//任务编号
		String sql = "call ZH_U_jiedan ('"+username+"','"+MissionNo+"')";
		try {
			QueryUtil.ExecUpdate(sql);
			map.put("success", true);
			map.put("msg", "接单成功！");
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", "错误信息:"+e.getCause());
		}
		GsonUtil.OnjectToJsonP(map);
    }
    
    /**
     * 任务完成确认
     */
    public void mission_u_confirm()
    {
		String MissionNo = strNullCheck(request.getParameter("MissionNo"));//任务编号
		String sql = "call ZH_U_confirm ('"+MissionNo+"')";
		try {
			QueryUtil.ExecUpdate(sql);
			map.put("success", true);
			map.put("msg", "确认成功！");
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", "错误信息:"+e.getCause());
		}
		GsonUtil.OnjectToJsonP(map);
    }

    /**
     * ====================================hao==================================================
     */ 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}

