-- 1. Abort if historical data violates the new business rule.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM TB_VOTE v
        JOIN TB_VOTING_SESSION s ON s.ID = v.ID_VOTING_SESSION
        GROUP BY s.ID_AGENDA, v.ID_ASSOCIATE
        HAVING COUNT(*) > 1
    ) THEN
        RAISE EXCEPTION 'Duplicate votes exist for the same agenda and associate. Resolve before migrating.';
    END IF;
END $$;

-- 2. Add and backfill agenda reference.
ALTER TABLE TB_VOTE ADD COLUMN ID_AGENDA BIGINT;

UPDATE TB_VOTE v
SET ID_AGENDA = s.ID_AGENDA
FROM TB_VOTING_SESSION s
WHERE s.ID = v.ID_VOTING_SESSION;

ALTER TABLE TB_VOTE ALTER COLUMN ID_AGENDA SET NOT NULL;

-- 3. Ensure the agenda recorded on a vote matches its session.
ALTER TABLE TB_VOTING_SESSION
    ADD CONSTRAINT tb_voting_session_id_agenda_uk
        UNIQUE (ID, ID_AGENDA);

ALTER TABLE TB_VOTE
    ADD CONSTRAINT tb_vote_session_agenda_fk
        FOREIGN KEY (ID_VOTING_SESSION, ID_AGENDA)
            REFERENCES TB_VOTING_SESSION (ID, ID_AGENDA);

ALTER TABLE TB_VOTE
    ADD CONSTRAINT tb_vote_agenda_fk
        FOREIGN KEY (ID_AGENDA)
            REFERENCES TB_AGENDA (ID);

-- 4. Enforce one vote per associate per agenda, across all sessions.
ALTER TABLE TB_VOTE
    ADD CONSTRAINT tb_vote_agenda_associate_unique
        UNIQUE (ID_AGENDA, ID_ASSOCIATE);

-- 5. Drop the old, now-redundant uniqueness constraint.
ALTER TABLE TB_VOTE
DROP CONSTRAINT tb_vote_voting_session_unique;