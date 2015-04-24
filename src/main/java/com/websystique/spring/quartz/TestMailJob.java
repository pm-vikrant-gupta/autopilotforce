package com.websystique.spring.quartz;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.websystique.spring.Attachment;
import com.websystique.spring.Email;
import com.websystique.spring.EmailService;

public class TestMailJob extends QuartzJobBean {

	private EmailService emailService;

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		// TODO Auto-generated method stub

		sendMail();
		sendMail();
		sendMail();
		checkMail();

	}

	private void checkMail() {
		String host = "pop.gmail.com";// change accordingly
	      String mailStoreType = "pop3";
	      String username = "auto.pilot.pm@gmail.com";// change accordingly
	      String password = "K!llB!ll";// change accordingly
	      
		try {
			//create properties field
			  Properties properties = new Properties();

			  properties.put("mail.pop3.host", host);
			  properties.put("mail.pop3.port", "995");
			  properties.put("mail.pop3.starttls.enable", "true");
			  
			  Session emailSession = Session.getDefaultInstance(properties);
			  
			//create the POP3 store object and connect with the pop server
			  Store store = emailSession.getStore("pop3s");

			  store.connect(host, username, password);

			  //create the folder object and open it
			  Folder emailFolder = store.getFolder("INBOX");
			  emailFolder.open(Folder.READ_ONLY);

			  // retrieve the messages from the folder in an array and print it
			  Message[] messages = emailFolder.getMessages();
			  System.out.println("messages.length---" + messages.length);

			  for (int i = 0, n = messages.length; i < n; i++) {
			     Message message = messages[i];
			     System.out.println("---------------------------------");
			     System.out.println("Email Number " + (i + 1));
			     System.out.println("Subject: " + message.getSubject());
			     System.out.println("From: " + message.getFrom()[0]);
			     System.out.println("Text: " + message.getContent().toString());

			  }

			  //close the store and folder objects
			  emailFolder.close(false);
			  store.close();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void sendMail() {
		// TODO Auto-generated method stub
		System.out.println("Fetching mail..");
		try {
			Email newEmail = new Email();
			newEmail.setFrom("test_shalmali@gmail.com");
			newEmail.setSubject("Test subject");
			newEmail.setText("<h1>Dear Many Many Happy Returns of the day :-)</h1>");
			newEmail.setTo("auto.pilot.pm@gmail.com");
	
			byte[] data = null;
		    ClassPathResource img = new ClassPathResource("testOfferSheet.xls");
		    InputStream inputStream = img.getInputStream();
		    data = new byte[inputStream.available()];
		    while((inputStream.read(data)!=-1));
		   
		    Attachment attachment = new Attachment(data, "testOfferSheet", 
		      "application/vnd.ms-excel", false);
		    newEmail.addAttachment(attachment);
	    
		
			emailService.sendEmail(newEmail);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

}
