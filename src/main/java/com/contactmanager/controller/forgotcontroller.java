package com.contactmanager.controller;

import java.util.Random;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactmanager.Messagegen.Message;
import com.contactmanager.entity.user;
import com.contactmanager.repo.userrepo;
import com.contactmanager.service.mailservice;

@Controller
public class forgotcontroller {
	@Autowired
	private mailservice mailsservice;
	
	@Autowired
	private userrepo userepo;
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	Random random = new Random(1000);

	@GetMapping("/forgot")
	public String openemailform() {

		return "forgotpassword";

	}

	@PostMapping("/sendotp")
	public String sendotp(@RequestParam("email") String email, HttpSession session) {

		int otp = random.nextInt(99999);
		String subject = "Contact Manager";
		String message = "Please verify this OTP=" + otp ;
		String to = email;
		boolean f = mailsservice.sendmail(subject, message, to);

		if (f) {
			session.setAttribute("email", email);
			session.setAttribute("otp", otp);
			return "verifyotp";
		} else {
			session.setAttribute("msg", "check your mail ID");
			return "forgotpassword";
		}

	}
	@PostMapping("/verify-otp")
	
	public String  verify(@RequestParam("otp") Integer otp,HttpSession session) {
		
		int otpsend=(int)session.getAttribute("otp");
		String emailsend=(String) session.getAttribute("email");
		if(otpsend==otp) {
			
			user u = userepo.getuserbyName(emailsend);
		if(u==null)
		{
			session.setAttribute("msg", "User with this Email Does Not Exist");
			return "forgotpassword";
			
		}else {
			
		}
			return "changepasswordform";
		}
		else {
			session.setAttribute("msg", "You Are Entered Invalid OTP");
			return "verifyotp";
		}
		
	}
	@PostMapping("/changepassword")
	public  String  changepassword(@RequestParam("npass") String npass,HttpSession session) {
		String email=(String) session.getAttribute("email");
		user u = userepo.getuserbyName(email);
		u.setPassword(bcrypt.encode(npass));
		this.userepo.save(u);
	
		return "redirect:/signin?change=password changed successfully	";
		
	}
}
