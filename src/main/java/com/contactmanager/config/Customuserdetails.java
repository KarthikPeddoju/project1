package com.contactmanager.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.contactmanager.entity.user;

public class Customuserdetails implements UserDetails{

	
	private user us;
	
	public Customuserdetails(user us) {
		super();
		this.us = us;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		
	SimpleGrantedAuthority sm = new SimpleGrantedAuthority(us.getRole());
	List<SimpleGrantedAuthority> simpleGrantedAuthorities=new ArrayList<>();
		
	simpleGrantedAuthorities.add(sm);
		return simpleGrantedAuthorities;
	}
	

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return us.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return us.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
