-- user 테이블 외래키 on delete CASCADE 설정
ALTER TABLE user_social
DROP FOREIGN KEY FK_user_TO_user_social_1,
ADD CONSTRAINT FK_user_TO_user_social_cascade
FOREIGN KEY (user_id) REFERENCES user(user_id)
ON DELETE CASCADE;

ALTER TABLE post
DROP FOREIGN KEY FK_user_TO_post_1,
ADD CONSTRAINT FK_user_TO_post_cascade
FOREIGN KEY (user_id) REFERENCES user(user_id)
ON DELETE CASCADE;

ALTER TABLE comment
DROP FOREIGN KEY FK_user_TO_comment_1,
ADD CONSTRAINT FK_user_TO_comment_cascade
FOREIGN KEY (user_id) REFERENCES user(user_id)
ON DELETE CASCADE;

ALTER TABLE reaction
DROP FOREIGN KEY FK_user_TO_reaction_1,
ADD CONSTRAINT FK_user_TO_reaction_cascade
FOREIGN KEY (user_id) REFERENCES user(user_id)
ON DELETE CASCADE;

-- user_social 테이블에서 access_token 컬럼 제거
ALTER TABLE user_social DROP COLUMN access_token;

-- user 테이블에서 cell_phone 컬럼을 "NULL 허용"으로 변경
ALTER TABLE user MODIFY COLUMN cell_phone VARCHAR(255) NULL;