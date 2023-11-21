package com.phonebook.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService
{
	@Autowired
	ContactDao dao;

	String accountId;

	public String searchNickname()
	{
		try
		{
			return dao.searchNickname(accountId);
		} catch (Exception e)
		{
			e.printStackTrace();
			return "닉네임 찾는중 에러 발생";
		}
	}

	public String getAccountId()
	{
		return accountId;
	}

	public ArrayList<ContactDto> getAllContacts()
	{
		ArrayList<ContactDto> list = null;
		try
		{
			list = dao.getAll(accountId);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

	public String addAccount(ContactDto dto)
	{
		try
		{
			if (dao.searchId(dto.getAccount_id()))
			{
				return "동일한 ID가 존재합니다";
			} else
			{
				dao.addAccount(dto.getAccount_id(), dto.getAccount_pw(), dto.getNickname());
				return "redirect:/contact/loginWindow";
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return "계정추가 에러";
		}

	}

	public String addContact(ContactDto dto, String account_id)
	{
		int temp = 0;
		if (dto.getGroupnm().equals("가족"))
		{
			temp = 1;
		} else if (dto.getGroupnm().equals("친구"))
		{
			temp = 2;
		} else if (dto.getGroupnm().equals("직장"))
		{
			temp = 3;
		} else
		{
			temp = 4;
		}

		try
		{
			dao.addContacts(account_id, dto.getName(), dto.getPhone(), dto.getAddress(), temp);
			return "redirect:/contact/list";
		} catch (Exception e)
		{
			e.printStackTrace();
			return "연락처 추가 에러";
		}
	}

	public String editContact(ContactDto dto, String account_id)
	{
		int temp = 0;
		if (dto.getGroupnm().equals("가족"))
		{
			temp = 1;
		} else if (dto.getGroupnm().equals("친구"))
		{
			temp = 2;
		} else if (dto.getGroupnm().equals("직장"))
		{
			temp = 3;
		} else
		{
			temp = 4;
		}

		try
		{
			dao.updateContacts(dto.getContact_id(), dto.getName(), dto.getPhone(), dto.getAddress(), temp);
			return "redirect:/contact/list";
		} catch (Exception e)
		{
			e.printStackTrace();
			return "연락처 수정 에러";
		}
	}

	public String editAccount(ContactDto dto, String account_id)
	{
		try
		{
			dao.updateAccount(dto.getNickname(), dto.getAccount_pw(), account_id);
			return "redirect:/contact/loginWindow";
		} catch (Exception e)
		{
			e.printStackTrace();
			return "계정 수정 에러";
		}
	}

	public String delContact(int contact_id)
	{
		try
		{
			dao.delContact(contact_id);
			return "redirect:/contact/list";
		} catch (Exception e)
		{
			e.printStackTrace();
			return "삭제 에러";
		}
	}

	public String login(ContactDto dto)
	{
		try
		{
			if (dao.searchId(dto.getAccount_id()))
			{
				if (dao.searchPw(dto.getAccount_id(), dto.getAccount_pw()))
				{
					accountId = dto.getAccount_id();
					return "redirect:/contact/list";
				} else
				{
					return "비밀번호가 일치하지 않습니다";
				}
			} else
			{
				return "ID가 존재하지 않습니다";
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return "로그인 에러";
		}
	}

	public List<ContactDto> searchContact(String keyword)
	{
		try
		{
			List<ContactDto> searchResults = dao.searchContact(accountId, keyword);
			return searchResults;
		} catch (Exception e)
		{
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

}
