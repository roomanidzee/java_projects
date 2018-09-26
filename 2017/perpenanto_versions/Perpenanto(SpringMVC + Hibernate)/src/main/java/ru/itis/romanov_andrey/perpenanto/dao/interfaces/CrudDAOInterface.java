package ru.itis.romanov_andrey.perpenanto.dao.interfaces;

import java.util.List;

/**
 * 12.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface CrudDAOInterface<M, ID> {

    List<M> findAll();
    void save(M model);
    M find(ID id);
    void delete(ID id);
    void update(M model);

}
