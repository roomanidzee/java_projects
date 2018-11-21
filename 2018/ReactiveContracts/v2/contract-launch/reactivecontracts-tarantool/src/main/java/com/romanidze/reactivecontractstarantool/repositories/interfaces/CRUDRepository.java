package com.romanidze.reactivecontractstarantool.repositories.interfaces;

/**
 * 17.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface CRUDRepository<M, ID> {

    void save(M model);
    M findOne(ID id);
    void update(ID id, M model);
    void delete(ID id);

}
