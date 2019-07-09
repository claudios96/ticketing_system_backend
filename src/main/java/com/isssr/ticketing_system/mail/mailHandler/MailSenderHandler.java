package com.isssr.ticketing_system.mail.mailHandler;

import com.isssr.ticketing_system.logger.aspect.LogOperation;
import com.isssr.ticketing_system.mail.MailService;
import com.isssr.ticketing_system.mail.model.Mail;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Handle the email sending process
 */
@Service
public class MailSenderHandler extends MailHandler {

    //Thread Attribute
    private String address;
    private String mailType;
    private String mailText;

    @Autowired
    private MailService mailService;

    /**
     * Send an email to the specified address
     *
     * @param address the address to which mail is sent
     * @param mailType the type of email
     */
    @LogOperation(tag = "MAIL_SEND", inputArgs = "mailType")
    public void sendMail(String address, String mailType) {
        //init attribute
        this.address = address;
        this.mailType = mailType;
        this.mailText = null;

        //Start thread
        (new Thread(this)).start();
    }


    /**
     * Send an email to the specified address
     *
     * @param address the address to which mail is sent
     * @param mailType the type of email
     * @param mailText the text to send
     */
    @LogOperation(tag = "MAIL_SEND", inputArgs = "mailType")
    public void sendMail(String address, String mailType, String mailText) {
        //init attribute
        this.address = address;
        this.mailType = mailType;
        this.mailText = mailText;

        //Start thread
        (new Thread(this)).start();
    }

    /**
     * This thread send a mail to a specified address
     */
    public void run() {
        try {
            //Query to db for retrieve subject and content email, by type
            Mail mail = this.mailService.findByType(mailType).get();

            //Build email
            MultiPartEmail email = new MultiPartEmail();
            email.setSmtpPort(587);
            email.setAuthenticator(new DefaultAuthenticator(userName, password));
            email.setHostName("smtp.gmail.com");
            email.setFrom(userName);
            email.setSubject(mail.getSubject());
            if (mailText == null) email.setMsg(mail.getDescription());
            else email.setMsg(mail.getDescription() + "\n\n" + mailText);
            email.addTo(address);
            email.setTLS(true);

            //Check email type for adding attach
            if (mailType.equals("FORMAT")) {
                // Create the attachment
                email.attach(new File(System.getProperty("user.dir") + saveDirectory + File.separator + attach));
            }

            //Send e-mail
            email.send();
            System.out.println("Response e-mail sent!");
        } catch (Exception e) {
            System.out.println("Exception :: " + e);
        }
    }



    @Override
    public void receiveMail() {
    }

    /**
     * Check if server is running
     *
     * @return true if server is running, false otherwise
     */
    @Override
    public boolean isServerRunning() {
        return false;
    }
}


