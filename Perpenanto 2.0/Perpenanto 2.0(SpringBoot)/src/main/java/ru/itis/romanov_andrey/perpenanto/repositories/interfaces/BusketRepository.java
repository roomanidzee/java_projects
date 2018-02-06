package ru.itis.romanov_andrey.perpenanto.repositories.interfaces;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.Busket;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Reservation;


/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface BusketRepository extends CrudDAOInterface<Busket, Long>{

    void delete(Long id1, Long id2);
    void payForBusket(Busket busket, Reservation reservation);

}
