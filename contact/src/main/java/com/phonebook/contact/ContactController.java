package com.phonebook.contact;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/contact")
public class ContactController
{
	
	@Autowired
	ContactDao dao;
	
	String tempId;
	
//	연락처 목록
	@GetMapping("/list")
	public String listContact(ContactDto dto, Model model, RedirectAttributes ra)
	{
		ra.addFlashAttribute("account_id", dto.getAccount_id());
		model.addAttribute("account_id", tempId);
		ArrayList<ContactDto> list;
		try
		{
			list = dao.getAll(tempId);
			
			model.addAttribute("listContact", list);
		} catch (Exception e)
		{
			e.printStackTrace();
			model.addAttribute("error", "연락처 목록 에러");
		}
		return "list";
	}
	
	
	@GetMapping("/addAccount")
	public String addAccount(@ModelAttribute ContactDto dto, Model model)
	{	
		String tempPw = "";
		String tempNiN = "";
		tempId = dto.getAccount_id();
		tempPw = dto.getAccount_pw();
		String re;
		try {
			if(dao.searchId(tempId))
			{
				model.addAttribute("erroMessage", "동일한 ID가 존재합니다");
				re = "register";
			} else
			{
				dao.addAccount(tempId, tempPw, tempNiN);
				re ="redirect:/contact/login";
			}
		} catch (Exception e) 
		{
			model.addAttribute("errorMessage", "회원가입 중 오류가 발생했습니다");
			re = "register";
			e.printStackTrace();
		}
		return re;
	}
	
	// 추가 메소드
		@GetMapping("/addContact/{account_id}")
		public String addContact(@ModelAttribute ContactDto dto, @PathVariable String account_id, Model model, RedirectAttributes ra)
		{			
			ra.addFlashAttribute("account_id", dto.getAccount_id());

			int temp = 1;
			if(dto.getGroupnm().equals("가족"))
			{
				temp = 1;
			} else if(dto.getGroupnm().equals("친구"))
			{
				temp = 2;
			} else if(dto.getGroupnm().equals("친구"))
			{
				temp = 3;
			} else
			{
				temp = 4;
			}
			
			try
			{
				dao.addContacts(account_id, dto.getName(), dto.getPhone(), dto.getAddress(), temp);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
			
			return "redirect:/contact/list";
		}
	
	// 성공 메소드
	@GetMapping("/editContact/{account_id}")
	public String editContact(@ModelAttribute ContactDto dto, @PathVariable String account_id, Model model, RedirectAttributes ra)
	{		
		ra.addFlashAttribute("account_id", dto.getAccount_id());
		int temp = 1;
		if(dto.getGroupnm().equals("가족"))
		{
			temp = 1;
		} else if(dto.getGroupnm().equals("친구"))
		{
			temp = 2;
		} else if(dto.getGroupnm().equals("친구"))
		{
			temp = 3;
		} else
		{
			temp = 4;
		}
		
		
		try
		{
			dao.updateContacts(dto.getContact_id(), dto.getName(), dto.getPhone(), dto.getAddress(), temp);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		return "redirect:/contact/list";
	}
	
	@GetMapping("/delContact/{contact_id}")
	public String delContact(@PathVariable int contact_id)
	{
		try 
		{
			dao.delContact(contact_id);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return "redirect:/contact/list";
	}
	
	@GetMapping("/loginWindow")
	public String loginWindow(Model model)
	{
		return "login";
	}
	
	@GetMapping("/login")
	public String login(@ModelAttribute ContactDto dto, Model model)
	{
		String tempPw = "";
		tempId = dto.getAccount_id();
		tempPw = dto.getAccount_pw();
		String re;
		try {
			if(dao.searchId(tempId))
			{
				if(dao.searchPw(tempPw))
				{
					re = "redirect:/contact/list";
				} else
				{
					model.addAttribute("errorMessage", "비밀번호가 존재하지 않습니다");
					re = "login";
				}
			} else
			{
				model.addAttribute("errorMessage", "ID가 존재하지 않습니다");
				re ="login";
			}
		} catch (Exception e) 
		{
			model.addAttribute("errorMessage", "로그인 중 오류가 발생했습니다");
			re = "login";
			e.printStackTrace();
		}
		return re;
	}
	
	
	@GetMapping("/registerWindow")
	public String registerWindow()
	{
		return "register";
	}
	
	
	
	
	
	
}
