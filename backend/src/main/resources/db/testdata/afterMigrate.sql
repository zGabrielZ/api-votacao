-- Clean test data and reset sequences to keep each scenario isolated.
TRUNCATE TABLE TB_ASSOCIATE RESTART IDENTITY CASCADE;

-- Base data for vote and associate integration tests.
INSERT INTO TB_ASSOCIATE (ID_EXTERNAL_UUID, NAME, EMAIL, PASSWORD, DOCUMENT_NUMBER, CREATED_AT, UPDATED_AT)
VALUES
  ('eac019fd-bbbb-4c13-a5c0-3e83f2b2fd82', 'Maria Silva', 'maria@email.com', '123456', '15245705087', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('38831c6f-1e69-4434-abb5-767fbe9276dd', 'Joao Souza', 'joao@email.com', '123456', '19371073020', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('d5a25e69-e7ea-45ba-ae4e-4f0fbbb1b59d', 'Ana Costa', 'ana@email.com', '123456', '65303559017', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

