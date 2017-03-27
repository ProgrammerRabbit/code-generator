package ${dtoPackage};

import ${entityFullName};
import lombok.Data; // or generate getters and setters by IDE

import java.io.Serializable;

/**
 * Generated by Code-Generator.
 * Author: ProgrammerRabbit
 * http://github.com/ProgrammerRabbit
 */
@Data
// TODO to import packages after this Class generated.
public class ${entityName}Dto implements Serializable {
    <#list entityFields as entityField>
    private ${entityField.type.simpleName} ${entityField.name};
    </#list>

    public ${entityName} toEntity() {
        ${entityName} entity = new ${entityName}();
        // ATTENTION first param is source, second param is target.
        BeanUtils.copyProperties(this, entity);
        return entity;
    }

    public static ${entityName}Dto fromEntity(${entityName} entity) {
        ${entityName}Dto dto = new ${entityName}Dto();
        // ATTENTION first param is source, second param is target.
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
