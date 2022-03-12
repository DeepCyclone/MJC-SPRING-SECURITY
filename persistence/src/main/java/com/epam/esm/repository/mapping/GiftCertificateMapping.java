package com.epam.esm.repository.mapping;

import com.epam.esm.repository.metadata.GiftCertificateMetadata;
import com.epam.esm.repository.model.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;

@Component
public class GiftCertificateMapping implements RowMapper<GiftCertificate> {
    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GiftCertificate.builder().
                id(rs.getLong(GiftCertificateMetadata.ID)).
                name(rs.getString(GiftCertificateMetadata.NAME)).
                description(rs.getString(GiftCertificateMetadata.DESCRIPTION)).
                price(rs.getBigDecimal(GiftCertificateMetadata.PRICE)).
                duration(rs.getInt(GiftCertificateMetadata.DURATION)).
                createDate(rs.getTimestamp(GiftCertificateMetadata.CREATE_DATE,new GregorianCalendar())).
                lastUpdateDate(rs.getTimestamp(GiftCertificateMetadata.LAST_UPDATE_DATE,new GregorianCalendar())).
                build();
    }
}
