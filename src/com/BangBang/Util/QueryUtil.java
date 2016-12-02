package com.BangBang.Util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.BangBang.Util.HibernateUtil;


public class QueryUtil {

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> QueryBySql(String qrySql) {	
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		@SuppressWarnings("rawtypes")
		List sqlList = null;
		try {
			session.beginTransaction();
			sqlList = session.createSQLQuery(qrySql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			session.getTransaction().commit();
		} 
		catch (Exception e) {
			session.close();
			e.printStackTrace();
		}
		return sqlList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> QueryBySqlWithoutTrans(String qrySql) {	
		Session session = (Session) HibernateUtil.getSessionFactory().openSession();
		@SuppressWarnings("rawtypes")
		List sqlList = null;
		try {
			sqlList = session.createSQLQuery(qrySql)
							 .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
							 .list();
			session.close();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
		} finally {
			if( session.isOpen() ) { session.close(); }
		}
		
		return sqlList;
	}
	
	/**
	 * 用于执行更新和插入类存储过程，且需要从存储过过程返回执行结果
	 */
	public String ExecSqlWithScalarResu(String qrySql) {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SQLQuery sqlQuery = null; 
		String strRes = null;
		try {
			sqlQuery = session.createSQLQuery(qrySql);
			strRes = (String)sqlQuery.list().get(0);
			session.getTransaction().commit();
		} 
		catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			strRes = "0错误信息:" + e.getMessage();
		}
		return strRes;
	}
	
	/**
	 * 用于执行查询 总行数 的存储过程
	 */
	public Integer ExecSqlWithIntScalarResu(String qrySql) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		SQLQuery sqlQuery = null; 
		Integer strRes = null;
		try {
			sqlQuery = session.createSQLQuery(qrySql);
			strRes = (Integer)sqlQuery.list().get(0);
			session.getTransaction().commit();
		} 
		catch (Exception e) {
			session.close();
			e.printStackTrace();
			strRes = 0;
		}
		
		return strRes;
	}
	
	public BigDecimal ExecSqlWithBigDecScalarResu(String qrySql) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SQLQuery sqlQuery = null; 
		try {
			sqlQuery = session.createSQLQuery(qrySql);
		} 
		catch (Exception e) {
			e.printStackTrace();
			session.close();
		}
		
		BigDecimal strRes = (BigDecimal)sqlQuery.list().get(0);
		session.getTransaction().commit();
		return strRes;
	}
	
	// 执行更新语句,不能接收存储过程返回的执行结果
	public String ExecUpdateSql(String sql)
	{
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			SQLQuery query=session.createSQLQuery(sql);
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			return "0" + e.getMessage();
		}
		
		return "1操作成功!";
	}
}

