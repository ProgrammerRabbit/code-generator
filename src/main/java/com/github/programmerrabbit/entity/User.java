package com.github.programmerrabbit.entity;

import com.github.programmerrabbit.DaoCodeGenerator;
import com.github.programmerrabbit.ProjectPath;
import com.github.programmerrabbit.test.Demo;
import lombok.Data;

/**
 * Created by Rabbit on 2016/12/13.
 */
@Data
@Demo
public class User {
    private String username;
    private String password;

    public static void main(String[] args) {
        DaoCodeGenerator.generateDaoCode(User.class, ProjectPath.DEFAULT_PROJECT_PATH);
    }
}
