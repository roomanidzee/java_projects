package com.romanidze.asyncappdbstorage.repositories;

import com.romanidze.asyncappdbstorage.domain.RoleType;
import com.romanidze.asyncappdbstorage.security.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface RoleTypeRepository extends JpaRepository<RoleType, Long> {

    RoleType findByRole(Role role);

}
