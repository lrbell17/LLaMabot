-- create schemas
CREATE SCHEMA IF NOT EXISTS keycloak AUTHORIZATION postgres;
CREATE SCHEMA IF NOT EXISTS llamabot AUTHORIZATION postgres;

-- grant privileges
GRANT ALL ON SCHEMA keycloak TO postgres;
GRANT ALL ON SCHEMA llamabot TO postgres;
