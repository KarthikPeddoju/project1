package com.contactmanager.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.contactmanager.entity.user;

@Repository
public interface userrepo extends JpaRepository<user, Integer>{
  @Query("select u from user u where u.email=:email")
	public user getuserbyName(@Param("email") String email); 
  @Transactional
  @Modifying
  @Query("UPDATE user u SET u.email = :email,u.about= :about,u.uname=:uname WHERE u.uid = :uid")
  public void update(@Param("email") String email, @Param("uid") Integer uid,@Param("about") String about,@Param("uname") String uname);

}
