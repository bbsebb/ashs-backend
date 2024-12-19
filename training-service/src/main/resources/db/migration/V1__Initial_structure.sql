-- Séquences
CREATE SEQUENCE public.coach_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.hall_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.team_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.training_session_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Tables avec contraintes
CREATE TABLE public.coach (
                              id BIGINT NOT NULL DEFAULT nextval('public.coach_seq'),
                              phone VARCHAR(15),
                              email VARCHAR(100) UNIQUE,
                              name VARCHAR(100),
                              surname VARCHAR(100),
                              PRIMARY KEY (id)
);

CREATE TABLE public.hall (
                             id BIGINT NOT NULL DEFAULT nextval('public.hall_seq'),
                             postal_code VARCHAR(10) NOT NULL,
                             city VARCHAR(50) NOT NULL,
                             country VARCHAR(50) NOT NULL,
                             name VARCHAR(100) NOT NULL,
                             street VARCHAR(100) NOT NULL,
                             PRIMARY KEY (id)
);

CREATE TABLE public.team (
                             id BIGINT NOT NULL DEFAULT nextval('public.team_seq'),
                             category SMALLINT NOT NULL CHECK (category BETWEEN 0 AND 5),
                             gender SMALLINT NOT NULL CHECK (gender BETWEEN 0 AND 2),
                             team_number INT NOT NULL CHECK (team_number > 0),
                             CONSTRAINT unique_team UNIQUE (gender, category, team_number),
                             PRIMARY KEY (id)
);

CREATE TABLE public.team_coaches (
                                     coaches_id BIGINT NOT NULL,
                                     teams_id BIGINT NOT NULL,
                                     PRIMARY KEY (coaches_id, teams_id),
                                     CONSTRAINT fk_team FOREIGN KEY (teams_id) REFERENCES public.team(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_coach FOREIGN KEY (coaches_id) REFERENCES public.coach(id) ON DELETE CASCADE
);

CREATE TABLE public.training_session (
                                         id BIGINT NOT NULL DEFAULT nextval('public.training_session_seq'),
                                         day_of_week SMALLINT NOT NULL CHECK (day_of_week BETWEEN 0 AND 6),
                                         start_time TIME NOT NULL,
                                         end_time TIME NOT NULL,
                                         hall_id BIGINT REFERENCES public.hall(id) ON DELETE SET NULL,
                                         team_id BIGINT REFERENCES public.team(id) ON DELETE CASCADE,
                                         PRIMARY KEY (id)
);
