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

import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/contact")
public class ContactController
{
	
	@Autowired
	ContactDao dao;
	
//	연락처 목록
	@GetMapping("/list")
	public String listContact(Model model)
	{
		ArrayList<ContactDto> list;

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
	public String addAccount(@ModelAttribute ContactDto dto, Model model, HttpServletRequest req, @RequestParam(value="error", required=false) String error)
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
	
	@GetMapping("/editContact/{contact_id}")
	public String editContact(ContactDto dto, @PathVariable int contact_id, Model model)
	{	
//		model.addAttribute("account_id", dto.getAccount_id());
		
		try
		{
			dao.updateContacts(contact_id, dto.getName(), dto.getPhone());
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
		
		model.addAttribute("account_id", dto.getAccount_id());
		listContact(model);
		return "list";
	}
	
	
	@GetMapping("/registerWindow")
	public String registerWindow()
	{
		return "register";
	}
	
	
	// 생성 추가 실험 v1
	
	
}
