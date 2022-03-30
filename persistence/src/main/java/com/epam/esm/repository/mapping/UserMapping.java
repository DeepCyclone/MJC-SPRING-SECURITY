package com.epam.esm.repository.mapping;

import com.epam.esm.repository.metadata.UserMetadata;
import com.epam.esm.repository.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMapping implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong(UserMetadata.ID));
        user.setName(rs.getString(UserMetadata.NAME));
        return user;
    }
    
}
