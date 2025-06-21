insert into forms (form_id , title, description, user_id, created_at, updated_at, form_version)
values (1, '햄버거 선호도 조사', '어떤 종류의 햄버거를 좋아하는가?', 'swnoh', now(), now(), 1);

insert into questions (question_id, question_text, form_id, question_type, created_at, updated_at, deleted, is_required)
values (1, '비프 or 치킨', 1, 'SINGLE_CHOICE', now(), now(), 'N', 'Y');

insert into options (option_text, question_id)
values ('비프', 1);
insert into options (option_text, question_id)
values ('치킨', 1);

insert into questions (question_id, question_text, form_id, question_type, created_at, updated_at, deleted, is_required)
values (2, '브랜드', 1, 'MULTIPLE_CHOICE', now(), now(), 'N', 'N');

insert into options (option_text, question_id)
values ('맥도날드', 2);
insert into options (option_text, question_id)
values ('맘스터치', 2);
insert into options (option_text, question_id)
values ('버거킹', 2);


insert into questions (question_id, question_text, form_id, question_type, created_at, updated_at, deleted, is_required)
values (3, '이유', 1, 'SHORT_ANSWER', now(), now(), 'N', 'Y');

