DELIMITER $$

CREATE TRIGGER trigger_set_uppercase_name_before_insert
    BEFORE INSERT ON users
    FOR EACH ROW
BEGIN
    IF NEW.country LIKE '%Ukraine%' THEN
        SET NEW.name = UPPER(NEW.name);
        -- SET NEW.is_valid = TRUE;
END IF;
END$$

CREATE TRIGGER trigger_set_uppercase_name_before_update
    BEFORE UPDATE ON users
    FOR EACH ROW
BEGIN
    IF NEW.country LIKE '%Ukraine%' THEN
        SET NEW.name = UPPER(NEW.name);
END IF;
END$$

DELIMITER ;
