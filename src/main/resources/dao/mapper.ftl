<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${daoPackage}.${entityName}Dao">

    <insert id="insert" parameterType="${entityFullName}">
        INSERT INTO ${entityName}
        (
        <#list entityFields as field>
            ${field.name}<#if field_has_next>,</#if>
        </#list>
        )
        VALUES
        (
        <#list entityFields as field>
            ${r'#{entity.'}${field.name}${r'}'}<#if field_has_next>,</#if>
        </#list>
        )
    </insert>

    <update id="updateById">
        UPDATE ${entityName}
        SET
        <#list entityFields as field>
            ${field.name} = ${r'#{entity.'}${field.name}${r'}'}<#if field_has_next>,</#if>
        </#list>
        WHERE
        id = ${r'#{id}'}
    </update>
</mapper>