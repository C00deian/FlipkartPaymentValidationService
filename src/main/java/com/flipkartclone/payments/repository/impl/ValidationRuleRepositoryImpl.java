package com.flipkartclone.payments.repository.impl;

import com.flipkartclone.payments.entity.ValidationRule;
import com.flipkartclone.payments.repository.ValidationRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ValidationRuleRepositoryImpl implements ValidationRuleRepository {

    private static final String FIND_ACTIVE_RULES_SQL = """
            SELECT id, validatorName, isActive, priority, creationDate
            FROM validations.validation_rules
            WHERE isActive = :isActive
            ORDER BY priority ASC, id ASC
            """;

    private static final RowMapper<ValidationRule> VALIDATION_RULE_ROW_MAPPER = (rs, rowNum) -> {
        Timestamp creationDate = rs.getTimestamp("creationDate");

        return ValidationRule.builder()
                .id(rs.getInt("id"))
                .validatorName(rs.getString("validatorName"))
                .isActive(rs.getBoolean("isActive"))
                .priority(rs.getShort("priority"))
                .creationDate(creationDate == null ? null : creationDate.toLocalDateTime())
                .build();
    };

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<ValidationRule> findActiveRulesOrderedByPriority() {
        MapSqlParameterSource params = new MapSqlParameterSource("isActive", true);
        return jdbcTemplate.query(FIND_ACTIVE_RULES_SQL, params, VALIDATION_RULE_ROW_MAPPER);
    }
}

