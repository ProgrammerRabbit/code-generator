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
    private static Configuration configuration = null;

    private static Map<String, String> map = MapUtils.newHashMap();

    private final static String DAO_RESOURCES = "src/main/resources/dao";
    private final static String JAVA_PATH = "src/main/java/";

    static {
        try {
            configuration = new Configuration();
            configuration.setDirectoryForTemplateLoading(new File(DAO_RESOURCES));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateDaoCode(Class clazz) {
        try {
            prepareMap(clazz);
            generateDao();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void prepareMap(Class clazz) {
        String entityName = clazz.getSimpleName();
        map.put("entityName", entityName);

        String entityFullName = clazz.getName();
        map.put("entityFullName", entityFullName);

        String entityPackage = entityFullName.substring(0, entityFullName.lastIndexOf('.'));
        String daoPackage = entityPackage.substring(0, entityPackage.lastIndexOf('.')) + ".dao";
        map.put("daoPackage", daoPackage);
    }

    private static void generateDao() throws IOException, TemplateException {
        Template template = configuration.getTemplate("dao.ftl");

        // create path
        String filePath = JAVA_PATH + map.get("daoPackage").replace('.', '/');
        File path = new File(filePath);
        path.mkdirs();

        // create and render file
        File file = new File(filePath + "/" + map.get("entityName") + "Dao.java");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        Writer writer = new OutputStreamWriter(fileOutputStream);
        template.process(map, writer);
    }
}
