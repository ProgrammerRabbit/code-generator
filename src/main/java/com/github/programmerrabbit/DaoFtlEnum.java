package com.github.programmerrabbit;

/**
 * Created by Rabbit on 2016/12/19.
 */
public enum  DaoFtlEnum {
    DAO_FTL("dao.ftl"),
    DAO_TEST_FTL("daoTest.ftl"),
    MAPPER_FTL("mapper.ftl"),
    DTO_FTL("dto.ftl"),
    SERVICE_FTL("service.ftl"),
    SERVICE_IMPL_FTL("impl.ftl");

    private String fileName;

    DaoFtlEnum(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }
}
