package io.vscale.perpenanto.utils.dbutils;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import io.vscale.perpenanto.repositories.interfaces.CrudDAOInterface;

import java.util.Optional;

@Component
public class JdbcTemplateWrapper<T> {

    private final JdbcTemplate template;

    @Autowired
    public JdbcTemplateWrapper(JdbcTemplate template) {
        this.template = template;
    }

    public Optional<T> findItem(CrudDAOInterface crudDAO, Long id){

        Optional<T> finalResult;

        Object result;
        Object temp = new Object();
        T obj = (T)temp;

        try{

            result = DataAccessUtils.objectResult(ImmutableList.of(crudDAO.find(id)), obj.getClass());
            finalResult = Optional.of((T) result);

        }catch(IncorrectResultSizeDataAccessException | IndexOutOfBoundsException e){
            return Optional.empty();
        }

        return finalResult;

    }

    public Optional<T> findByItem(String sql, RowMapper<T> rowMapper, Object... args){

        Optional<T> finalResult;

        try{
            finalResult = Optional.of(this.template.queryForObject(sql, rowMapper, args));
        }catch(IncorrectResultSizeDataAccessException | IndexOutOfBoundsException e){
            return Optional.empty();
        }

        return finalResult;

    }

}
