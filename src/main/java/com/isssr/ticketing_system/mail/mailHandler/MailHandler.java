package com.isssr.ticketing_system.mail.mailHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Configuration
@Component
@Service
public abstract class MailHandler implements IMailHandler, Runnable {

    //IMAP server settings -----------

    @Value("${mail.receiver.protocol}")
    protected String protocol;

    @Value("${mail.receiver.host}")
    protected String receiverHost;

    @Value("${mail.receiver.port}")
    protected String port;

    @Value("${mail.receiver.attachmentDirectory}")
    protected String saveDirectory;

    //Sender setting ----------

    @Value("${mail.sender.host}")
    protected String senderHost;

    //E-mail credentials ---------

    @Value("${mail.username}")
    protected String userName;

    @Value("${mail.password}")
    protected String password;

    //E-mail format ---------

    @Value("${mail.format}")
    protected String[] format;

    //E-mail Attachment ----------

    @Value("${mail.attach.format}")
    protected String attach;

    /**
     * Send an email to the specified address
     *
     * @param address the address to which mail is sent
     * @param mailType the type of email
     */
    public abstract void sendMail(String address, String mailType);


    /**
     * Send an email to the specified address
     *
     * @param address the address to which mail is sent
     * @param mailType the type of email
     * @param text the text to send
     */
    public abstract void sendMail(String address, String mailType, String text);

    /**
     * Receive an email
     */
    public abstract void receiveMail();

    /**
     * Check if server is running
     *
     * @return true if server is running, false otherwise
     */
    public abstract boolean isServerRunning();
}
