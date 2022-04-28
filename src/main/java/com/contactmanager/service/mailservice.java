package com.contactmanager.service;

import java.util.Properties;


import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class mailservice {

	public boolean sendmail(String subject, String message, String to) {
		boolean f = false;

		String from = "karthikpeddoju13@gmail.com";
		String host = "smtp.gmail.com";

		Properties properties = System.getProperties();

		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		// steps
		Session session = Session.getInstance(properties,new javax.mail.Authenticator() {
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication("karthikpeddoju13@gmail.com", "Karthik@123");
			}
		});

		session.setDebug(true);
		MimeMessage m = new MimeMessage(session);
		try {
			//InternetAddress addressFrom = new InternetAddress(from);
			m.setFrom(from);
			m.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			m.setSubject(subject);
			m.setText(message);
			
			Transport.send(m);
f=true;
		} catch (Exception e) {
e.printStackTrace();

		}
		return f;

	}

}
