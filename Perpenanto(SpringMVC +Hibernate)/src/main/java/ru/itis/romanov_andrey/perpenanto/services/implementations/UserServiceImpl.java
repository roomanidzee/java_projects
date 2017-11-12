package ru.itis.romanov_andrey.perpenanto.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.romanov_andrey.perpenanto.dao.interfaces.UserDAOInterface;
import ru.itis.romanov_andrey.perpenanto.models.User;
import ru.itis.romanov_andrey.perpenanto.services.interfaces.UserServiceInterface;

import java.util.List;

/**
 * 11.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserServiceInterface {

    @Autowired
    private UserDAOInterface userDAO;

    @Override
    public void saveOrUpdate(User user) {
        this.userDAO.save(user);
    }

    @Override
    public void delete(Long id) {
        this.userDAO.delete(id);
    }

    @Override
    public List<User> getUsers(){
        return this.userDAO.findAll();
    }
}
