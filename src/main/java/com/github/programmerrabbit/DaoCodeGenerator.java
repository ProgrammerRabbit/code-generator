package com.github.programmerrabbit;

import com.github.programmerrabbit.utils.MapUtils;
import com.github.programmerrabbit.utils.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

/**
 * MyBatis Dao Code Generator
 *
 * Created by Rabbit on 2016/12/12.
 */
public class DaoCodeGenerator {
    private static Configuration configuration;

    private static Map<String, Object> map;

    @Deprecated
    public static void generateDaoCode(Class clazz, ProjectPath projectPath) {
        generateDao(clazz, projectPath);
        generateDaoTest(clazz, projectPath);
        generateMapper(clazz, projectPath);
    }

    public static void generateCodeFromEntity(Class clazz, ProjectPath projectPath) {
        generateDaoCode(clazz, projectPath);
        generateDto(clazz, projectPath);
        generateService(clazz, projectPath);
    }

    public static void generateDao(Class clazz, ProjectPath projectPath) {
        try {
            init(clazz, projectPath);

            Template template = configuration.getTemplate(DaoFtlEnum.DAO_FTL.getFileName());

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

            Template template = configuration.getTemplate(DaoFtlEnum.DAO_TEST_FTL.getFileName());

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

            Template template = configuration.getTemplate(DaoFtlEnum.MAPPER_FTL.getFileName());

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

    public static void generateDto(Class clazz, ProjectPath projectPath) {
        try {
            init(clazz, projectPath);

            Template template = configuration.getTemplate(DaoFtlEnum.DTO_FTL.getFileName());

            // e.g. src/main/java/com/github/programmerrabbit/dto
            String filePath = projectPath.getMainJavaPath() + String.valueOf(map.get("dtoPackage")).replace('.', '/');
            new File(filePath).mkdirs();

            // e.g. src/main/java/com/github/programmerrabbit/dto/UserDto.java
            File file = new File(filePath + "/" + map.get("entityName") + "Dto.java");
            process(template, file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateService(Class clazz, ProjectPath projectPath) {
        try {
            init(clazz, projectPath);

            Template serviceTemplate = configuration.getTemplate(DaoFtlEnum.SERVICE_FTL.getFileName());
            Template serviceImplTemplate = configuration.getTemplate(DaoFtlEnum.SERVICE_IMPL_FTL.getFileName());

            // e.g. src/main/java/com/github/programmerrabbit/service
            String serviceFilePath = projectPath.getMainJavaPath() + String.valueOf(map.get("servicePackage")).replace('.', '/');
            new File(serviceFilePath).mkdirs();

            // e.g. src/main/java/com/github/programmerrabbit/service/impl/
            String serviceImplFilePath = serviceFilePath + "/" + projectPath.getServiceImplPath();
            new File(serviceImplFilePath).mkdirs();

            // e.g. src/main/java/com/github/programmerrabibt/service/UserService.java
            File serviceFile = new File(serviceFilePath + "/" + map.get("entityName") + "Service.java");
            process(serviceTemplate, serviceFile);

            // e.g. src/main/java/com/github/programmerrabbit/service/impl/UserServiceImpl.java
            String upperCaseImplPath = String.valueOf(map.get("upperCaseImplPath"));
            File serviceImplFile = new File(serviceImplFilePath + map.get("entityName") + "Service" + upperCaseImplPath + ".java");
            process(serviceImplTemplate, serviceImplFile);
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
            for (DaoFtlEnum daoFtlEnum : DaoFtlEnum.values()) {
                copyFileFromJar2Temp(clazz, projectPath, daoFtlEnum.getFileName());
            }
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
            String[] daoSubPaths = daoRelativePath.split("/");
            for (String daoSubPath : daoSubPaths) {
                if (!StringUtils.isNullOrEmpty(daoSubPath)) {
                    if ("..".equals(daoSubPath)) {
                        daoPackage = daoPackage.substring(0, daoPackage.lastIndexOf("."));
                    } else {
                        daoPackage = daoPackage + "." + daoSubPath;
                    }
                }
            }
            map.put("daoPackage", daoPackage);

            // e.g. com.github.programmerribbit.dto
            String dtoRelativePath = projectPath.getDtoRelativePath();
            String dtoPackage = entityPackage;
            String[] dtoSubPaths = dtoRelativePath.split("/");
            for (String dtoSubPath : dtoSubPaths) {
                if (!StringUtils.isNullOrEmpty(dtoSubPath)) {
                    if ("..".equals(dtoSubPath)) {
                        dtoPackage = dtoPackage.substring(0, dtoPackage.lastIndexOf("."));
                    } else {
                        dtoPackage = dtoPackage + "." + dtoSubPath;
                    }
                }
            }
            map.put("dtoPackage", dtoPackage);

            // e.g. com.github.programmerrabbit.service
            String serviceRelativePath = projectPath.getServiceRelativePath();
            String servicePackage = entityPackage;
            String[] serviceSubPaths = serviceRelativePath.split("/");
            for (String serviceSubPath : serviceSubPaths) {
                if (!StringUtils.isNullOrEmpty(serviceSubPath)) {
                    if ("..".equals(serviceSubPath)) {
                        servicePackage = servicePackage.substring(0, servicePackage.lastIndexOf("."));
                    } else {
                        servicePackage = servicePackage + "." + serviceSubPath;
                    }
                }
            }
            map.put("servicePackage", servicePackage);

            // e.g. impl
            String implPath = projectPath.getServiceImplPath().substring(0, projectPath.getServiceImplPath().length() - 1);
            map.put("implPath", implPath);

            // e.g. Impl
            String upperCaseImplPath = implPath.substring(0, 1).toUpperCase() + implPath.substring(1);
            map.put("upperCaseImplPath", upperCaseImplPath);

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
