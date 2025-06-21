insert into forms (form_id , title, description, user_id, created_at, updated_at, form_version)
values (1, '햄버거 선호도 조사', '어떤 종류의 햄버거를 좋아하는가?', 'swnoh', now(), now(), 1);

insert into questions (question_id, question_text, form_id, question_type, created_at, updated_at, deleted, is_required)
values (1, '비프 or 치킨', 1, 'SINGLE_CHOICE', now(), now(), 'N', 'Y');

insert into options (option_id, option_text, question_id)
values (1, '비프', 1);
insert into options (option_id, option_text, question_id)
values (2, '치킨', 1);

insert into questions (question_id, question_text, form_id, question_type, created_at, updated_at, deleted, is_required)
values (2, '브랜드', 1, 'MULTIPLE_CHOICE', now(), now(), 'N', 'N');

insert into options (option_id, option_text, question_id)
values (3, '맥도날드', 2);
insert into options (option_id, option_text, question_id)
values (4, '맘스터치', 2);
insert into options (option_id, option_text, question_id)
values (5, '버거킹', 2);

insert into questions (question_id, question_text, form_id, question_type, created_at, updated_at, deleted, is_required)
values (3, '이유', 1, 'SHORT_ANSWER', now(), now(), 'N', 'Y');

insert into submissions (submission_id, form_id, user_id, form_version, created_at, updated_at)
values (10, 1, 'sungwoong', 1, now(), now());

insert into answers (answer_id, submission_id, question_id, option_id, answer_text)
values (10, 10, 1, 1, null);

insert into answers (answer_id, submission_id, question_id, option_id, answer_text)
values (16, 10, 3, null, 'ㅁㅁㅁㅁ');

insert into submissions (submission_id, form_id, user_id, form_version, created_at, updated_at)
values (11, 1, 'sungwoong', 1, now(), now());

insert into answers (answer_id, submission_id, question_id, option_id, answer_text)
values (11, 11, 1, 2, null);

insert into submissions (submission_id, form_id, user_id, form_version, created_at, updated_at)
values (12, 1, 'sungwoong', 1, now(), now());

insert into answers (answer_id, submission_id, question_id, option_id, answer_text)
values (12, 12, 2, 2, null);

insert into answers (answer_id, submission_id, question_id, option_id, answer_text)
values (13, 12, 2, 3, null);

insert into answers (answer_id, submission_id, question_id, option_id, answer_text)
values (14, 12, 2, 4, null);

insert into answers (answer_id, submission_id, question_id, option_id, answer_text)
values (15, 12, 3, null, '마마마마마');