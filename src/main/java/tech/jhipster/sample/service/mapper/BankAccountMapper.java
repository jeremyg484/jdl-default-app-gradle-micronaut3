package tech.jhipster.sample.service.mapper;

import org.mapstruct.*;
import tech.jhipster.sample.domain.BankAccount;
import tech.jhipster.sample.domain.User;
import tech.jhipster.sample.service.dto.BankAccountDTO;
import tech.jhipster.sample.service.dto.UserDTO;

/**
 * Mapper for the entity {@link BankAccount} and its DTO {@link BankAccountDTO}.
 */
@Mapper(componentModel = "jsr330", uses = UserMapper.class)
public interface BankAccountMapper extends EntityMapper<BankAccountDTO, BankAccount> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    BankAccountDTO toDto(BankAccount s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
