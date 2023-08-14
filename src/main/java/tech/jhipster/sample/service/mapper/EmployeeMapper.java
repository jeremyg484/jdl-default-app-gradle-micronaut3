package tech.jhipster.sample.service.mapper;

import org.mapstruct.*;
import tech.jhipster.sample.domain.Department;
import tech.jhipster.sample.domain.Employee;
import tech.jhipster.sample.domain.GoldenBadge;
import tech.jhipster.sample.domain.SilverBadge;
import tech.jhipster.sample.domain.User;
import tech.jhipster.sample.service.dto.DepartmentDTO;
import tech.jhipster.sample.service.dto.EmployeeDTO;
import tech.jhipster.sample.service.dto.GoldenBadgeDTO;
import tech.jhipster.sample.service.dto.SilverBadgeDTO;
import tech.jhipster.sample.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "jsr330", uses = UserMapper.class)
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "manager", source = "manager", qualifiedByName = "employeeLastName")
    @Mapping(target = "sibag", source = "sibag", qualifiedByName = "silverBadgeName")
    @Mapping(target = "gobag", source = "gobag", qualifiedByName = "goldenBadgeName")
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentId")
    EmployeeDTO toDto(Employee s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("employeeLastName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastName", source = "lastName")
    EmployeeDTO toDtoEmployeeLastName(Employee employee);

    @Named("silverBadgeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SilverBadgeDTO toDtoSilverBadgeName(SilverBadge silverBadge);

    @Named("goldenBadgeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    GoldenBadgeDTO toDtoGoldenBadgeName(GoldenBadge goldenBadge);

    @Named("departmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentDTO toDtoDepartmentId(Department department);
}
