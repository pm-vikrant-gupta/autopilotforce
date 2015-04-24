package com.websystique.spring.quartz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import javax.mail.*;
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

        String host = "pop.gmail.com";// change accordingly
        String mailStoreType = "pop3";
        String username = "auto.pilot.pm@gmail.com";// change accordingly
        String password = "K!llB!ll";// change accordingly


		/*sendMail();
		sendMail();
		sendMail();*/
        fetch( host,  mailStoreType,  username, password);

    }

    public  void fetch(String pop3Host, String storeType, String user,
                             String password) {
        try {
            // create properties field
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "pop3");
            properties.put("mail.pop3.host", pop3Host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);
            // emailSession.setDebug(true);

            // create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect(pop3Host, user, password);

            // create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);
            boolean isAttachmentPresent = false;
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                writePart(message,isAttachmentPresent);
            }

            if(isAttachmentPresent){
                System.out.println("Attachment is present ...so go for operation");
            }else{
                System.out.println("Attachment is not present ...return template ");
            }

            // close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    * This method checks for content-type
    * based on which, it processes and
    * fetches the content of the message
    */
    public static void writePart(Part p,boolean isAttachmentPresent) throws Exception {
        if (p instanceof Message)
            //Call methos writeEnvelope
            writeEnvelope((Message) p);
        if (p.isMimeType("multipart/*")) {
            System.out.println("This is a Multipart");
            System.out.println("--------------------------- ");
            Multipart mp = (Multipart) p.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++)
                writePart(mp.getBodyPart(i),isAttachmentPresent);
        }

        else {
            Object o = p.getContent();
            if (o instanceof InputStream) {
                if(p.isMimeType("application/vnd.ms-excel")){
                    isAttachmentPresent = true;
                }
                InputStream is = (InputStream) o;
                is = (InputStream) o;
                int c;
                while ((c = is.read()) != -1)
                    System.out.write(c);
            }

        }

    }
    /*
    * This method would print FROM,TO and SUBJECT of the message
    */
    public static void writeEnvelope(Message m) throws Exception {
        System.out.println("This is the message envelope");
        System.out.println("---------------------------");
        Address[] a;

        // FROM
        if ((a = m.getFrom()) != null) {
            for (int j = 0; j < a.length; j++)
                System.out.println("FROM: " + a[j].toString());
        }

        // TO
        if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
            for (int j = 0; j < a.length; j++)
                System.out.println("TO: " + a[j].toString());
        }

        // SUBJECT
        if (m.getSubject() != null)
            System.out.println("SUBJECT: " + m.getSubject());

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
