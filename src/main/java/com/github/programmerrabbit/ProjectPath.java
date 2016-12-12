package com.github.programmerrabbit;

import lombok.Data;

/**
 * Created by Rabbit on 2016/12/12.
 */
@Data
public class ProjectPath {
    public final static String DEFAULT_MAIN_JAVA_PATH = "src/main/java/";
    public final static String DEFAULT_TEST_JAVA_PATH = "src/test/java/";
    public final static String DEFAULT_MAIN_RESOURCES_PATH = "src/main/resources/";
    public final static String DEFAULT_TEST_RESOURCES_PATH = "src/test/resources/";

    private String mainJavaPath = DEFAULT_MAIN_JAVA_PATH;
    private String testJavaPath = DEFAULT_TEST_JAVA_PATH;
    private String mainResourcesPath = DEFAULT_MAIN_RESOURCES_PATH;
    private String testResourcesPath = DEFAULT_TEST_RESOURCES_PATH;

    public final static ProjectPath DEFAULT_PROJECT_PATH = new ProjectPath();
}
