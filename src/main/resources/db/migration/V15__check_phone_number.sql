CREATE OR REPLACE FUNCTION validate_phone_number()
    RETURNS TRIGGER
AS $$
BEGIN
    IF NEW.phone_number IS NOT NULL AND NEW.phone_number !~ '^\+?[0-9]{10,15}$' THEN
        RAISE EXCEPTION 'Invalid phone number format';
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validate_phone_number
    BEFORE INSERT OR UPDATE ON users
                         FOR EACH ROW
                         EXECUTE FUNCTION validate_phone_number();