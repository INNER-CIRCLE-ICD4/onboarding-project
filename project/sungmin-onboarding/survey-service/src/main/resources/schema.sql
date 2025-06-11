create table t_survey (
    survey_id CHAR(13) not null,
    created_date timestamp(6),
    last_modified_date timestamp(6),
    survey_description VARCHAR(2000),
    survey_name VARCHAR(200) not null,
    primary key (survey_id)
);

create table t_survey_item (
    item_id CHAR(13) not null,
    survey_id CHAR(13),
    created_date timestamp(6),
    last_modified_date timestamp(6),
    is_deleted BOOLEAN not null,
    is_required BOOLEAN not null,
    item_description VARCHAR(2000),
    item_name VARCHAR(200) not null,
    item_type enum ('SHORT_TEXT', 'LONG_TEXT', 'SINGLE_SELECT', 'MULTIPLE_SELECT') not null,
    primary key (item_id),
    foreign key (survey_id) references t_survey
);

create table t_survey_item_option (
    option_id CHAR(13) not null,
    item_id CHAR(13),
    created_date timestamp(6),
    last_modified_date timestamp(6),
    is_deleted BOOLEAN not null,
    is_other_option BOOLEAN not null,
    option_name VARCHAR(200) not null,
    primary key (option_id),
    foreign key (item_id) references t_survey_item
);

create table t_survey_response (
    response_id CHAR(13) not null,
    survey_id CHAR(13),
    created_date timestamp(6),
    last_modified_date timestamp(6),
    response_user VARCHAR(100),
    primary key (response_id),
    foreign key (survey_id) references t_survey
);

create table t_survey_response_answer (
    answer_id CHAR(13) not null,
    response_id CHAR(13),
    item_id CHAR(13),
    created_date timestamp(6),
    last_modified_date timestamp(6),
    answer JSON,
    item_name VARCHAR(200) not null,
    primary key (answer_id),
    foreign key (response_id) references t_survey_response,
    foreign key (item_id) references t_survey_item
);

