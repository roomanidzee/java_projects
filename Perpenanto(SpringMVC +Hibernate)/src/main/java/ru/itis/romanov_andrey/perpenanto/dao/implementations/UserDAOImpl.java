package ru.itis.romanov_andrey.perpenanto.dao.implementations;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.romanov_andrey.perpenanto.dao.interfaces.UserDAOInterface;
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
public class UserDAOImpl implements UserDAOInterface{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAll() {
        return (List<User>) this.entityManager.createQuery("SELECT u FROM User u")
                                              .getResultList();
    }

    @Override
    @Transactional
    public void save(User user) {
        this.entityManager.persist(user);
        this.entityManager.flush();
    }

    @Override
    public User find(Long id) {
        return this.entityManager.find(User.class, id);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        User user = find(id);
        this.entityManager.remove(user);
        this.entityManager.flush();

    }

    @Override
    @Transactional
    public void update(User model) {

        User user = find(model.getId());

        this.entityManager.detach(user);
        user = User.builder()
                   .id(model.getId())
                   .username(model.getUsername())
                   .hashPassword(model.getHashPassword())
                   .build();
        this.entityManager.merge(user);

    }

    @Override
    public User findByUsername(String username) {
        return (User) this.entityManager.createQuery("SELECT u FROM User u where u.username = :username")
                                        .setParameter("username", username)
                                        .getSingleResult();
    }
}
