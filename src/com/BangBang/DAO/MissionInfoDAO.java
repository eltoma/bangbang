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
		System.out.println(new MissionInfoDAO().listMissionInfo(0, 100, 1, -1, -1, "", "待接单", "","154546"));
		System.out.println(new MissionInfoDAO().getMissionInfoTotal(1, -1, -1, "", "待接单", "", "154546"));
	}
	
	public Integer getMissionInfoTotal(int hasExpired,int relsuid, int recvuid, 
			String MissionCop, String MissionStatus, String MissionRecerName, 
			String MissionCA) {
		String sql = " SELECT COUNT(*) "
				   + " FROM releasemission "
				   + " WHERE (MissionDeadline >= NOW() OR ?) "
				   + " AND userID = CASE WHEN ? < 0 THEN userID ELSE ? END "
				   + " AND IFNULL(MissionRecUserID,-1) = CASE WHEN ? < 0 THEN IFNULL(MissionRecUserID,-1) ELSE ? END "
				   + " AND MissionCop LIKE CONCAT('%',?,'%') "
				   + " AND MissionStatus LIKE CONCAT('%',?,'%') "
				   + " AND MissionRecerName LIKE CONCAT('%',?,'%') "
				   + " AND MissionCA LIKE CONCAT('%',?,'%') ";
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Query query = session.createSQLQuery(sql);
			
			query.setInteger(0, hasExpired);
			query.setInteger(1, relsuid);
			query.setInteger(2, relsuid);
			query.setInteger(3, recvuid);
			query.setInteger(4, recvuid);

			
			query.setString(5, MissionCop);
			query.setString(6, MissionStatus);
			query.setString(7, MissionRecerName);
			query.setString(8, MissionCA);
			
			Integer total = Integer.parseInt(query.list().get(0).toString());
			session.close();
			return total;
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
			return -1;
		}
		
	}
	

	
	public List<Map<String, Object>> listMissionInfo(int offset, int size, int hasExpired,
			int relsuid, int recvuid, String MissionCop, String MissionStatus, 
			String MissionRecerName, String MissionCA){
		Session session = HibernateUtil.getSessionFactory().openSession();
		String sql = " SELECT * "
				   + " FROM releasemission "
				   + " WHERE (MissionDeadline >= NOW() OR ?) "
				   + " AND userID = CASE WHEN ? < 0 THEN userID ELSE ? END "
				   + " AND IFNULL(MissionRecUserID,-1) = CASE WHEN ? < 0 THEN IFNULL(MissionRecUserID,-1) ELSE ? END "
				   + " AND MissionCop LIKE CONCAT('%',?,'%') "
				   + " AND MissionStatus LIKE CONCAT('%',?,'%') "
				   + " AND MissionRecerName LIKE CONCAT('%',?,'%') "
				   + " AND MissionCA LIKE CONCAT('%',?,'%') "
				   + " ORDER BY MissionReleaseTime DESC "
				   + " LIMIT ?,?; ";
		
		List datalist = null;
		try {
			Query query = session.createSQLQuery(sql);
			query.setInteger(0, hasExpired);
			query.setInteger(1, relsuid);
			query.setInteger(2, relsuid);
			query.setInteger(3, recvuid);
			query.setInteger(4, recvuid);
			query.setInteger(9, offset);
			query.setInteger(10, size);
			
			query.setString(5, MissionCop);
			query.setString(6, MissionStatus);
			query.setString(7, MissionRecerName);
			query.setString(8, MissionCA);
			
			datalist = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			session.close();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
		}

		return datalist;
	}
}
