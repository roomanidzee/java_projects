package com.romanidze.asyncappdbstorage.repositories;

import com.romanidze.asyncappdbstorage.domain.CatImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 04.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface CatImageRepository extends JpaRepository<CatImage, Long> {

    Optional<CatImage> findByImageURL(String imageURL);

}
