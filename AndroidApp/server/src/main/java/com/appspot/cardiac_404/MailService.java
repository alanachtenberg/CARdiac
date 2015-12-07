package com.appspot.cardiac_404;



import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Alan on 11/23/2015.
 */
public class MailService {

    static private String CONFIRMATION_MSG = "Thank you for registering for Cardiac. You can now use the services.";
    static private String CONFIRMATION_SBJ = "CARdiac Registration";
    static private String SENDER_ADDR = "admin@cardiac-404.appspotmail.com";


    static public void sendConfirmation(String targetAddress) throws MessagingException {
        sendMail(targetAddress,CONFIRMATION_MSG, CONFIRMATION_SBJ);
    }


    static public void sendMail(String targetAddress,String message, String subject) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props,null);
        try{
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(SENDER_ADDR));
            msg.setSubject(subject);
            msg.setText(message);
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(targetAddress));
            Transport.send(msg);
        }catch(AddressException e){
            e.printStackTrace();
        }
    }
}
