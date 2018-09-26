package ru.itis.romanov_andrey.perpenanto.dao.implementations;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.romanov_andrey.perpenanto.dao.interfaces.ProfileDAOInterface;
import ru.itis.romanov_andrey.perpenanto.models.Profile;
import ru.itis.romanov_andrey.perpenanto.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * 12.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Repository
public class ProfileDAOImpl implements ProfileDAOInterface{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Profile> findAll() {
        return (List<Profile>) this.entityManager.createQuery("SELECT p FROM Profile p " +
                                                                 "join fetch p.user, join fetch p.products")
                                                 .getResultList();
    }

    @Override
    @Transactional
    public void save(Profile profile) {
        this.entityManager.persist(profile);
        this.entityManager.flush();
    }

    @Override
    public Profile find(Long id) {
        return this.entityManager.find(Profile.class, id);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Profile profile = find(id);
        this.entityManager.remove(profile);
        this.entityManager.flush();

    }

    @Override
    @Transactional
    public void update(Profile model) {

        Profile profile = find(model.getId());

        this.entityManager.detach(profile);
        profile = Profile.builder()
                         .id(model.getId())
                         .personName(model.getPersonName())
                         .personSurname(model.getPersonSurname())
                         .build();
        this.entityManager.merge(profile);

    }

    @Override
    public Profile findByUser(User user) {
        return (Profile) this.entityManager.createQuery("SELECT p FROM Profile p where p.user = :user")
                                           .setParameter("user", user)
                                           .getSingleResult();
    }
}
