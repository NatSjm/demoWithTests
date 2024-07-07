DELIMITER $$

CREATE TRIGGER trigger_user_country_null
    BEFORE INSERT ON users
    FOR EACH ROW
BEGIN
    IF NEW.country IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Country cannot be null';
END IF;
END$$

CREATE TRIGGER trigger_user_country_null_update
    BEFORE UPDATE ON users
    FOR EACH ROW
BEGIN
    IF NEW.country IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Country cannot be null';
END IF;
END$$

DELIMITER ;

