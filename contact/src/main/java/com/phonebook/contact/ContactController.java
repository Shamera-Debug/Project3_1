package com.phonebook.contact;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contact")
public class ContactController
{

	@Autowired
	private ContactService contactService;

//	연락처 목록
	@GetMapping("/list")
	public String listContact(Model model)
	{
		try
		{
			model.addAttribute("nickname", contactService.searchNickname());
			model.addAttribute("account_id", contactService.getAccountId());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		model.addAttribute("listContact", contactService.getAllContacts());
		return "list";
	}

	@GetMapping("/addAccount")
	public String addAccount(@ModelAttribute ContactDto dto, Model model)
	{
		String serviceReturn = contactService.addAccount(dto);
		if (serviceReturn.equals("동일한 ID가 존재합니다"))
		{
			model.addAttribute("errorMessage", serviceReturn);
			return "register";
		} else if (serviceReturn.equals("redirect:/contact/loginWindow"))
		{
			return serviceReturn;
		} else
		{
			model.addAttribute("errorMessage", serviceReturn);
			return "register";
		}
	}

	// 추가 메소드
	@GetMapping("/addContact/{account_id}")
	public String addContact(@ModelAttribute ContactDto dto, @PathVariable String account_id, Model model,
			RedirectAttributes ra)
	{
		ra.addFlashAttribute("account_id", account_id);
		String serviceReturn = contactService.addContact(dto, account_id);

		if (serviceReturn.equals("연락처 추가 에러"))
		{
			model.addAttribute("errorMessage", serviceReturn);
			return "list";
		} else
		{
			return serviceReturn;
		}
	}

	@GetMapping("/editContact/{account_id}")
	public String editContact(@ModelAttribute ContactDto dto, @PathVariable String account_id, Model model,
			RedirectAttributes ra)
	{
		ra.addFlashAttribute("account_id", account_id);
		String serviceReturn = contactService.editContact(dto, account_id);

		if (serviceReturn.equals("연락처 수정 에러"))
		{
			model.addAttribute("errorMessage", serviceReturn);
			return "list";
		} else
		{
			return serviceReturn;
		}
	}

	@GetMapping("/editAccount/{account_id}")
	public String editAccount(@ModelAttribute ContactDto dto, @PathVariable String account_id, Model model)
	{
		String serviceReturn = contactService.editAccount(dto, account_id);

		if (serviceReturn.equals("계정 수정 에러"))
		{
			model.addAttribute("errorMessage", serviceReturn);
			return "list";
		} else
		{
			return serviceReturn;
		}
	}

	@GetMapping("/delContact/{contact_id}")
	public String delContact(@PathVariable int contact_id, Model model)
	{
		String serviceReturn = contactService.delContact(contact_id);

		if (serviceReturn.equals("삭제 에러"))
		{
			model.addAttribute("errorMessage", serviceReturn);
			return "list";
		} else
		{
			return serviceReturn;
		}
	}

	@GetMapping("/login")
	public String login(@ModelAttribute ContactDto dto, Model model)
	{
		String serviceReturn = contactService.login(dto);

		if (serviceReturn.equals("비밀번호가 일치하지 않습니다"))
		{
			model.addAttribute("errorMessage", serviceReturn);
			return "login";
		} else if (serviceReturn.equals("ID가 존재하지 않습니다"))
		{
			model.addAttribute("errorMessage", serviceReturn);
			return "login";
		} else if (serviceReturn.equals("로그인 에러"))
		{
			model.addAttribute("errorMessage", serviceReturn);
			return "login";
		} else
		{
			return serviceReturn;
		}
	}

	@GetMapping("/search")
	@ResponseBody
	public List<ContactDto> searchContact(@RequestParam String keyword)
	{
		return contactService.searchContact(keyword);
	}

	@GetMapping("/loginWindow")
	public String loginWindow()
	{
		return "login";
	}

	@GetMapping("/registerWindow")
	public String registerWindow()
	{
		return "register";
	}

}
