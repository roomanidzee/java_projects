package ru.itis.romanov_andrey.perpenanto.services.implementations.newsletter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import ru.itis.romanov_andrey.perpenanto.services.interfaces.newsletter.EmailServiceInterface;

/**
 * 17.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class SimpleEmailServiceImpl implements EmailServiceInterface{

    private final JavaMailSender javaMailSender;

    @Autowired
    public SimpleEmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMail(String text, String subject, String email) {

        MimeMessagePreparator messagePreparator = mimeMessage -> {

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

            messageHelper.setFrom("perpenantonewsletter@gmail.com");
            messageHelper.setTo(email);
            messageHelper.setSubject(subject);
            messageHelper.setText(text, true);

        };

        try{
            this.javaMailSender.send(messagePreparator);
        }catch (MailException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
