package com.phonebook.contact;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;


@Repository
public class ContactDao
{
	private final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private final String USERID = "ora_user";
	private final String USERPW = "1234";
	
//	Connection 생성 open()
	private Connection open()
	{
		Connection conn = null;
		try
		{
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USERID, USERPW);
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return conn;
	}
	
//	계정 추가 메소드 
	public void addAccount(String account_id, String account_pw, String nickname) throws Exception
	{
		Connection conn 		= open();
		String sql = "insert into account(account_id, ACCOUNT_PW, NICKNAME)	"
				   + " values('"+account_id+"', '"+account_pw+"', '"+nickname+"')										";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		try(conn;pstmt) 
		{			
			pstmt.executeUpdate();
		}
	}
	
//	계정 수정 메소드 
	public void updateAccount(String nickname, String account_pw, String account_id) throws Exception
	{
		Connection conn 		= open();
		String sql = "UPDATE ACCOUNT SET nickname = '"+nickname+"', account_pw = '"+account_pw+"' 	WHERE account_id = '"+account_id+"'";
		
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		try(conn;pstmt) 
		{
			pstmt.executeUpdate();
		}
	}
	
//	연락처 추가 메소드 
	public void addContacts(String account_id, String name, String phone, String address, int groupno) throws Exception
	{
		Connection conn 		= open();
		String sql = "INSERT INTO CONTACT (contact_id, ACCOUNT_ID ,NAME ,PHONE ,ADDRESS , GROUPNO )"
				+	 "VALUES ((SELECT nvl(max(CONTACT_ID),0)+1 FROM CONTACT),'"+account_id+"','"+name+"','"+phone+"','"+address+"','"+groupno+"')";
		

		PreparedStatement pstmt = conn.prepareStatement(sql);
		try(conn;pstmt) 
		{
			pstmt.executeUpdate();
		}
	}
	
//	연락처 수정 메소드 
	public void updateContacts(int contact_id, String name, String phone, String address, int groupno) throws Exception
	{
		Connection conn 		= open();
		String sql = "UPDATE CONTACT SET name = '"+name+"', phone = '"+phone+"', address = '"+address+"', groupno = '"+groupno+"'	WHERE contact_id = '"+contact_id+"'";
		
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		try(conn;pstmt) 
		{
			pstmt.executeUpdate();
		}
	}

	
//	전체 목록 getAll() : parameter x
	public ArrayList<ContactDto> getAll(String account_id) throws Exception
	{
		Connection conn = open();
		String sql = "SELECT c.contact_id, c.name, c.phone, c.address, g.groupnm, TO_CHAR(c.contact_regdt, 'YYYY-MM-DD') AS contact_regdt  	"
				+	 "FROM contact c, cgroup g										"
				+	 "WHERE c.groupno = g.groupno AND c.account_id = '"+account_id+"'	";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		ArrayList<ContactDto> contactList = new ArrayList<>();
		
		// 데이터베이스 select => list
		ResultSet rs = pstmt.executeQuery();
		
		try(conn; pstmt; rs)
		{
			while(rs.next())
			{
				// 한개의 뉴스 저장
				ContactDto dto = new ContactDto();
				dto.setContact_id(rs.getInt("contact_id"));
				dto.setName(rs.getString("name"));
				dto.setPhone(rs.getString("phone"));
				dto.setAddress(rs.getString("address"));
				dto.setGroupnm(rs.getString("groupnm"));
				dto.setContact_regdt(rs.getString("contact_regdt"));
				
				contactList.add(dto);
			}
		}
		return contactList;
	}
	 // end getAll()
	
//	
////	뉴스 상세 => getNews(newsid), return 한개의 뉴스(NewsDto)
//	public NewsDto getNews(int newsid) throws Exception
//	{
//		NewsDto dto = new NewsDto();
//		Connection conn = open();
//		String sql = "select * from news where newsid = '"+newsid+"' ";
//		PreparedStatement pstmt = conn.prepareStatement(sql);
//		ResultSet rs = pstmt.executeQuery();
//		
//		rs.next();
//		dto.setNewsid(rs.getInt("newsid"));
//		dto.setTitle(rs.getString("title"));
//		dto.setImg(rs.getString("img"));
//		dto.setRegdt(rs.getDate("regdt"));
//		dto.setContent(rs.getString("content"));
//		
//		return dto;
//	}
//	
////	뉴스 삭제 => return X : void
////	newsid : 뉴스의 고유 번호, 1씩 증가
	public void delContact(int contact_id) throws Exception
	{
		Connection conn = open();
		String sql = "delete from contact where contact_id = " + contact_id;
		PreparedStatement pstmt = conn.prepareStatement(sql);	
		
		try(conn;pstmt)
		{
			if(pstmt.executeUpdate() == 0)
			{
				throw new SQLException("DB error");
			}
		}
	}
	
	public boolean searchId(String account_id) throws Exception
	{
		Connection conn = open();
		String sql = "SELECT ACCOUNT_ID FROM ACCOUNT WHERE ACCOUNT_ID = '"+account_id+"'  ";
		PreparedStatement pstmt = conn.prepareStatement(sql);	
		
		ResultSet rs = pstmt.executeQuery();
		
		try(conn; pstmt; rs)
		{
			if(rs.next())
			{
				return true;
			} else
			{
				return false;
			}
		}
	}
	
	public boolean searchPw(String account_id, String account_pw) throws Exception
	{
		Connection conn = open();
		String sql = "SELECT * FROM ACCOUNT WHERE ACCOUNT_ID = '"+account_id+"' AND ACCOUNT_PW = '"+account_pw+"'  ";
		PreparedStatement pstmt = conn.prepareStatement(sql);	
		
		ResultSet rs = pstmt.executeQuery();
		
		try(conn; pstmt; rs)
		{
			if(rs.next())
			{
				return true;
			} else
			{
				return false;
			}
		}
	}
	
	public String searchNickname(String account_id) throws Exception
	{
		Connection conn = open();
		String sql = "SELECT NICKNAME FROM ACCOUNT WHERE ACCOUNT_ID = '"+account_id+"'  ";
		PreparedStatement pstmt = conn.prepareStatement(sql);	
		
		ResultSet rs = pstmt.executeQuery();
		String nickname = "";
		
		try(conn; pstmt; rs)
		{
			while(rs.next()) 
			{
				nickname = rs.getString("nickname");
			}
		}
		return nickname;
	}
	
	public ArrayList<ContactDto> searchContact(String account_id, String keyword) throws Exception
	{
		Connection conn = open();
		String sql = "";
		if(keyword.contains("010"))
		{
			sql = "SELECT c.contact_id, c.name, c.phone, c.address, g.groupnm, TO_CHAR(c.contact_regdt, 'YYYY-MM-DD') AS contact_regdt  	 "
					+	 "FROM contact c, cgroup g																								 "
					+	 "WHERE c.groupno = g.groupno AND c.account_id = '"+account_id+"'	AND c.phone LIKE '%"+keyword+"%'					 ";
		} else
		{
			sql = "SELECT c.contact_id, c.name, c.phone, c.address, g.groupnm, TO_CHAR(c.contact_regdt, 'YYYY-MM-DD') AS contact_regdt  	 "
					+	 "FROM contact c, cgroup g																								 "
					+	 "WHERE c.groupno = g.groupno AND c.account_id = '"+account_id+"'	AND c.name LIKE '%"+keyword+"%'						 ";
		}
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		ArrayList<ContactDto> contactList = new ArrayList<>();
		
		// 데이터베이스 select => list
		ResultSet rs = pstmt.executeQuery();
		
		try(conn; pstmt; rs)
		{
			while(rs.next())
			{
				// 한개의 뉴스 저장
				ContactDto dto = new ContactDto();
				dto.setContact_id(rs.getInt("contact_id"));
				dto.setName(rs.getString("name"));
				dto.setPhone(rs.getString("phone"));
				dto.setAddress(rs.getString("address"));
				dto.setGroupnm(rs.getString("groupnm"));
				dto.setContact_regdt(rs.getString("contact_regdt"));
				
				contactList.add(dto);
			}
		}
		return contactList;
	}
}
