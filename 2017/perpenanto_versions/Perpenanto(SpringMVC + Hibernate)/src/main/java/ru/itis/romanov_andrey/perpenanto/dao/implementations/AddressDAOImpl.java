package ru.itis.romanov_andrey.perpenanto.dao.implementations;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.romanov_andrey.perpenanto.dao.interfaces.AddressDAOInterface;
import ru.itis.romanov_andrey.perpenanto.models.Address;
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
public class AddressDAOImpl implements AddressDAOInterface{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Address> findAll() {
        return (List<Address>) this.entityManager.createQuery("SELECT a from Address a join fetch a.users")
                                                 .getResultList();
    }

    @Override
    @Transactional
    public void save(Address model) {
        this.entityManager.persist(model);
        this.entityManager.flush();
    }

    @Override
    public Address find(Long id) {
        return this.entityManager.find(Address.class, id);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Address address = find(id);
        this.entityManager.remove(address);
        this.entityManager.flush();

    }

    @Override
    @Transactional
    public void update(Address model) {

        Address address = find(model.getId());

        this.entityManager.detach(address);
        address = Address.builder()
                         .id(model.getId())
                         .country(model.getCountry())
                         .postalCode(model.getPostalCode())
                         .city(model.getCity())
                         .street(model.getStreet())
                         .homeNumber(model.getHomeNumber())
                         .build();
        this.entityManager.merge(address);

    }

    @Override
    public List<Address> findByUsers(List<User> users) {
        return (List<Address>) this.entityManager.createQuery("SELECT a from Address a where a.users = :users")
                                                 .setParameter("users", users)
                                                 .getResultList();
    }

    @Override
    public Address findByPostalCode(Integer postalCode) {
        return (Address) this.entityManager.createQuery("SELECT a from Address a where a.postalCode = :postalCode")
                                           .setParameter("postalCode", postalCode)
                                           .getSingleResult();
    }
}
