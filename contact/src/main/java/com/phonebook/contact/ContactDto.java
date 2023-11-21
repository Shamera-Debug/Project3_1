package com.phonebook.contact;

import java.sql.Date;



public class ContactDto
{
//	account 테이블
	String account_id;
	String account_pw;
	String nickname;
	Date account_regdt;
	
//	contact 테이블
	int contact_id;
	String name;
	String phone;
	String address;
	String contact_regdt;
	Date updatedt;
	
//	cgroup 테이블
	int groupno;
	String groupnm;
	
//	검색
	String keyword;
	String searchType;
	
	
	public String getKeyword()
	{
		return keyword;
	}

	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}

	public String getSearchType()
	{
		return searchType;
	}

	public void setSearchType(String searchType)
	{
		this.searchType = searchType;
	}

	public void clearAccount() 
	{
		account_id = "";
		account_pw = "";
	}
	
//	getter setter 설정
	public String getAccount_id()
	{
		return account_id;
	}
	public void setAccount_id(String account_id)
	{
		this.account_id = account_id;
	}
	public String getAccount_pw()
	{
		return account_pw;
	}
	public void setAccount_pw(String account_pw)
	{
		this.account_pw = account_pw;
	}
	public String getNickname()
	{
		return nickname;
	}
	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
	public Date getAccount_regdt()
	{
		return account_regdt;
	}
	public void setAccount_regdt(Date account_regdt)
	{
		this.account_regdt = account_regdt;
	}
	public int getContact_id()
	{
		return contact_id;
	}
	public void setContact_id(int contact_id)
	{
		this.contact_id = contact_id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getContact_regdt()
	{
		return contact_regdt;
	}
	public void setContact_regdt(String contact_regdt)
	{
		this.contact_regdt = contact_regdt;
	}
	public Date getUpdatedt()
	{
		return updatedt;
	}
	public void setUpdatedt(Date updatedt)
	{
		this.updatedt = updatedt;
	}
	public int getGroupno()
	{
		return groupno;
	}
	public void setGroupno(int groupno)
	{
		this.groupno = groupno;
	}
	public String getGroupnm()
	{
		return groupnm;
	}
	public void setGroupnm(String groupnm)
	{
		this.groupnm = groupnm;
	}
	
	
}
