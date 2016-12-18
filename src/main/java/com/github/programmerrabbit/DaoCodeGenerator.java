package com.github.programmerrabbit;

import com.github.programmerrabbit.utils.MapUtils;
import com.github.programmerrabbit.utils.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

/**
 * Created by Rabbit on 2016/12/12.
 */
public class DaoCodeGenerator {
    private final static String DAO_FTL_NAME = "dao.ftl";
    private final static String DAO_TEST_FTL_NAME = "daoTest.ftl";
    private final static String MAPPER_FTL_NAME = "mapper.ftl";

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

            Template template = configuration.getTemplate(DAO_FTL_NAME);

            // e.g. src/main/java/com/github/programmerrabbit/dao
            String filePath = projectPath.getMainJavaPath() + String.valueOf(map.get("daoPackage")).replace('.', '/');
            new File(filePath).mkdirs();

            // e.g. src/main/java/com/github/programmerrabbit/dao/UserDao.java
            File file = new File(filePath + "/" + map.get("entityName") + "Dao.java");
            process(template, file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateDaoTest(Class clazz, ProjectPath projectPath) {
        try {
            init(clazz, projectPath);

            Template template = configuration.getTemplate(DAO_TEST_FTL_NAME);

            // e.g. src/test/java/com/github/programmerrabbit/dao
            String filePath = projectPath.getTestJavaPath() + String.valueOf(map.get("daoPackage")).replace(".", "/");
            new File(filePath).mkdirs();

            // e.g. src/test/java/com/github/programmerrabbit/dao/UserDaoTest.java
            File file = new File(filePath + "/" + map.get("entityName") + "DaoTest.java");
            process(template, file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateMapper(Class clazz, ProjectPath projectPath) {
        try {
            init(clazz, projectPath);

            Template template = configuration.getTemplate(MAPPER_FTL_NAME);

            // e.g. src/main/resources/mybatis/
            String filePath = projectPath.getMainResourcesPath() + projectPath.getMyBatisConfigPath();
            new File(filePath).mkdirs();

            // e.g. src/main/resources/mybatis/UserDao.xml
            File file = new File(filePath + map.get("entityName") + "Dao.xml");
            process(template, file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void init(Class clazz, ProjectPath projectPath) throws IOException {
        prepareMap(clazz, projectPath);

        if (configuration == null) {
            configuration = new Configuration();
            // e.g. src/main/resources/temp/
            File tempPath = new File(projectPath.getMainResourcesPath() + projectPath.getFtlTempPath());
            tempPath.mkdirs();
            copyFileFromJar2Temp(clazz, projectPath, DAO_FTL_NAME);
            copyFileFromJar2Temp(clazz, projectPath, DAO_TEST_FTL_NAME);
            copyFileFromJar2Temp(clazz, projectPath, MAPPER_FTL_NAME);
            configuration.setDirectoryForTemplateLoading(tempPath);
        }
    }

    private static void prepareMap(Class clazz, ProjectPath projectPath) {
        if (map == null) {
            map = MapUtils.newHashMap();

            // e.g. User
            map.put("entityName", clazz.getSimpleName());

            // e.g. com.github.programmerrabbit.entity.User
            String entityFullName = clazz.getName();
            map.put("entityFullName", entityFullName);

            // e.g. com.github.programmerrabbit.entity
            String entityPackage = entityFullName.substring(0, entityFullName.lastIndexOf('.'));
            map.put("entityPackage", entityPackage);

            // e.g. com.github.programmerrabbit.dao
            String daoRelativePath = projectPath.getDaoRelativePath();
            String daoPackage = entityPackage;
            String[] paths = daoRelativePath.split("/");
            for (String path : paths) {
                if (!StringUtils.isNullOrEmpty(path)) {
                    if ("..".equals(path)) {
                        daoPackage = daoPackage.substring(0, daoPackage.lastIndexOf("."));
                    } else {
                        daoPackage = daoPackage + "." + path;
                    }
                }
            }
            map.put("daoPackage", daoPackage);

            map.put("entityFields", clazz.getDeclaredFields());
        }
    }

    private static void copyFileFromJar2Temp(Class clazz, ProjectPath projectPath, String fileName) throws IOException {
        File file = new File(projectPath.getMainResourcesPath() + projectPath.getFtlTempPath() + fileName);
        file.createNewFile();

        InputStream inputStream = clazz.getResourceAsStream("/dao/" + fileName);
        OutputStream outputStream = new FileOutputStream(file);

        int bytesRead;
        int bufferSize = 8192;
        byte[] buffer = new byte[bufferSize];
        while ((bytesRead = inputStream.read(buffer, 0, bufferSize)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();
    }

    private static void process(Template template, File file) throws IOException, TemplateException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        Writer writer = new OutputStreamWriter(fileOutputStream);
        template.process(map, writer);
    }
}
