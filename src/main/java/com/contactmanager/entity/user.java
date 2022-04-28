package com.contactmanager.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.aspectj.weaver.tools.Trace;



@Entity

public class user {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int uid;
	@NotBlank(message = "Name field is required!")
	@Size(min = 2,max = 20,message = "Min 2 and Max 20 characters needed")
	private String uname;
	@Column(unique = true)
	@NotBlank(message = "Email should not be empty!")
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
	private String email;
	@NotBlank(message = "Password should not be empty!")
	private String password;
	private String role;
	private boolean enabled;
	private String imgurl;
	@Column(length = 500)
	private String about;
	
	@OneToMany(cascade =CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "u",orphanRemoval = true)
	private List<Contact> contacts=new ArrayList<>();
	
	public List<Contact> getContacts() {
		return contacts;
	}


	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}


	public int getUid() {
		return uid;
	}


	public void setUid(int uid) {
		this.uid = uid;
	}


	public String getUname() {
		return uname;
	}


	public void setUname(String uname) {
		this.uname = uname;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public String getImgurl() {
		return imgurl;
	}


	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}


	public String getAbout() {
		return about;
	}


	public void setAbout(String about) {
		this.about = about;
	}


	@Override
	public String toString() {
		return "user [uid=" + uid + ", uname=" + uname + ", email=" + email + ", password=" + password + ", role="
				+ role + ", enabled=" + enabled + ", imgurl=" + imgurl + ", about=" + about + ", contacts=" + contacts
				+ "]";
	}
	
	
	

}
