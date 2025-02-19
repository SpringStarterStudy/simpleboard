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

-- 전체 게시물 조회의 searchKeyword 풀텍스트 인덱스 사용
CREATE FULLTEXT INDEX idx_title_content ON post(post_title, post_content);

-- 게시물 조회시 조회수 업데이트에 따른 게시물 수정현상 방지
ALTER TABLE post
MODIFY COLUMN updated_at TIMESTAMP NULL;