package ru.itis.romanov_andrey.perpenanto.services.implementations.admin;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.ProfileRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.AdminService;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.newsletter.EmailService;
import ru.itis.romanov_andrey.perpenanto.utils.PasswordGenerator;

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
public class AdminServiceImpl implements AdminService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${server.port}")
    private Integer port;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    @Transactional
    public void createTempPassword(Long userId) {

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

            this.executorService.submit(() ->{

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
        sb1.append(address).append(":").append(this.port).append("/confirm/").append(result);

        this.executorService.submit(() -> {

            Context context = new Context();
            context.setVariable("message", "Ссылка для подтверждения: " + sb1.toString());
            context.setVariable("time", localDateTime.toString());

            String text = this.templateEngine.process("email/mail_template", context);
            String subject = "Подтверждение для пользователя " + profile.getEmail();
            this.emailService.sendMail(text, subject, profile.getEmail());

        });

    }
}
