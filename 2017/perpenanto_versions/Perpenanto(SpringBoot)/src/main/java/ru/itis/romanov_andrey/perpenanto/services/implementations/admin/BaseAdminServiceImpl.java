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
import ru.itis.romanov_andrey.perpenanto.repositories.ProfileRepository;
import ru.itis.romanov_andrey.perpenanto.repositories.UserRepository;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.admin.AdminServiceInterface;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.newsletter.EmailServiceInterface;
import ru.itis.romanov_andrey.perpenanto.utils.PasswordGenerator;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 24.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class BaseAdminServiceImpl implements AdminServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailServiceInterface emailService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${server.port}")
    private Integer port;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    @Transactional
    public void createTempPassword(Long userId) {

        Optional<User> existedUser =
                Optional.of(this.userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found")));

        User admin = this.userRepository.findOne(Long.valueOf(1));
        User user = existedUser.get();

        String tempPassword = this.passwordGenerator.generate();
        user.setHashTempPassword(this.passwordEncoder.encode(tempPassword));
        this.userRepository.save(user);

        this.executorService.submit(() -> {

            Context context = new Context();
            context.setVariable("message", "Пароль: " + tempPassword);

            ZoneId moscowZone = ZoneId.of("Europe/Moscow");
            LocalDateTime localDateTime = LocalDateTime.now(moscowZone);
            context.setVariable("time", localDateTime.toString());

            String text = this.templateEngine.process("email/mail_template", context);
            this.emailService.sendMail(text,
                    "Сгенерированный пароль для пользователя " + user.getProfile().getEmail(),
                    admin.getProfile().getEmail());

        });

    }

    @Override
    @Transactional
    @SneakyThrows
    public void generateHash(User user) {

        ZoneId moscowZone = ZoneId.of("Europe/Moscow");
        LocalDateTime localDateTime = LocalDateTime.now(moscowZone);
        Profile profile = this.profileRepository.findByUserId(user.getId());

        StringBuilder sb = new StringBuilder();
        sb.append(user.getLogin()).append(user.getProtectedPassword())
          .append(user.getState()).append(localDateTime.toString());

        byte[] bytes = sb.toString().getBytes("UTF-8");
        UUID uuid = UUID.nameUUIDFromBytes(bytes);

        String result = uuid.toString();

        user.setConfirmHash(result);
        this.userRepository.save(user);

        StringBuilder sb1 = new StringBuilder();
        String address = InetAddress.getLocalHost().getHostAddress();
        sb1.append(address).append(":").append(this.port).append("/confirm/").append(result);

        this.executorService.submit(() -> {

            Context context = new Context();
            context.setVariable("message", "Ссылка для подтверждения: " + sb1.toString());
            context.setVariable("time", localDateTime.toString());

            String text = this.templateEngine.process("email/mail_template", context);
            this.emailService.sendMail(text, "Подтверждение для пользователя " + profile.getEmail(),
                                       profile.getEmail());

        });

    }
}
