package io.vscale.perpenanto.services.implementations.admin;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import io.vscale.perpenanto.models.usermodels.Profile;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.repositories.interfaces.ProfileRepository;
import io.vscale.perpenanto.repositories.interfaces.UserRepository;
import io.vscale.perpenanto.services.interfaces.admin.AdminService;
import io.vscale.perpenanto.services.interfaces.newsletter.EmailService;
import io.vscale.perpenanto.utils.userutils.PasswordGenerator;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 31.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminServiceImpl implements AdminService{

    private UserRepository userRepository;
    private ProfileRepository profileRepository;
    private PasswordGenerator passwordGenerator;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    private SpringTemplateEngine templateEngine;

    @Override
    @Transactional
    public void createTempPassword(Long userId) {

        ExecutorService executorService = Executors.newCachedThreadPool();

        Optional<User> existedUser = this.userRepository.findById(userId);

        if(!existedUser.isPresent()){
           throw new IllegalArgumentException("User not found");
        }

        existedUser.ifPresent(user -> {

            User admin = this.userRepository.find(1L);
            Profile adminProfile = this.profileRepository.findByUser(admin);

            String tempPassword = this.passwordGenerator.generate();
            user.setTempPassword(this.passwordEncoder.encode(tempPassword));
            this.userRepository.update(user);

            executorService.submit(() ->{

                Context context = new Context();

                StringBuilder sb = new StringBuilder();
                StringBuilder sb1 = new StringBuilder();

                sb.append("Временный пароль для пользователя ").append(user.getLogin())
                  .append(" :").append(tempPassword);

                context.setVariable("message", sb.toString());
                context.setVariable("time", LocalDateTime.now(ZoneId.of("Europe/Moscow")).toString());

                String text = this.templateEngine.process("email/mail_template", context);
                String adminEmail = adminProfile.getEmail();
                sb1.append("Сгенерированный пароль для пользователя: ").append(user.getLogin());

                this.emailService.sendMail(text, sb1.toString(), adminEmail);

            });

        });

    }

    @Override
    @Transactional
    @SneakyThrows
    public void generateConfirmLink(User user) {

        int port = 8080;
        ExecutorService executorService = Executors.newCachedThreadPool();

        ZoneId moscowZone = ZoneId.of("Europe/Moscow");
        LocalDateTime localDateTime = LocalDateTime.now(moscowZone);
        Profile profile = this.profileRepository.findByUser(user);

        StringBuilder sb = new StringBuilder();
        sb.append(user.getLogin()).append(user.getPassword())
          .append(user.getUserState().toString()).append(localDateTime.toString());

        byte[] bytes = sb.toString().getBytes("UTF-8");
        UUID uuid = UUID.nameUUIDFromBytes(bytes);
        String result = uuid.toString();

        user.setConfirmHash(result);
        this.userRepository.update(user);

        StringBuilder sb1 = new StringBuilder();
        String address = InetAddress.getLocalHost().getHostAddress();
        sb1.append(address).append(":").append(port).append("/confirm/").append(result);

        executorService.submit(() -> {

            Context context = new Context();
            context.setVariable("message", "Ссылка для подтверждения: " + sb1.toString());
            context.setVariable("time", localDateTime.toString());

            String text = this.templateEngine.process("email/mail_template", context);
            String subject = "Подтверждение для пользователя " + profile.getEmail();
            this.emailService.sendMail(text, subject, profile.getEmail());

        });

    }
}
