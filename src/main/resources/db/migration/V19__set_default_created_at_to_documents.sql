CREATE OR REPLACE FUNCTION set_created_at()
    RETURNS TRIGGER
AS $$
BEGIN
    IF NEW.created_at IS NULL THEN
        NEW.created_at := CURRENT_TIMESTAMP;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_set_created_at
    BEFORE INSERT ON documents
    FOR EACH ROW
    EXECUTE FUNCTION set_created_at();