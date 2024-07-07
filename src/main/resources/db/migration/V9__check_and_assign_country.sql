DELIMITER $$

CREATE TRIGGER trigger_check_and_assign_country_insert
    BEFORE INSERT ON users
    FOR EACH ROW
BEGIN
    IF NEW.country IS NULL THEN
        SET NEW.country = CASE
                              WHEN RAND() < 0.33 THEN 'Ukraine'
                              WHEN RAND() < 0.66 THEN 'United Kingdom'
                              ELSE 'United States of America'
END;
END IF;
END$$

CREATE TRIGGER trigger_check_and_assign_country_update
    BEFORE UPDATE ON users
    FOR EACH ROW
BEGIN
    IF NEW.country IS NULL THEN
        SET NEW.country = CASE
                              WHEN RAND() < 0.33 THEN 'Ukraine'
                              WHEN RAND() < 0.66 THEN 'United Kingdom'
                              ELSE 'United States of America'
END;
END IF;
END$$

DELIMITER ;
