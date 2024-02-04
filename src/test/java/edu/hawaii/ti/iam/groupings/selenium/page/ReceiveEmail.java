package edu.hawaii.ti.iam.groupings.selenium.page;

import com.sun.mail.imap.protocol.FLAGS;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;


import java.util.Date;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AddressTerm;
import javax.mail.search.FromTerm;
import javax.mail.search.SubjectTerm;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class is used to receive simple email.
 * @author w3spoint
 */
public class ReceiveEmail {
    public static void receiveEmail(String host,
                                    String storeType, String user, String password, String emailSubject, String emailContent, boolean stackDump){
        Properties props = new Properties();
        props.put("mail.imap.host", host);
        props.put("mail.imap.port", "993");
        props.put("mail.imap.starttls.enable", "true");
        props.put("mail.store.protocol", "imaps");
        props.setProperty("mail.imap.ssl.enable", "true");
        Session session = Session.getInstance(props);
        try {
            Store mailStore = session.getStore(storeType);
            mailStore.connect(host, user, password);
            Folder inbox = mailStore.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            AddressTerm sender = new FromTerm(new InternetAddress("no-reply@its.hawaii.edu"));;
            SubjectTerm subject;
            if (!stackDump) {
                 subject = new SubjectTerm("UH Groupings service feedback [" + emailSubject + "]");
            } else {
                subject = new SubjectTerm("(test) UH Groupings API Error Response");
            }
            Message[] emailMessages = inbox.search(subject);
            ArrayUtils.reverse(emailMessages);
            Date currentTime = new Date();
            currentTime = DateUtils.addMinutes(currentTime, 0);
            int date = emailMessages[0].getReceivedDate().compareTo(currentTime);
            assertEquals(-1, date, "Dates not equal: " + currentTime + "/n" + emailMessages[0].getReceivedDate());
            if(!stackDump){
                assertEquals(0, emailMessages[0].getSubject().compareTo("UH Groupings service feedback [" + emailSubject + "]"), "Subject not equal. Actual: " + emailMessages[0].getSubject() + "\nExpected: UH Groupings service feedback [" + emailSubject + "]");
                assertEquals(true, emailMessages[0].getContent().toString().contains(emailContent), "Email body not correct \n Actual: " + emailMessages[0].getContent().toString());

            } else {
                assertEquals(0, emailMessages[0].getSubject().compareTo("(test) UH Groupings API Error Response"), "Subject not equal. Actual: " + emailMessages[0].getSubject() + "\nExpected: (test) UH Groupings API Error Response");
                assertEquals(true, emailMessages[0].getContent().toString().contains(emailContent), "Email body not correct \n Actual: " + emailMessages[0].getContent().toString());
            }
            emailMessages[0].setFlag(FLAGS.Flag.DELETED, true);

            inbox.close(false);
            mailStore.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in receiving email.");
        }
    }
}
