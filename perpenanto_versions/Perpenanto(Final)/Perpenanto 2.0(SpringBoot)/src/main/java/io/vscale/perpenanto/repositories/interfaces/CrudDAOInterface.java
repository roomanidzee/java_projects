package io.vscale.perpenanto.repositories.interfaces;

import java.util.List;
/**
 * 24.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface CrudDAOInterface<M, I> {

    List<M> findAll();
    void save(M model);
    M find(I id);
    void delete(I id);
    void update(M model);

}

