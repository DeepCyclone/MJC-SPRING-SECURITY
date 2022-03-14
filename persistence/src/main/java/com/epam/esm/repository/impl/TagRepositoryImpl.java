package com.epam.esm.repository.impl;

import com.epam.esm.repository.mapping.GiftCertificateMapping;
import com.epam.esm.repository.mapping.TagMapping;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.template.TagRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import static com.epam.esm.repository.query.holder.TagQueryHolder.DELETE_BY_ID;
import static com.epam.esm.repository.query.holder.TagQueryHolder.FETCH_ASSOCIATED_CERTIFICATES;
import static com.epam.esm.repository.query.holder.TagQueryHolder.GET_BY_NAME;
import static com.epam.esm.repository.query.holder.TagQueryHolder.INSERT_INTO_BY_NAME;
import static com.epam.esm.repository.query.holder.TagQueryHolder.INSERT_INTO_WITH_ID;
import static com.epam.esm.repository.query.holder.TagQueryHolder.READ_ALL;
import static com.epam.esm.repository.query.holder.TagQueryHolder.READ_BY_ID;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {


    public static final int MIN_AFFECTED_ROWS = 1;

    private final JdbcTemplate jdbcTemplate;
    private final GiftCertificateMapping certificateMapper;
    private final TagMapping tagMapper;

    @Autowired
    public TagRepositoryImpl(JdbcTemplate jdbcTemplate, GiftCertificateMapping certificateMapper, TagMapping tagMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.certificateMapper = certificateMapper;
        this.tagMapper = tagMapper;
    }

    @Override
    public Tag create(Tag object) {
        KeyHolder holder = new GeneratedKeyHolder();
        if(object.getId() > 0) {
            jdbcTemplate.update(con -> {
                PreparedStatement stmt = con.prepareStatement(INSERT_INTO_WITH_ID, Statement.RETURN_GENERATED_KEYS);
                stmt.setLong(1, object.getId());
                stmt.setString(2, object.getName());
                return stmt;
            }, holder);
        }
        else if(object.getId() == 0){
            jdbcTemplate.update(con -> {
                PreparedStatement stmt = con.prepareStatement(INSERT_INTO_BY_NAME, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, object.getName());
                return stmt;
            }, holder);
        }
        if(holder.getKey()!=null) {
            return getByID(holder.getKey().longValue()).get();
        }
        else {
            if(object.getName()!=null && getByName(object.getName()).isPresent()){
                return getByName(object.getName()).get();
            }
            else {
                return getByID(object.getId()).get();
            }
        }
    }

    @Override
    public List<Tag> readAll(Optional<Long> limit,Optional<Long> offset) {
        return jdbcTemplate.query(READ_ALL,tagMapper,limit,offset);
    }

    @Override
    public boolean update(Tag object,long ID) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Tag> getByID(long ID) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(READ_BY_ID, tagMapper, ID));
        }
        catch (DataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteByID(long ID) {
        return fetchAssociatedCertificates(ID).isEmpty() &&
                jdbcTemplate.update(DELETE_BY_ID,ID) >= MIN_AFFECTED_ROWS;
    }


    @Override
    public Optional<Tag> getByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(GET_BY_NAME, tagMapper, name));
        }
        catch (DataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<GiftCertificate> fetchAssociatedCertificates(long tagID) {
        return jdbcTemplate.query(FETCH_ASSOCIATED_CERTIFICATES,certificateMapper,tagID);
    }
}
