package com.github.programmerrabbit;

import lombok.Data;

/**
 * Created by Rabbit on 2016/12/12.
 */
@Data
public class ProjectPath {
    private String mainJavaPath = "src/main/java/";
    private String mainResourcesPath = "src/main/resources/";
    private String testJavaPath = "src/test/java/";
    private String testResourcesPath = "src/test/resources/";

    private String myBatisConfigPath = "mybatis/";
    private String ftlTempPath = "temp/";

    private String daoRelativePath = "../dao/";
    private String dtoRelativePath = "../dto/";
    private String serviceRelativePath = "../service/";
    private String serviceImplPath = "impl/"; // under service package

    public final static ProjectPath DEFAULT_PROJECT_PATH = new ProjectPath();
}
