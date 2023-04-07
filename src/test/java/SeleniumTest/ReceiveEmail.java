package SeleniumTest;

import com.sun.mail.imap.protocol.FLAGS;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.*;
import javax.mail.*;
import javax.mail.BodyPart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.AddressTerm;
import javax.mail.search.FromTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class is used to receive simple email.
 * @author w3spoint
 */
public class ReceiveEmail {
    public static void receiveEmail(String host,
                                    String storeType, String user, String password, String emailSubject, String emailContent){
        Properties props = new Properties();
        props.put("mail.imap.host", host);
        props.put("mail.imap.port", "993");
        props.put("mail.imap.starttls.enable", "true");
        props.put("mail.store.protocol", "imaps");
        Properties propsImap = new Properties();
        propsImap.setProperty("mail.imap.ssl.enable", "true");
        Session session = Session.getInstance(propsImap);
//        Session session = Session.getInstance(props);
        try {
            Store mailStore = session.getStore(storeType);
            mailStore.connect(host, user, password);

            Folder inbox = mailStore.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            AddressTerm sender = new FromTerm(new InternetAddress("no-reply@its.hawaii.edu"));
            SubjectTerm subject = new SubjectTerm("UH Groupings service feedback [" + emailSubject + "]");
            Message[] emailMessages = inbox.search(subject);
            ArrayUtils.reverse(emailMessages);
            System.out.println("Total Message - "
                    + emailMessages.length);
            Date currentTime = new Date();
            currentTime = DateUtils.addMinutes(currentTime, 0);
            int date = emailMessages[0].getReceivedDate().compareTo(currentTime);
            System.out.println(currentTime);
            assertEquals(-1, date, "Dates not equal: " + currentTime + "/n" + emailMessages[0].getReceivedDate());
            assertEquals(0, emailMessages[0].getSubject().compareTo("UH Groupings service feedback [" + emailSubject + "]"), "Subject not equal. Actual: " + emailMessages[0].getSubject() +"\nExpected: UH Groupings service feedback [" + emailSubject + "]");
            assertEquals(true, emailMessages[0].getContent().toString().contains(emailContent), "Email body not correct \n Actual: " + emailMessages[0].getContent().toString());

            for (int i = 0; i < 1; i++) {
                Message message = emailMessages[i];
                Address[] toAddress =
                        message.getRecipients(Message.RecipientType.TO);
                System.out.println();
                System.out.println("Email " + (i+1) + "-");
                System.out.println("Subject - " + message.getSubject());
                System.out.println("From - " + message.getFrom()[0]);
                System.out.println("Date - " + message.getReceivedDate().toString());

                System.out.print("To - ");
                for(int j = 0; j < toAddress.length; j++){
                    System.out.println(toAddress[j].toString());
                }
                System.out.println("Text - " +
                        message.getContent().toString());
                message.setFlag(FLAGS.Flag.DELETED, true);
            }
            emailMessages[0].setFlag(FLAGS.Flag.DELETED, true);


            inbox.close(false);
            mailStore.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in receiving email.");
        }
    }

    public static void main(String[] args) {
        MainPage admin = new MainPage();
        try {
            admin.getAdminCredentials();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String host = "imap.gmail.com";
        String mailStoreType = "imap";
        final String userName = admin.username + "@hawaii.edu";
        final String password = admin.password;
        final String sentBy = "fd";

        receiveEmail(host, mailStoreType, userName, password, "fdslk", "fdsl");
    }
}