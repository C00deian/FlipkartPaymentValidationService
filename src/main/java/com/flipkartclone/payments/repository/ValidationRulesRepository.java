package com.flipkartclone.payments.repository;


import java.util.List;

public interface ValidationRulesRepository {

    List<String> loadActiveValidatorNamesOrderedByPriority();

}

