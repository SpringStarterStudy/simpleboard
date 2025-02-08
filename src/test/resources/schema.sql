CREATE TABLE `user` (
    user_id BIGINT NOT NULL COMMENT 'AUTO_INCREMENT',
    name VARCHAR(50) NOT NULL COMMENT 'UNIQUE',
    email VARCHAR(100) NOT NULL COMMENT 'UNIQUE',
    password VARCHAR(255) NULL COMMENT '소셜 로그인 사용자의 경우 NULL 허용',
    cell_phone VARCHAR(255) NOT NULL,
    is_enabled BOOLEAN NOT NULL DEFAULT true COMMENT '사용자 계정이 현재 사용 가능한 상태인지 아닌지',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    login_type ENUM('LOCAL', 'KAKAO') NOT NULL COMMENT 'LOCAL(일반 회원가입), KAKAO 등 소셜 로그인 구분',
    PRIMARY KEY (user_id)
);

CREATE TABLE post (
    post_id	BIGINT	NOT NULL	COMMENT 'AUTO_INCREMENT',
    user_id	BIGINT	NOT NULL	COMMENT 'AUTO_INCREMENT',
    post_title	VARCHAR(255)	NOT NULL,
    post_content	TEXT	NOT NULL,
    created_at TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP,
    updated_at	TIMESTAMP	NULL	DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at	TIMESTAMP	NULL,
    view_count	BIGINT	NOT NULL	DEFAULT 0,
    PRIMARY KEY(post_id)
);

CREATE TABLE reaction (
    reaction_id	BIGINT	NOT NULL	COMMENT 'AUTO_INCREMENT',
    user_id	BIGINT	NOT NULL	COMMENT 'AUTO_INCREMENT',
    target_id	BIGINT	NOT NULL,
    target_type	ENUM('POST', 'COMMENT')	NOT NULL	COMMENT 'POST, COMMENT',
    reaction_type	ENUM('LIKE', 'DISLIKE')	NOT NULL	COMMENT 'LIKE, DISLIKE',
    is_active	BOOLEAN	NOT NULL	DEFAULT true,
    PRIMARY KEY(reaction_id)
);

ALTER TABLE `user`  MODIFY COLUMN user_id bigint auto_increment NOT NULL;

ALTER TABLE post  MODIFY COLUMN post_id bigint auto_increment NOT NULL;

ALTER TABLE reaction  MODIFY COLUMN reaction_id bigint auto_increment NOT NULL;

ALTER TABLE post ADD CONSTRAINT FK_user_TO_post_1 FOREIGN KEY (user_id) REFERENCES `user`(user_id);

ALTER TABLE reaction ADD CONSTRAINT FK_user_TO_reaction_1 FOREIGN KEY(user_id) REFERENCES `user`(user_id);