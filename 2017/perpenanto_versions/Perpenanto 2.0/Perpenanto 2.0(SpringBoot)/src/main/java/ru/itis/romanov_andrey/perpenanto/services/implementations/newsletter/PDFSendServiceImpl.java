package ru.itis.romanov_andrey.perpenanto.services.implementations.newsletter;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.*;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.FileInfoRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.FilesToProfileRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProfileRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ReservationRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.newsletter.PDFSendService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.user.ProductToReservationService;
import ru.itis.romanov_andrey.perpenanto.utils.FileStorageUtil;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class PDFSendServiceImpl implements PDFSendService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ProductToReservationService productToReservationService;

    @Autowired
    private FileStorageUtil fileStorageUtil;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private FilesToProfileRepository filesToProfileRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    @Transactional
    public void sendPDF(FileInfo fileInfo, Profile profile) {

        MimeMessagePreparator messagePreparator = mimeMessage -> {

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("perpenantonewsletter@gmail.com");
            messageHelper.setTo(profile.getEmail());
            messageHelper.setSubject("Информация о вашем заказе");

            StringBuilder message = new StringBuilder();
            message.append(profile.getSurname()).append(" ").append(profile.getName())
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

        Long id = this.reservationRepository.findByCreatedAt(timestamp).getId();
        List<Product> products = this.productToReservationService.getReservationProducts(user, timestamp);
        Integer price = this.productToReservationService.getReservationPrice(timestamp);
        FileInfo fileInfo = this.fileStorageUtil.convertHTMLToPDF(id, products, price, timestamp);

        Profile profile = this.profileRepository.findByUser(user);

        FilesToProfile filesToProfile;

        try{

            filesToProfile =
             DataAccessUtils.objectResult(Collections.singletonList(this.filesToProfileRepository.find(profile.getId())),
                                          FilesToProfile.class);

            this.fileInfoRepository.save(fileInfo);
            filesToProfile.getFiles().add(fileInfo);
            this.filesToProfileRepository.update(filesToProfile);

        }catch(IncorrectResultSizeDataAccessException | IndexOutOfBoundsException e){

            FilesToProfile filesToProfile1 = FilesToProfile.builder()
                                                           .profile(profile)
                                                           .files(Sets.newHashSet(fileInfo))
                                                           .build();
            this.fileInfoRepository.save(fileInfo);
            this.filesToProfileRepository.save(filesToProfile1);


        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> this.sendPDF(fileInfo, profile));

    }
}
