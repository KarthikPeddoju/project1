package com.contactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contactmanager.entity.user;
import com.contactmanager.repo.userrepo;

public class userdetserviceimpl implements UserDetailsService {

	@Autowired
	private userrepo userepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		user u = userepo.getuserbyName(username);
		if(u==null)
		{
			throw new UsernameNotFoundException("user not found");
		}
		
		Customuserdetails customuserdetails = new Customuserdetails(u);
		return customuserdetails;
	}

}
