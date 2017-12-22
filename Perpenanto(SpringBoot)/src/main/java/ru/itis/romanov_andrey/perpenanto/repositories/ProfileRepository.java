package ru.itis.romanov_andrey.perpenanto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Profile;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface ProfileRepository extends JpaRepository<Profile, Long>{

    Profile findByUser(User user);

    @Query(value = "SELECT * FROM profile WHERE user_id = :userId", nativeQuery = true)
    Profile findByUserId(@Param("userId") Long userId);


}
