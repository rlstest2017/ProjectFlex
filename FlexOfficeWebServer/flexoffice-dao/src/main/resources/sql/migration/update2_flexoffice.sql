
ALTER TABLE ONLY rooms
    ADD CONSTRAINT rooms_user_id_key UNIQUE (user_id);
