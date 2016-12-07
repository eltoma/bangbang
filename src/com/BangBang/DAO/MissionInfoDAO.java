package com.BangBang.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.BangBang.Util.HibernateUtil;

public class MissionInfoDAO {
	

	
	public Integer getMissionInfoTotal(int hasExpired,int relsuid, int recvuid, 
			String MissionCop, String MissionStatus, String userName) {
		String sql = " SELECT COUNT(*) "
				   + " FROM releasemission a "
				   + " LEFT JOIN userinfo b ON a.userID = b.userID "
				   + " WHERE (MissionDeadline >= NOW() OR ?) "
				   + " AND b.userID = CASE WHEN ? < 0 THEN b.userID ELSE ? END "
				   + " AND IFNULL(MissionRecUserID,-1) = CASE WHEN ? < 0 THEN IFNULL(MissionRecUserID,-1) ELSE ? END "
				   + " AND MissionCop LIKE CONCAT('%',?,'%') "
				   + " AND MissionStatus LIKE CONCAT('%',?,'%') "
				   + " AND userName LIKE CONCAT('%',?,'%') "
				   + " ORDER BY MissionReleaseTime DESC ";
		
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
			query.setString(7, userName);
			
			Integer total = Integer.parseInt(query.list().get(0).toString());
			session.close();
			return total;
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public static void main(String[] args) {
		System.out.println(new MissionInfoDAO().listMissionInfo(0, 100, 1, -1, -1, "", "", ""));
		System.out.println(new MissionInfoDAO().getMissionInfoTotal(1, -1, -1, "", "", ""));
	}
	
	public List<Map<String, Object>> listMissionInfo(int offset, int size, int hasExpired,
			int relsuid, int recvuid, String MissionCop, String MissionStatus, String userName){
		Session session = HibernateUtil.getSessionFactory().openSession();
		String sql = " SELECT a.*, "
				   + "b.userName FROM releasemission a "
				   + "LEFT JOIN userinfo b ON a.userID = b.userID "
				   + "WHERE (MissionDeadline >= NOW() OR ?) "
				   + "AND b.userID = CASE WHEN ? < 0 THEN b.userID ELSE ? END "
				   + "AND IFNULL(MissionRecUserID,-1) = CASE WHEN ? < 0 THEN IFNULL(MissionRecUserID,-1) ELSE ? END "
				   + "AND MissionCop LIKE CONCAT('%',?,'%') "
				   + "AND MissionStatus LIKE CONCAT('%',?,'%') "
				   + "AND userName LIKE CONCAT('%',?,'%') "
				   + "ORDER BY MissionReleaseTime DESC "
				   + "LIMIT ?,?; ";
		
		List datalist = null;
		try {
			Query query = session.createSQLQuery(sql);
			query.setInteger(0, hasExpired);
			query.setInteger(1, relsuid);
			query.setInteger(2, relsuid);
			query.setInteger(3, recvuid);
			query.setInteger(4, recvuid);
			query.setInteger(8, offset);
			query.setInteger(9, size);
			
			query.setString(5, MissionCop);
			query.setString(6, MissionStatus);
			query.setString(7, userName);
			
			System.out.println(query.getQueryString());
			datalist = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			session.close();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
		}

		return datalist;
	}
}
