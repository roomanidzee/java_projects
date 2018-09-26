package io.vscale.perpenanto.services.implementations.newsletter;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.vscale.perpenanto.models.usermodels.*;
import io.vscale.perpenanto.repositories.interfaces.FileInfoRepository;
import io.vscale.perpenanto.repositories.interfaces.FilesToProfileRepository;
import io.vscale.perpenanto.repositories.interfaces.ProfileRepository;
import io.vscale.perpenanto.repositories.interfaces.ReservationRepository;
import io.vscale.perpenanto.services.interfaces.newsletter.PDFSendService;
import io.vscale.perpenanto.services.interfaces.user.ProductToReservationService;
import io.vscale.perpenanto.utils.dbutils.FileStorageUtil;
import io.vscale.perpenanto.utils.dbutils.JdbcTemplateWrapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PDFSendServiceImpl implements PDFSendService {

    private JavaMailSender javaMailSender;
    private ReservationRepository reservationRepository;
    private ProductToReservationService productToReservationService;
    private FileStorageUtil fileStorageUtil;
    private FileInfoRepository fileInfoRepository;
    private FilesToProfileRepository filesToProfileRepository;
    private ProfileRepository profileRepository;
    private JdbcTemplateWrapper<FilesToProfile> jdbcTemplateWrapper;

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

        Optional<FilesToProfile> tempFilesToProfile =
                this.jdbcTemplateWrapper.findItem(this.filesToProfileRepository, profile.getId());

        if(!tempFilesToProfile.isPresent()){

            FilesToProfile filesToProfile1 = FilesToProfile.builder()
                                                           .profile(profile)
                                                           .files(Sets.newHashSet(fileInfo))
                                                           .build();
            this.fileInfoRepository.save(fileInfo);
            this.filesToProfileRepository.save(filesToProfile1);

        }

        tempFilesToProfile.ifPresent(filesToProfile -> {

            this.fileInfoRepository.save(fileInfo);
            filesToProfile.getFiles().add(fileInfo);
            this.filesToProfileRepository.update(filesToProfile);

        });

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> this.sendPDF(fileInfo, profile));

    }
}
