create table if not exists GENRES
(
    GENRE_ID   BIGINT auto_increment,
    GENRE_NAME CHARACTER VARYING(50) not null,
    constraint "GENRE_pk"
        primary key (GENRE_ID)
);

create table if not exists MPA
(
    MPA_ID   BIGINT auto_increment,
    MPA_NAME CHARACTER VARYING(50) not null,
    constraint "MPA_pk"
        primary key (MPA_ID)
);

create table if not exists FILMS
(
    FILM_ID     BIGINT auto_increment,
    FILM_NAME   CHARACTER VARYING(50) not null,
    DESCRIPTION CHARACTER VARYING(50),
    RELEASEDATE DATE                  not null,
    DURATION    INTEGER               not null,
    RATE        BIGINT default 0      not null,
    MPA_ID      BIGINT                not null,
    constraint "FILMS_pk"
        primary key (FILM_ID),
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create table if not exists FILM_GENRES
(
    FILM_ID  BIGINT not null,
    GENRE_ID BIGINT not null,
    constraint "FILM_GENRES_FILMS_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS
            on update cascade on delete cascade,
    constraint "FILM_GENRES_GENRE_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRES
            on update cascade on delete cascade
);

create table if not exists USERS
(
    USER_ID   BIGINT auto_increment
        primary key,
    EMAIL     CHARACTER VARYING(50) not null,
    LOGIN     CHARACTER VARYING(50) not null,
    USER_NAME CHARACTER VARYING(50),
    BIRTHDAY  DATE
);

create table if not exists FRIENDS
(
    USER_ID   BIGINT not null,
    FRIEND_ID BIGINT not null,
    STATUS    BOOLEAN default FALSE,
    constraint FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on update cascade on delete cascade,
    constraint FRIENDS_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
            on update cascade on delete cascade
);

create table if not exists LIKES
(
    FILM_ID BIGINT not null,
    USER_ID BIGINT not null,
    constraint LIKES_FILMS_FILM_ID
        foreign key (FILM_ID) references FILMS
            on update cascade on delete cascade,
    constraint LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on update cascade on delete cascade
);

