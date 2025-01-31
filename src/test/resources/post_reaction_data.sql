insert into `user`(`name`, email, `password`, cell_phone, login_type)
values('홍길동', 'hong@naver.com', '12345', '010-1234-5678', 'LOCAL');

set @user_id = last_insert_id();

insert into post(user_id, post_title, post_content)
values(@user_id, '제목입니다', '내용입니다');