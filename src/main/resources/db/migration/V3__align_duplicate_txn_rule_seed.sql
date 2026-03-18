UPDATE validations.validation_rules
SET isActive = false
WHERE validatorName = 'DUPLICATION_TXN_RULE';

INSERT IGNORE INTO validations.validation_rules (validatorName, isActive, priority)
VALUES ('DUPLICATE_TXN_RULE', true, 0);
