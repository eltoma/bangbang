package com.BangBang.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.BangBang.Util.HibernateUtil;

public class MissionInfoDAO {
	
	public static void main(String[] args) {
		System.out.println(new MissionInfoDAO().listMissionInfo(0,3,-1,"",-1));
		System.out.println(new MissionInfoDAO().getMissionInfoTotal(-1,"",-1));
	}
	
	public Integer getMissionInfoTotal(int relsuid, String q, int recvuid) {
		String sql = "	SELECT count(*) "
    			+ "FROM releasemission "
    			+ "WHERE userID = CASE WHEN ? < 0 THEN userID ELSE ? END "
    			+ "AND IFNULL(MissionRecUserID,-1) = CASE WHEN ? < 0 THEN IFNULL(MissionRecUserID,-1) ELSE ? END AND "
    			+ "MissionCop LIKE CONCAT('%',?,'%') ";
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Query query = session.createSQLQuery(sql);
			
			query.setInteger(0, relsuid);
			query.setInteger(1, relsuid);
			query.setInteger(2, recvuid);
			query.setInteger(3, recvuid);
			query.setString(4, q);
			
			Integer total = Integer.parseInt(query.list().get(0).toString());
			session.close();
			return total;
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
			return -1;
		}
		
	}
	public List<Map<String, Object>> listMissionInfo(int offset, int size, 
			int relsuid, String q, int recvuid){
		Session session = HibernateUtil.getSessionFactory().openSession();
		String sql = "	SELECT * "
    			+ "FROM releasemission "
    			+ "WHERE userID = CASE WHEN ? < 0 THEN userID ELSE ? END "
    			+ "AND IFNULL(MissionRecUserID,-1) = CASE WHEN ? < 0 THEN IFNULL(MissionRecUserID,-1) ELSE ? END AND "
    			+ "MissionCop LIKE CONCAT('%',?,'%') "
    			+ "ORDER BY MissionReleaseTime "
    			+ "LIMIT ?,?;";
		
		List datalist = null;
		try {
			Query query = session.createSQLQuery(sql);
			
			query.setInteger(0, relsuid);
			query.setInteger(1, relsuid);
			query.setInteger(2, recvuid);
			query.setInteger(3, recvuid);
			query.setString(4, q);
			query.setInteger(5, offset);
			query.setInteger(6, size);
			
			datalist = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			session.close();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
		}

		return datalist;
	}
}
