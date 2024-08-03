CREATE OR REPLACE FUNCTION set_default_status()
    RETURNS TRIGGER
AS $$
BEGIN
    IF NEW.status IS NULL THEN
        NEW.status := 'Pending';
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_set_default_status
    BEFORE INSERT ON documents
    FOR EACH ROW
    EXECUTE FUNCTION set_default_status();