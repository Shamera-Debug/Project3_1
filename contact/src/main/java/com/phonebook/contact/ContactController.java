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
	
//	연락처 목록
	@GetMapping("/list")
	public String listContact(@ModelAttribute ContactDto dto, Model model)
	{
		ArrayList<ContactDto> list;
//		model.addAttribute("account_id", dto.getAccount_id());
		try
		{
			list = dao.getAll();
			
			model.addAttribute("listContact", list);
		} catch (Exception e)
		{
			e.printStackTrace();
			model.addAttribute("error", "연락처 목록 에러");
		}
		return "list";
	}
	
	
	@PostMapping("/addAccount")
	public String addAccount(@ModelAttribute ContactDto dto, Model model, HttpServletRequest req)
	{	
		ArrayList<ContactDto> accountList;
		try
		{
			accountList = dao.searchId(dto.getAccount_id());
			if(accountList.size()>0)
			{
				req.setAttribute("error", "중복");
				return "redirect:/contact/registerWindow";
			}else
			{
				dao.addAddress(dto);
			}	
			
		} catch (Exception e)
		{
			e.printStackTrace();
			model.addAttribute("error", "존재하는 ID");
		}
		return "redirect:/contact/list";
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
	@GetMapping("/editContact/{account_id}/{contact_id}")
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
	
	@GetMapping("/loginWindow")
	public String loginWindow(Model model)
	{
		return "login";
	}
	
	@GetMapping("/login")
	public String login(@ModelAttribute ContactDto dto, RedirectAttributes ra)
	{
		ra.addFlashAttribute("account_id", dto.getAccount_id());
//		listContact(dto, model);
		return "redirect:/contact/list";
	}
	
	
	@GetMapping("/registerWindow")
	public String registerWindow()
	{
		return "register";
	}
	
	
	
}
