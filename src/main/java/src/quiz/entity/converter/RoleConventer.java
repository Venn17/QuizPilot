package src.quiz.entity.converter;

import src.quiz.entity.Role;

import javax.persistence.AttributeConverter;

public class RoleConventer implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {

        return role.shortName;
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {

        return Role.fromShortName(dbData);
    }
}
