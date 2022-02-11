package com.epam.esm.repository.mapping;

import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.dto.response.TagResponseDto;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CertificateResponseDtoMapper implements ResultSetExtractor<List<GiftCertificateResponseDto>> {

    private GiftCertificateResponseDto gcResponseDto = null;

    private void giftCertificateStructureCreator(ResultSet rs,long gcID) throws SQLException{
        gcResponseDto = new GiftCertificateResponseDto();
        gcResponseDto.setId(gcID);
        gcResponseDto.setName(rs.getString("gc_name"));
        gcResponseDto.setDescription(rs.getString("gc_description"));
        gcResponseDto.setDuration(rs.getInt("gc_duration"));
        gcResponseDto.setPrice(rs.getBigDecimal("gc_price"));
        gcResponseDto.setCreateDate(new Date(rs.getTimestamp("gc_create_date").getTime()));
        gcResponseDto.setLastUpdateDate(new Date(rs.getTimestamp("gc_last_update_date").getTime()));
        gcResponseDto.setAssociatedTags(new ArrayList<>());
    }

    private TagResponseDto tagStructureCreator(ResultSet rs) throws SQLException {
        TagResponseDto tagResponseDto = new TagResponseDto();
        tagResponseDto.setId(rs.getLong("t_id"));
        tagResponseDto.setName(rs.getString("t_name"));
        return tagResponseDto;
    }

    @Override
    public List<GiftCertificateResponseDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
        final List<GiftCertificateResponseDto> gcResponseDtos = new ArrayList<>();
        while(rs.next()){
            long gcID = rs.getLong("gc_id");
            if(gcResponseDto == null || gcResponseDto.getId() != gcID){
                giftCertificateStructureCreator(rs,gcID);
                gcResponseDtos.add(gcResponseDto);
            }
            TagResponseDto tag = tagStructureCreator(rs);
            gcResponseDto.getAssociatedTags().add(tag);
        }
        return gcResponseDtos;
    }
}

