package com.contactmanager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import javax.xml.bind.PrintConversionEvent;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.contactmanager.Messagegen.Message;
import com.contactmanager.entity.Contact;
import com.contactmanager.entity.user;
import com.contactmanager.repo.contactsrepo;
import com.contactmanager.repo.userrepo;

@Controller
@RequestMapping("/user")
public class usercontroller {

	@Autowired
	private userrepo userepo;
	@Autowired
	private contactsrepo contactrepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@ModelAttribute
	public void grtcommomData(Model model, Principal principal) {
		String name = principal.getName();
		System.out.println(name);
		user u = userepo.getuserbyName(name);
		System.out.println(u);

		model.addAttribute("u", u);
	}

	@RequestMapping("/index")
	public String dashboard(Model model) {
		model.addAttribute("title", "ContactManager-UserDashboard");
		return "normal/dashboard";
	}

	@GetMapping("/addcontact")
	public String addcontacts(Model model) {
		model.addAttribute("title", "ContactManager-AddContacts");
		model.addAttribute("contact", new Contact());
		return "normal/contacsadd";
	}

	// processcontact added data

	@PostMapping("/processcontact")
	public String processcontact(@ModelAttribute("contact") Contact contact,
			@RequestParam("contactimage") MultipartFile file, Principal principal, HttpSession session) {

		try {
			String name = principal.getName();
			user u = userepo.getuserbyName(name);

			// uploading file

			if (file.isEmpty()) {
				System.out.println("no file choosen for profile");
				contact.setImage("default.jpg");
			} else {
				contact.setImage(file.getOriginalFilename());
				File file2 = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("uploaded image");

			}
			contact.setU(u);
			u.getContacts().add(contact);
			userepo.save(u);
			System.out.println(u);
			System.out.println(contact);
			session.setAttribute("msg", new Message("Contact saved Successfully!", "success"));

		} catch (Exception e) {

			System.out.println(e.getMessage());
			session.setAttribute("msg", new Message("Something Went Wrong!..Contact Not Saved", "danger"));
			e.printStackTrace();

		}
		return "normal/contacsadd";

	}

	@GetMapping("/showcontacts/{pageno}")
	public String getcontacts(@PathVariable("pageno") Integer pageno, Model model, Principal principal) {
		model.addAttribute("title", "ContactManager-showcontacts");

		String loogedemailname = principal.getName();
		user u = userepo.getuserbyName(loogedemailname);
		// currentpage
		// records per page

		org.springframework.data.domain.Pageable peagable = PageRequest.of(pageno, 5);
		Page<Contact> contacts = contactrepo.getcontactsbyUserId(u.getUid(), peagable);

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentpage", pageno);
		model.addAttribute("totalpages", contacts.getTotalPages());
		return "normal/showcontacts";

	}

	@GetMapping("/{cid}/contact")
	public String getcontact(@PathVariable("cid") Integer cid, Model model, Principal principal) {
		Optional<Contact> singlecontact = contactrepo.findById(cid);
		Contact contact1 = singlecontact.get();
		String name = principal.getName();
		user userr = this.userepo.getuserbyName(name);
		if (userr.getUid() == contact1.getU().getUid()) {
			model.addAttribute("contact", contact1);
			model.addAttribute("title", contact1.getCname());
		} else {
			model.addAttribute("title", "Dont have permission");
		}

		return "normal/singlecontactdetail";
	}

	@GetMapping("/delete/{cid}")
	public String deletecontact(@PathVariable("cid") Integer cid, Principal principal, Model model,
			HttpSession session) {
		Optional<Contact> findById = this.contactrepo.findById(cid);
		Contact contact = findById.get();
		String name = principal.getName();
		user userr = userepo.getuserbyName(name);
		if (userr.getUid() == contact.getU().getUid()) {

			userr.getContacts().remove(contact);
			this.userepo.save(userr);
			System.out.println("delete");
			this.contactrepo.delete(contact);
			session.setAttribute("msg", new Message("Contact Successfully deleted", "success"));
		}

		return "redirect:/user/showcontacts/0";

	}

	@PostMapping("/updatecontact/{cid}")
	public String update(@PathVariable("cid") Integer cid, Model model, Principal principal, HttpSession session) {
		try {
			Optional<Contact> findById = contactrepo.findById(cid);
			Contact contact = findById.get();
			user u = userepo.getuserbyName(principal.getName());
			if (u.getUid() == contact.getU().getUid()) {
				model.addAttribute("contact", contact);
			}
			return "normal/updatecontact";
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("msg", new Message("Something went wrong", "danger"));
			return "normal/showcontacts";
		}

	}

	// update total form
	@PostMapping("/processupdate")
	public String updatehandler(@ModelAttribute Contact contact, @RequestParam("contactimage") MultipartFile file,
			Model model, HttpSession session, Principal principal) {

		try {
			Contact oldcontact = this.contactrepo.findById(contact.getCid()).get();
			if (!file.isEmpty()) {
				// delete old photo
				File deletefile = new ClassPathResource("static/image").getFile();
				File file1 = new File(deletefile, oldcontact.getImage());
				file1.delete();
				// new photo
				File file2 = new ClassPathResource("static/image").getFile();

				Path path = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
			} else {
				contact.setImage(oldcontact.getImage());
			}

		} catch (Exception e) {

		}

		user u = userepo.getuserbyName(principal.getName());
		System.out.println(contact.getCid());
		contact.setU(u);

		this.contactrepo.save(contact);
		session.setAttribute("msg", new Message("Contact UPdated Successfully", "success"));

		return "redirect:/user/" + contact.getCid() + "/contact";
	}

	// user profile
	@GetMapping("/profile")
	public String userprofile(Model model) {

		model.addAttribute("title", "profile");
		return "normal/profile";
	}

	// update profile
	@PostMapping("/updateprofile")
	public String profileupdate() {

		return "normal/openprofile";
	}

	@PostMapping("/saveprofile")
	public String saveprofile(@ModelAttribute user us,@RequestParam("contactimage") MultipartFile file,Model model, HttpSession session, Principal principal) {

		try {
			user olduser = this.userepo.getuserbyName(principal.getName());
			if (!file.isEmpty()) {
				// delete old photo
				File deletefile = new ClassPathResource("static/image").getFile();
				File file1 = new File(deletefile, olduser.getImgurl());
				file1.delete(); // new photo
				File file2 = new ClassPathResource("static/image").getFile();

				Path path = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				olduser.setImgurl(file.getOriginalFilename());
				//olduser.setAbout(about);
				
				//userepo.save(u);
				session.setAttribute("msg", new Message("Profile Updated Successfully", "success"));
			} else {
				us.setImgurl("default.jpg");
				userepo.save(us);
			
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "normal/profile";
		}
		userepo.update(us.getEmail(), us.getUid(),us.getAbout(),us.getUname());
		return "normal/profile";
	}
	
	@GetMapping("/settings")
	public String settings(Model model) {
		model.addAttribute("title", "settings");
		return "normal/settings";
		}
	

	@PostMapping("/changepassword")
	public String changepassword(@RequestParam("oldp") String oldp,@RequestParam("newp") String newp,Principal principal,HttpSession session) {
		
		String name = principal.getName();
		user u = userepo.getuserbyName(name);
		if(bCryptPasswordEncoder.matches(oldp, u.getPassword()))
		{
			u.setPassword(this.bCryptPasswordEncoder.encode(newp));
			this.userepo.save(u);
			session.setAttribute("msg", new Message("Password changed successfully!","success"));
		}else {
			session.setAttribute("msg", new Message("Enter oldpassword correctly","danger"));
			return "redirect:/user/settings";
		}
		
		
		return "redirect:/user/index";
		
	}
	
	
	

}