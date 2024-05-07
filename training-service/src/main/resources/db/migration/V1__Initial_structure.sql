

--
-- Name: coach; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.coach (
                              id bigint NOT NULL,
                              email character varying(255),
                              name character varying(255),
                              phone character varying(255),
                              surname character varying(255)
);


--
-- Name: coach_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.coach_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: halle; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.halle (
                              id bigint NOT NULL,
                              city character varying(255),
                              country character varying(255),
                              name character varying(255),
                              postal_code character varying(255),
                              street character varying(255)
);


--
-- Name: halle_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.halle_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: team; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.team (
                             category smallint,
                             gender smallint,
                             team_number integer NOT NULL,
                             coach_id bigint,
                             id bigint NOT NULL,
                             CONSTRAINT team_category_check CHECK (((category >= 0) AND (category <= 5))),
                             CONSTRAINT team_gender_check CHECK (((gender >= 0) AND (gender <= 2)))
);


--
-- Name: team_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.team_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: team_training_session; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.team_training_session (
                                              team_id bigint NOT NULL,
                                              training_session_id bigint NOT NULL
);


--
-- Name: training_session; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.training_session (
                                         day_of_week smallint,
                                         end_time time(6) without time zone,
                                         start_time time(6) without time zone,
                                         halle_id bigint,
                                         id bigint NOT NULL,
                                         CONSTRAINT training_session_day_of_week_check CHECK (((day_of_week >= 0) AND (day_of_week <= 6)))
);


--
-- Name: training_session_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.training_session_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: coach coach_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.coach
    ADD CONSTRAINT coach_pkey PRIMARY KEY (id);



--
-- Name: halle halle_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.halle
    ADD CONSTRAINT halle_pkey PRIMARY KEY (id);


--
-- Name: team team_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT team_pkey PRIMARY KEY (id);


--
-- Name: team_training_session team_training_session_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.team_training_session
    ADD CONSTRAINT team_training_session_pkey PRIMARY KEY (team_id, training_session_id);


--
-- Name: team_training_session team_training_session_training_session_id_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.team_training_session
    ADD CONSTRAINT team_training_session_training_session_id_key UNIQUE (training_session_id);


--
-- Name: training_session training_session_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.training_session
    ADD CONSTRAINT training_session_pkey PRIMARY KEY (id);


--
-- Name: team fk3kq9y3up07so7lqkt4cpe6xb0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT fk3kq9y3up07so7lqkt4cpe6xb0 FOREIGN KEY (coach_id) REFERENCES public.coach(id);


--
-- Name: team_training_session fk65xjj4fiix9qk8nh6ykf0r45f; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.team_training_session
    ADD CONSTRAINT fk65xjj4fiix9qk8nh6ykf0r45f FOREIGN KEY (team_id) REFERENCES public.team(id);


--
-- Name: team_training_session fkg28gcog3ky0sy2pycuknsl5m7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.team_training_session
    ADD CONSTRAINT fkg28gcog3ky0sy2pycuknsl5m7 FOREIGN KEY (training_session_id) REFERENCES public.training_session(id);


--
-- Name: training_session fksy6cgsn6hdyw55kac2mmvkr75; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.training_session
    ADD CONSTRAINT fksy6cgsn6hdyw55kac2mmvkr75 FOREIGN KEY (halle_id) REFERENCES public.halle(id);


--
-- PostgreSQL database dump complete
--

