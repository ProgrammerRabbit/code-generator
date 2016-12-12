package com.github.programmerrabbit.entity;

import com.github.programmerrabbit.DaoCodeGenerator;
import com.github.programmerrabbit.ProjectPath;
import lombok.Data;

/**
 * Created by Rabbit on 2016/12/12.
 */
@Data
public class User {
    private String username;
    private String password;

    public static void main(String[] args) {
        DaoCodeGenerator.generateDaoCode(User.class, ProjectPath.DEFAULT_PROJECT_PATH);
    }
}
