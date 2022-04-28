package com.contactmanager.repo;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.contactmanager.entity.Contact;
import com.contactmanager.entity.user;

@Repository
public interface contactsrepo extends JpaRepository<Contact, Integer>{
  @Query("select c from Contact c where c.u.uid=:userid")
	public Page<Contact> getcontactsbyUserId(@Param("userid") int userid,org.springframework.data.domain.Pageable pePageable); 
	//pegable daggara how many records per page and current page
  
  public List<Contact> findByCnameContainingAndU(String cname,user u) ;

}
