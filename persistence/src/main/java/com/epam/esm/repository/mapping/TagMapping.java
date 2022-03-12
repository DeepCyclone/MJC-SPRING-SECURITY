package com.epam.esm.repository.mapping;

import com.epam.esm.repository.metadata.TagMetadata;
import com.epam.esm.repository.model.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagMapping implements RowMapper<Tag> {
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getLong(TagMetadata.ID));
        tag.setName(rs.getString(TagMetadata.NAME));
        return tag;
    }
}
