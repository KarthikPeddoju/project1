package com.contactmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanager.entity.Contact;
import com.contactmanager.entity.user;
import com.contactmanager.repo.contactsrepo;
import com.contactmanager.repo.userrepo;

@RestController
public class searchcontroller {

	
	
	
@Autowired
	private userrepo userepo;
@Autowired
	private contactsrepo contactrepo;

	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(Principal principal,@PathVariable("query") String query) {
		System.out.println(query);
		user u = userepo.getuserbyName(principal.getName());
		List<Contact> contacts = contactrepo.findByCnameContainingAndU(query, u);
		return ResponseEntity.ok(contacts);
	}
	
}
