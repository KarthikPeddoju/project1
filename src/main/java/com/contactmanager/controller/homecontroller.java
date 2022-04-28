	package com.contactmanager.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.event.PublicInvocationEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contactmanager.Messagegen.Message;
import com.contactmanager.entity.user;
import com.contactmanager.repo.userrepo;

@Controller
public class homecontroller {
	
	@Autowired
	private BCryptPasswordEncoder passwordencoder;
	@Autowired
	private userrepo ur;
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "ContactManager-Home");
		return "home";
		
	}
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "ContactManager-About");
		return "about";
		
	}
	@GetMapping("/signup")
	public String signup(Model model) {
		user u=new user();
		model.addAttribute("title", "ContactManager-SignUp");
		model.addAttribute("u", u);
		return "signup";
		
	}
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("u") user u,BindingResult result,@RequestParam(value="agre",defaultValue = "false") boolean agre,Model model,HttpSession session) {
		
		try {
			if(!agre) {
				System.out.println("Please check the box");
				System.out.println(result);
				throw new Exception();
				}
			if(result.hasErrors()) {
				model.addAttribute(u);
				System.out.println(result);
				return "signup";
			}
		
			u.setRole("ROLE_USER");
			u.setEnabled(true);
			u.setImgurl("default.jpg");
			u.setPassword(passwordencoder.encode(u.getPassword()));
			user saveduser = this.ur.save(u);
			System.out.println(agre);
			System.out.println(u);
			model.addAttribute("u",new user());
			session.setAttribute("msg", new Message("Successfully Registered!","alert-success" )); 
			return "signup";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("u",u);
		  session.setAttribute("msg",new Message("Something Went Wrong!"+e.getMessage(),"alert-danger"));
		  return "signup";
		}
	}
		@GetMapping("/signin")
		public String loginmade(Model model)
		{
			model.addAttribute("title", "ContactManger-Login");
			return "login";
		}
		
		

}
