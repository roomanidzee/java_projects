package ru.itis.romanov_andrey.perpenanto.services.implementations.newsletter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.newsletter.SendPDFServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProfileServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ReservationServiceInterface;
import ru.itis.romanov_andrey.perpenanto.utils.FileStorageUtil;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 09.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class SendPDFServiceImpl implements SendPDFServiceInterface{

    private final JavaMailSender javaMailSender;

    @Autowired
    public SendPDFServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Autowired
    private FileStorageUtil fileStorageUtil;

    @Autowired
    private ProfileServiceInterface profileService;

    @Autowired
    private ReservationServiceInterface reservationService;

    @Override
    @Transactional
    public void sendPDF(FileInfo fileInfo, Profile profile) {

        MimeMessagePreparator messagePreparator = mimeMessage -> {

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("perpenantonewsletter@gmail.com");
            messageHelper.setTo(profile.getEmail());
            messageHelper.setSubject("Информация о вашем заказе");

            StringBuilder message = new StringBuilder();
            message.append(profile.getPersonSurname()).append(" ").append(profile.getPersonName())
                   .append(", отправляем вам квитанцию о вашем последнем заказе");
            messageHelper.setText(message.toString(), false);

            FileSystemResource file = new FileSystemResource(fileInfo.getUrl());
            messageHelper.addAttachment(file.getFilename(), file);

        };

        try{
            this.javaMailSender.send(messagePreparator);
        }catch (MailException e) {
            throw new IllegalArgumentException(e);
        }

    }

    @Override
    public void sendEmailWithPDF(User user, Timestamp timestamp) {

        Long id = this.reservationService.getReservationId(timestamp);
        List<Product> products = this.reservationService.getReservationProducts(user.getProfile(), timestamp);
        Integer price = this.reservationService.getReservationPrice(timestamp);
        FileInfo fileInfo = this.fileStorageUtil.convertHTMLToPDF(id, products, price, timestamp);

        user.getProfile().getFiles().add(fileInfo);
        this.profileService.saveOrUpdate(user.getProfile());

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> sendPDF(fileInfo, user.getProfile()));

    }
}
