package com.epam.esm.repository.impl;

import com.epam.esm.repository.mapping.GiftCertificateMapping;
import com.epam.esm.repository.mapping.TagMapping;
import com.epam.esm.repository.metadata.GiftCertificateMetadata;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.query.processor.ComplexParamMapProcessor;
import com.epam.esm.repository.query.processor.UpdateQueryBuilder;
import com.epam.esm.repository.template.GiftCertificateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import static com.epam.esm.repository.query.holder.CertificateQueryHolder.DELETE_ENTRY;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.DETACH_ASSOCIATED_TAGS;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.FETCH_ASSOCIATED_TAGS;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.INSERT_INTO_M2M;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.READ_ALL;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.READ_BY_ID;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.READ_BY_NAME;
import static com.epam.esm.repository.query.processor.ComplexParamMapProcessor.PERCENT;
import static com.epam.esm.repository.query.processor.ComplexParamMapProcessor.DESCRIPTION_PART;
import static com.epam.esm.repository.query.processor.ComplexParamMapProcessor.NAME_PART;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {


    public static final int MIN_AFFECTED_ROWS = 1;


    private final JdbcTemplate jdbcOperations;
    private final GiftCertificateMapping certificateMapper;
    private final TagMapping tagMapper;

    @Autowired
    public GiftCertificateRepositoryImpl(JdbcTemplate jdbcOperations, GiftCertificateMapping mapper, TagMapping tagMapper) {
        this.jdbcOperations = jdbcOperations;
        this.certificateMapper = mapper;
        this.tagMapper = tagMapper;
    }

    @Override
    public GiftCertificate create(GiftCertificate object){
        SimpleJdbcInsertOperations simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcOperations);
        simpleJdbcInsert.withTableName("gift_certificate").usingGeneratedKeyColumns("gc_id").usingColumns("gc_name","gc_description","gc_price","gc_duration");
        Map<String,Object> params = new HashMap<>();
        params.put(GiftCertificateMetadata.NAME,object.getName());
        params.put(GiftCertificateMetadata.DESCRIPTION,object.getDescription());
        params.put(GiftCertificateMetadata.PRICE,object.getPrice());
        params.put(GiftCertificateMetadata.DURATION,object.getDuration());
        Number key = simpleJdbcInsert.executeAndReturnKey(params);
        return getByID(key.longValue()).get();
    }

    @Override
    public List<GiftCertificate> readAll(Optional<Long> limit,Optional<Long> offset) {
        return jdbcOperations.query(READ_ALL, certificateMapper,limit,offset);
    }

    @Override
    public boolean update(GiftCertificate object,long id) {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcOperations);
        return template.update(UpdateQueryBuilder.buildUpdateQuery(object, id), UpdateQueryBuilder.getUpdateParams(object)) >= MIN_AFFECTED_ROWS;
    }

    @Override
    public Optional<GiftCertificate> getByID(long ID){
        try {
            return Optional.ofNullable(jdbcOperations.queryForObject(READ_BY_ID, certificateMapper, ID));
        }
        catch (DataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteByID(long ID) {
        return jdbcOperations.update(DELETE_ENTRY,ID) >= MIN_AFFECTED_ROWS;
    }

    @Override
    public void linkAssociatedTags(long certificateID, List<Tag> tags) {
        for(Tag tag:tags) {
            if(tag!=null) {
                jdbcOperations.update(con -> {
                    PreparedStatement stmt = con.prepareStatement(INSERT_INTO_M2M, Statement.RETURN_GENERATED_KEYS);
                    stmt.setLong(1, tag.getId());
                    stmt.setLong(2, certificateID);
                    return stmt;
                });
            }
        }
    }

    @Override
    public boolean detachAssociatedTags(long certificateID) {
        return jdbcOperations.update(DETACH_ASSOCIATED_TAGS,certificateID) >= MIN_AFFECTED_ROWS;
    }

    @Override
    public List<Tag> fetchAssociatedTags(long certificateID) {
        return jdbcOperations.query(FETCH_ASSOCIATED_TAGS,tagMapper,certificateID);
    }

    @Override
    public List<GiftCertificate> handleParametrizedRequest(MultiValueMap<String,String> params){
        String query = ComplexParamMapProcessor.buildQuery(params);
        prepareParamsToSearchStatement(params);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcOperations);
        return template.query(query, params,certificateMapper);
    }

    @Override
    public Optional<GiftCertificate> getByName(String name){
        try {
            return Optional.ofNullable(jdbcOperations.queryForObject(READ_BY_NAME, certificateMapper, name));
        }
        catch (DataAccessException e){
            return Optional.empty();
        }
    }

    private void prepareParamsToSearchStatement(MultiValueMap<String,String> params){
        if(params.containsKey(NAME_PART)){
            params.set(NAME_PART,PERCENT+params.getFirst(NAME_PART)+PERCENT);
        }
        if(params.containsKey(DESCRIPTION_PART)){
            params.set(DESCRIPTION_PART,PERCENT+params.getFirst(DESCRIPTION_PART)+PERCENT);
        }
        if(params.containsKey(ComplexParamMapProcessor.TAG_NAME)){
        List<String> tags = params.get(ComplexParamMapProcessor.TAG_NAME);
        int iter = 1;
        for(String tagName:tags){
            params.set(ComplexParamMapProcessor.TAG_NAME+iter, tagName);
            iter++;
        }
        params.remove(ComplexParamMapProcessor.TAG_NAME);
    }
    }

}
