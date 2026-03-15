package com.flipkartclone.payments.repository;

import com.flipkartclone.payments.entity.ValidationRuleParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValidationRuleParamRepository extends JpaRepository<ValidationRuleParam, Integer> {


}

