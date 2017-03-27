package ${daoPackage};

import ${entityFullName};
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * Generated by Code-Generator.
 * Author: ProgrammerRabbit
 * http://github.com/ProgrammerRabbit
 */
// TODO add spring4junit support
public class ${entityName}DaoTest {
    @Resource
    private ${entityName}Dao dao;

    private final static int ID = 1; // TODO modify the value

    @Test
    public void insert() throws Exception {
        ${entityName} entity = new ${entityName}();
        // TODO fill entity
        dao.insert(entity);
        System.out.println(entity.getId());
    }

    @Test
    public void getById() throws Exception {
        ${entityName} entity = dao.getById(ID);
        System.out.println(entity.toString());
    }

    @Test
    public void getByField() throws Exception {
        List<${entityName}> entityList = dao.getByField("", ""); // TODO fill fieldName and fieldValue
        for (${entityName} entity : entityList) {
            System.out.println(entity.toString());
        }
    }

    @Test
    public void updateById() throws Exception {
        ${entityName} entity = new ${entityName}();
        // TODO fill new entity
        dao.updateById(ID, entity);
    }

    @Test
    public void deleteById() throws Exception {
        dao.deleteById(ID);
    }

}