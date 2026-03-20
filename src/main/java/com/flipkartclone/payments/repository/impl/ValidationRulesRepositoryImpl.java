package com.flipkartclone.payments.repository.impl;

import com.flipkartclone.payments.repository.ValidationRulesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
@Slf4j
public class ValidationRulesRepositoryImpl implements ValidationRulesRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SELECT_ACTIVE_RULES_SQL = """
            SELECT validatorName
            FROM validations.validation_rules
            WHERE isActive = true
            ORDER BY priority ASC
            """;

    @Override
    public List<String> loadActiveValidatorNamesOrderedByPriority() {
        log.debug("Loading active validator names ordered by priority");

        return namedParameterJdbcTemplate.query(
                SELECT_ACTIVE_RULES_SQL,
                new MapSqlParameterSource(),
                (rs, rowNum) -> rs.getString("validatorName")
        );
    }
}
