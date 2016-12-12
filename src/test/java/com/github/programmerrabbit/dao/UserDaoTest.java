package com.github.programmerrabbit.dao;

import com.github.programmerrabbit.entity.User;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * Generated by Code-Generator.
 * Author: ProgrammerRabbit
 * http://github.com/ProgrammerRabbit
 */
// TODO add spring4junit support
public class UserDaoTest {
    @Resource
    private UserDao dao;

    private final static int ID = 1; // TODO modify the value

    @Test
    public void insert() throws Exception {
        User entity = new User();
        // TODO fill entity
        dao.insert(entity);
    }

    @Test
    public void getById() throws Exception {
        User entity = dao.getById(ID);
        System.out.println(entity.toString());
    }

    @Test
    public void getByField() throws Exception {
        List<User> entityList = dao.getByField("", ""); // TODO fill fieldName and fieldValue
        for (User entity : entityList) {
            System.out.println(entity.toString());
        }
    }

    @Test
    public void updateById() throws Exception {
        User entity = new User();
        // TODO fill new entity
        dao.updateById(ID, entity);
    }

    @Test
    public void deleteById() throws Exception {
        dao.deleteById(ID);
    }

}