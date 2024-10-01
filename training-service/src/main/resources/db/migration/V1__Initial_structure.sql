-- Table: coach
CREATE TABLE public.coach (
                              id BIGSERIAL PRIMARY KEY,
                              email character varying(255),
                              name character varying(255),
                              phone character varying(255),
                              surname character varying(255)
);

-- Table: hall
CREATE TABLE public.hall (
                             id BIGSERIAL PRIMARY KEY,
                             city character varying(255),
                             country character varying(255),
                             name character varying(255),
                             postal_code character varying(255),
                             street character varying(255)
);

-- Table: team
CREATE TABLE public.team (
                             id BIGSERIAL PRIMARY KEY,
                             category smallint,
                             gender smallint,
                             team_number integer NOT NULL,
                             coach_id bigint,
                             CONSTRAINT team_category_check CHECK (((category >= 0) AND (category <= 5))),
                             CONSTRAINT team_gender_check CHECK (((gender >= 0) AND (gender <= 2))),
                             FOREIGN KEY (coach_id) REFERENCES public.coach(id)
);

-- Table: training_session
CREATE TABLE public.training_session (
                                         id BIGSERIAL PRIMARY KEY,
                                         day_of_week smallint,
                                         end_time time(6) without time zone,
                                         start_time time(6) without time zone,
                                         hall_id bigint,
                                         CONSTRAINT training_session_day_of_week_check CHECK (((day_of_week >= 0) AND (day_of_week <= 6))),
                                         FOREIGN KEY (hall_id) REFERENCES public.hall(id)
);

-- Table: team_training_session
CREATE TABLE public.team_training_session (
                                              team_id bigint NOT NULL,
                                              training_session_id bigint NOT NULL,
                                              PRIMARY KEY (team_id, training_session_id),
                                              UNIQUE (training_session_id),
                                              FOREIGN KEY (team_id) REFERENCES public.team(id),
                                              FOREIGN KEY (training_session_id) REFERENCES public.training_session(id)
);
