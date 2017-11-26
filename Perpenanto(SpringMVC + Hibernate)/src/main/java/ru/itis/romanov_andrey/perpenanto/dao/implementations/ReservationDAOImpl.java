package ru.itis.romanov_andrey.perpenanto.dao.implementations;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.romanov_andrey.perpenanto.dao.interfaces.ReservationDAOInterface;
import ru.itis.romanov_andrey.perpenanto.models.Reservation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.List;

/**
 * 12.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Repository
public class ReservationDAOImpl implements ReservationDAOInterface{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Reservation> findAll() {
        return (List<Reservation>) this.entityManager.createQuery("SELECT r FROM Reservation r ")
                                                     .getResultList();
    }

    @Override
    @Transactional
    public void save(Reservation reservation) {
       this.entityManager.persist(reservation);
       this.entityManager.flush();
    }

    @Override
    public Reservation find(Long id) {
        return this.entityManager.find(Reservation.class, id);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Reservation reservation = find(id);
        this.entityManager.remove(reservation);
        this.entityManager.flush();

    }

    @Override
    public void update(Reservation model) {

        Reservation reservation = find(model.getId());

        this.entityManager.detach(reservation);
        reservation = Reservation.builder()
                                 .id(model.getId())
                                 .createdAt(model.getCreatedAt())
                                 .status(model.getStatus())
                                 .build();
        this.entityManager.merge(reservation);

    }

    @Override
    public List<Reservation> findAllByCreatedAt(Timestamp createdAt) {
        return (List<Reservation>) this.entityManager.createQuery("SELECT r FROM Reservation r where " +
                                                                     "r.createdAt = :createdAt")
                                                     .setParameter("createdAt", createdAt)
                                                     .getResultList();
    }

    @Override
    public List<Reservation> findAllByStatus(String status) {
        return (List<Reservation>) this.entityManager.createQuery("SELECT r FROM Reservation r where " +
                                                                     "r.status = :status")
                                                     .setParameter("status", status)
                                                     .getResultList();
    }
}
