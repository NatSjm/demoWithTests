DELIMITER $$

CREATE TRIGGER trigger_set_gender_before_insert
    BEFORE INSERT ON users
    FOR EACH ROW
BEGIN
    IF LOCATE('Mr.', NEW.name) > 0 THEN
        SET NEW.gender = 'M';
END IF;

IF LOCATE('Mrs.', NEW.name) > 0 OR
       LOCATE('Ms.', NEW.name) > 0 OR
       LOCATE('Miss', NEW.name) > 0 THEN
        SET NEW.gender = 'F';
END IF;
END$$

CREATE TRIGGER trigger_set_gender_before_update
    BEFORE UPDATE ON users
    FOR EACH ROW
BEGIN
    IF LOCATE('Mr.', NEW.name) > 0 THEN
        SET NEW.gender = 'M';
END IF;

IF LOCATE('Mrs.', NEW.name) > 0 OR
       LOCATE('Ms.', NEW.name) > 0 OR
       LOCATE('Miss', NEW.name) > 0 THEN
        SET NEW.gender = 'F';
END IF;
END$$

DELIMITER ;
