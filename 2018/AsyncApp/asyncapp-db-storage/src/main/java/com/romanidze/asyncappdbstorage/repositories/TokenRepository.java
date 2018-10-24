package com.romanidze.asyncappdbstorage.repositories;

import com.romanidze.asyncappdbstorage.domain.Token;
import com.romanidze.asyncappdbstorage.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByUser(User user);

}
