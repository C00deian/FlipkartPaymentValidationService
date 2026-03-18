package com.flipkartclone.payments.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ValidationRuleParamRepository {

    private static final String FIND_PARAMS_BY_VALIDATOR_NAMES_SQL = """
            SELECT validatorName, paramName, paramValue
            FROM validations.validation_rules_params
            WHERE validatorName IN (:validatorNames)
            ORDER BY id ASC
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Map<String, Map<String, String>> findParamsByValidatorNames(List<String> validatorNames) {
        if (validatorNames == null || validatorNames.isEmpty()) {
            return Map.of();
        }

        MapSqlParameterSource params = new MapSqlParameterSource("validatorNames", validatorNames);

        return jdbcTemplate.query(FIND_PARAMS_BY_VALIDATOR_NAMES_SQL, params, rs -> {
            Map<String, Map<String, String>> paramsByValidator = new LinkedHashMap<>();
            while (rs.next()) {
                paramsByValidator
                        .computeIfAbsent(rs.getString("validatorName"), ignored -> new LinkedHashMap<>())
                        .put(rs.getString("paramName"), rs.getString("paramValue"));
            }
            return paramsByValidator;
        });
    }
}
