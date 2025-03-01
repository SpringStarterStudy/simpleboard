insert into `user`(`name`, email, `password`, cell_phone, login_type)
values('홍길동', 'hong@naver.com', '$2a$10$eXthWEeajRbGgRfvlfVBl.LlD6jDWoyAgyRSDa.FdRUTM4vfnYh86',
       '01012345678', 'LOCAL'); -- password = 1234567aA!

set @user_id = last_insert_id();

insert into post(user_id, post_title, post_content)
values(@user_id, '제목입니다', '내용입니다');