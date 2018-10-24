package com.romanidze.asyncapprabbitmain.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/**
 * 16.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class StompEventListener implements ApplicationListener<SessionConnectEvent> {

    private static final Logger logger = LogManager.getLogger(StompEventListener.class);

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {

        Object source = event.getSource();
        Message message = event.getMessage();

        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String connectMessage = sha.getCommand()== StompCommand.CONNECT ? "Да": "Нет";
        String disconnectMessage = sha.getCommand()== StompCommand.DISCONNECT ? "Да": "Нет";

        StringBuilder sb = new StringBuilder();

        sb.append("Клиент подсоединился: ").append(connectMessage).append("; клиент отсоединился: ")
          .append(disconnectMessage).append("; источник: ").append(source)
          .append("; сообщение: ").append(message);

        logger.info(sb.toString());

    }
}
