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
	
	String accountId;
	
//	연락처 목록
	@GetMapping("/list")
	public String listContact(ContactDto dto, Model model)
	{
		try 
		{
			model.addAttribute("nickname", dao.searchNickname(accountId));
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		model.addAttribute("account_id", accountId);
		ArrayList<ContactDto> list;
		try
		{
			list = dao.getAll(accountId);
			
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
		accountId = dto.getAccount_id();
		tempPw = dto.getAccount_pw();
		tempNiN = dto.getNickname();
		String re;
		try {
			if(dao.searchId(accountId))
			{
				model.addAttribute("errorMessage", "동일한 ID가 존재합니다");
				re = "register";
			} else
			{
				dao.addAccount(accountId, tempPw, tempNiN);
				re ="redirect:/contact/loginWindow";
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
		} else if(dto.getGroupnm().equals("직장"))
		{
			temp = 3;
		} else
		{
			temp = 4;
		}
		
		if (dto.getPhone().length() != 11)
		{
			model.addAttribute("errorMessage", "전화번호 11자리 모두 입력해주세요");
		} else if (dto.getPhone().contains(" "))
		{
			System.out.println("전화번호에 공백이 존재합니다!"); // 공백이 존재할때
		} else if (dto.getPhone().equals(""))
		{
			System.out.println("전화번호를 입력해주세요!"); // 입력값이 없을때
		} else if (dto.getPhone().matches(".*[^0-9].*"))
		{
			System.out.println("잘못된 번호입니다."); // 숫자외에 값이 들어올때
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
	
	@GetMapping("/editAccount/{account_id}")
	public String editAccount(ContactDto dto)
	{
		try 
		{
			dao.updateAccount(dto.getNickname(), dto.getAccount_pw(), dto.getAccount_id());
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return "redirect:/contact/loginWindow";
	}
	
	@GetMapping("/editAccountWindow")
	public String editAccountWindow(Model model)
	{
		try 
		{
			model.addAttribute("account_id", accountId);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return "editAccount";
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
		String tempId = "";
		String tempPw = "";
		
		tempId = dto.getAccount_id();
		tempPw = dto.getAccount_pw();
		String re;
		
		try {
			if(dao.searchId(tempId))
			{
				if(dao.searchPw(tempId, tempPw))
				{
					accountId = tempId;
					re = "redirect:/contact/list";
				} else
				{
					model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다");
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
