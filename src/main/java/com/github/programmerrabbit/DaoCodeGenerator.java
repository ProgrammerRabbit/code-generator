package com.github.programmerrabbit;

import com.github.programmerrabbit.utils.MapUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

/**
 * Created by Rabbit on 2016/12/12.
 */
public class DaoCodeGenerator {
    private static Configuration configuration;

    private static Map<String, Object> map;

    public static void generateDaoCode(Class clazz, ProjectPath projectPath) {
        try {
            init(clazz, projectPath);
            generateDao(clazz, projectPath);
            generateDaoTest(clazz, projectPath);
            generateMapper(clazz, projectPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateDao(Class clazz, ProjectPath projectPath) {
        try {
            init(clazz, projectPath);

            Template template = configuration.getTemplate("dao.ftl");

            // create path
            String filePath = projectPath.getMainJavaPath() + String.valueOf(map.get("daoPackage")).replace('.', '/');
            new File(filePath).mkdirs();

            // create and render file
            File file = new File(filePath + "/" + map.get("entityName") + "Dao.java");
            process(template, file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateDaoTest(Class clazz, ProjectPath projectPath) {
        try {
            init(clazz, projectPath);

            Template template = configuration.getTemplate("daoTest.ftl");

            String filePath = projectPath.getTestJavaPath() + String.valueOf(map.get("daoPackage")).replace(".", "/");
            new File(filePath).mkdirs();

            File file = new File(filePath + "/" + map.get("entityName") + "DaoTest.java");
            process(template, file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateMapper(Class clazz, ProjectPath projectPath) {
        try {
            init(clazz, projectPath);

            Template template = configuration.getTemplate("mapper.ftl");

            String filePath = projectPath.getMainResourcesPath() + "mybatis";
            new File(filePath).mkdirs();

            File file = new File(filePath + "/" + map.get("entityName") + "Dao.xml");
            process(template, file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void init(Class clazz, ProjectPath projectPath) throws IOException {
        prepareMap(clazz);
        if (configuration == null) {
            configuration = new Configuration();
            configuration.setDirectoryForTemplateLoading(new File(projectPath.getMainResourcesPath() + "dao"));
        }
    }

    private static void prepareMap(Class clazz) {
        if (map == null) {
            map = MapUtils.newHashMap();

            // e.g. User
            map.put("entityName", clazz.getSimpleName());

            // e.g. com.github.programmerrabbit.entity.User
            String entityFullName = clazz.getName();
            map.put("entityFullName", entityFullName);

            // e.g. com.github.programmerrabbit.dao
            String entityPackage = entityFullName.substring(0, entityFullName.lastIndexOf('.'));
            String daoPackage = entityPackage.substring(0, entityPackage.lastIndexOf('.')) + ".dao";
            map.put("daoPackage", daoPackage);

            map.put("entityFields", clazz.getDeclaredFields());
        }
    }

    private static void process(Template template, File file) throws IOException, TemplateException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        Writer writer = new OutputStreamWriter(fileOutputStream);
        template.process(map, writer);
    }
}
