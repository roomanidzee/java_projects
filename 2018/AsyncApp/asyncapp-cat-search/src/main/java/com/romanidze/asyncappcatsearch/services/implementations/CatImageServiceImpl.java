package com.romanidze.asyncappcatsearch.services.implementations;

import com.romanidze.asyncappcatsearch.dto.CatImageDTO;
import com.romanidze.asyncappcatsearch.services.interfaces.CatImageService;

import lombok.SneakyThrows;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 06.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class CatImageServiceImpl implements CatImageService {

    private static final Logger logger = LogManager.getLogger(CatImageServiceImpl.class);

    @Value("${cats.url}")
    private String catsURL;

    private final RestTemplate restTemplate;
    private final Executor executor;

    @Autowired
    public CatImageServiceImpl(RestTemplate restTemplate,
                               Executor executor) {
        this.restTemplate = restTemplate;
        this.executor = executor;
    }

    @Override
    @SneakyThrows
    public CatImageDTO getImageOfCat() {

        CompletableFuture<CatImageDTO> catFuture = new CompletableFuture<>();

        this.executor.execute(() -> {

            ResponseEntity<List<CatImageDTO>> response =
                    this.restTemplate.exchange(this.catsURL, HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<CatImageDTO>>(){});

            catFuture.complete(response.getBody().get(0));

        });

        while(!catFuture.isDone()){
            logger.info("Получаем картинку кота....");
        }

        return catFuture.get();

    }

}
