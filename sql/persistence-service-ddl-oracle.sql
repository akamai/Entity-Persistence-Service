SET serveroutput ON
DECLARE
  CR       VARCHAR2(100);
  tabCount NUMBER;
BEGIN
  DBMS_OUTPUT.ENABLE(1000000);
  CR := 'CR #foo';
  
  --Begin Creating ENTITIES
  SELECT COUNT(*)
  INTO tabCount
  FROM all_tables
  WHERE table_name='ENTITIES';
  
  IF tabCount     = 0 THEN
    EXECUTE IMMEDIATE( 'CREATE TABLE ENTITIES
      (   
        WORKSPACE   VARCHAR2(256 CHAR) NOT NULL ENABLE, 
        COLLECTION  VARCHAR2(256 CHAR) NOT NULL ENABLE,
        GUID        VARCHAR2(64 CHAR) NOT NULL ENABLE,
        UPDATED     TIMESTAMP(6) DEFAULT systimestamp NOT NULL ENABLE, 
        PROPERTIES  CLOB NOT NULL ENABLE, 
        DELETED     CHAR(1) DEFAULT ''F'' NOT NULL CHECK(DELETED IN (''T'', ''F'')) ENABLE, 
        PRIMARY KEY (WORKSPACE, COLLECTION, GUID, UPDATED) ENABLE
      )' );
    DBMS_OUTPUT.PUT_LINE(CR || ' created table ENTITIES');
    -- create index --
    /*EXECUTE IMMEDIATE( 'CREATE INDEX entities_index ON ENTITIES
      (PARTITION, SELF_URI, UPDATED, DELETED)' );
    DBMS_OUTPUT.PUT_LINE(CR || ' created index ENTITIES_INDEX');*/
  ELSE
    DBMS_OUTPUT.PUT_LINE(CR || ' ENTITIES table already exists');
  END IF;
  --End Creating ENTITIES
  
  --Begin Creating ENTITY_PROPERTIES
  SELECT COUNT(*)
  INTO tabCount
  FROM all_tables
  WHERE table_name='ENTITY_PROPERTIES';
  
  IF tabCount     = 0 THEN
    EXECUTE IMMEDIATE( 'CREATE TABLE ENTITY_PROPERTIES
      (
        WORKSPACE        VARCHAR2(256 CHAR) NOT NULL ENABLE, 
        COLLECTION       VARCHAR2(256 CHAR) NOT NULL ENABLE,
        GUID             VARCHAR2(64 CHAR) NOT NULL ENABLE,
        UPDATED          TIMESTAMP(6) DEFAULT systimestamp NOT NULL ENABLE, 
        PROPERTY_NAME    VARCHAR2(256 CHAR) NOT NULL ENABLE,
        PROPERTY_VALUE   CLOB NOT NULL ENABLE, 
        FOREIGN KEY (WORKSPACE, COLLECTION, GUID, UPDATED) REFERENCES ENTITIES (WORKSPACE, COLLECTION, GUID, UPDATED) INITIALLY DEFERRED DEFERRABLE ENABLE, 
        PRIMARY KEY (WORKSPACE, COLLECTION, GUID, PROPERTY_NAME) ENABLE
      )' );
    -- http://infolab.stanford.edu/~ullman/fcdb/oracle/or-triggers.html#deferring%20constraint%20checking
    DBMS_OUTPUT.PUT_LINE(CR || ' created table ENTITY_PROPERTIES');
    -- create index --
    /*EXECUTE IMMEDIATE( 'CREATE INDEX entity_properties_index ON ENTITY_PROPERTIES
      (PARTITION, SELF_URI, UPDATED, PROPERTY_NAME)' );
    DBMS_OUTPUT.PUT_LINE(CR || ' created index ENTITY_PROPERTIES_INDEX');*/
  ELSE
    DBMS_OUTPUT.PUT_LINE(CR || ' ENTITY_PROPERTIES table already exists');
  END IF;
  --End Creating ENTITY_PROPERTIES
  
  COMMIT;
END;
/