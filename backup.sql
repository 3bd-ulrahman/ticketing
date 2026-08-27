--
-- PostgreSQL database dump
--

\restrict CBK12BXJ7cdwx3a3EZlR1jyka8UynkZZEyAL1sKhaJhPH9spiW2K65UYrauFejt

-- Dumped from database version 16.15 (Debian 16.15-1.pgdg13+2)
-- Dumped by pg_dump version 18.4 (Homebrew)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categories (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    description text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.categories OWNER TO postgres;

--
-- Name: categories_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.categories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.categories_id_seq OWNER TO postgres;

--
-- Name: categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.categories_id_seq OWNED BY public.categories.id;


--
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comments (
    id bigint NOT NULL,
    ticket_id bigint NOT NULL,
    user_id bigint NOT NULL,
    content text NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.comments OWNER TO postgres;

--
-- Name: comments_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.comments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.comments_id_seq OWNER TO postgres;

--
-- Name: comments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.comments_id_seq OWNED BY public.comments.id;


--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO postgres;

--
-- Name: ticket_status_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ticket_status_histories (
    id bigint NOT NULL,
    ticket_id bigint NOT NULL,
    old_status character varying(20),
    new_status character varying(20) NOT NULL,
    changed_by bigint NOT NULL,
    changed_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.ticket_status_histories OWNER TO postgres;

--
-- Name: ticket_status_histories_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ticket_status_histories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ticket_status_histories_id_seq OWNER TO postgres;

--
-- Name: ticket_status_histories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ticket_status_histories_id_seq OWNED BY public.ticket_status_histories.id;


--
-- Name: tickets; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tickets (
    id bigint NOT NULL,
    category_id bigint NOT NULL,
    created_by bigint NOT NULL,
    assigned_to bigint,
    title character varying(255) NOT NULL,
    description text,
    status character varying(20) DEFAULT 'OPEN'::character varying NOT NULL,
    priority character varying(20) DEFAULT 'MEDIUM'::character varying NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.tickets OWNER TO postgres;

--
-- Name: tickets_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tickets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tickets_id_seq OWNER TO postgres;

--
-- Name: tickets_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tickets_id_seq OWNED BY public.tickets.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    role character varying(20) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: categories id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories ALTER COLUMN id SET DEFAULT nextval('public.categories_id_seq'::regclass);


--
-- Name: comments id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments ALTER COLUMN id SET DEFAULT nextval('public.comments_id_seq'::regclass);


--
-- Name: ticket_status_histories id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket_status_histories ALTER COLUMN id SET DEFAULT nextval('public.ticket_status_histories_id_seq'::regclass);


--
-- Name: tickets id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets ALTER COLUMN id SET DEFAULT nextval('public.tickets_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.categories (id, name, description, created_at) FROM stdin;
1	Technical Support	Hardware and software issues	2026-08-26 15:33:58.356508
2	Billing	Payment and invoicing inquiries	2026-08-26 15:33:58.356508
3	Feature Request	New feature suggestions	2026-08-26 15:33:58.356508
4	Bug Report	Software bugs and defects	2026-08-26 15:33:58.356508
5	General Inquiry	General questions and support	2026-08-26 15:33:58.356508
7	Bug	Bug reports	2026-08-26 21:10:33.066272
8	test	\N	2026-08-27 15:20:14.516951
\.


--
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.comments (id, ticket_id, user_id, content, created_at) FROM stdin;
1	1	2	Investigating the server logs now.	2026-08-26 15:33:58.37063
2	1	3	The error started after the latest deploy.	2026-08-26 15:33:58.37063
3	2	2	Forwarded to the billing team.	2026-08-26 15:33:58.37063
4	4	2	Fixed in version 2.1.3. Please update the app.	2026-08-26 15:33:58.37063
5	1	2	Working on this now	2026-08-26 20:13:31.62495
6	1	3	Testing comment	2026-08-26 20:41:55.57747
7	1	3	snake_case test	2026-08-26 21:10:33.272042
8	6	3	lorem ipsum	2026-08-26 21:43:10.41801
9	6	3	lorem ipsum	2026-08-27 14:53:58.054227
10	6	3	lorem ipsum	2026-08-27 14:54:37.0838
\.


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	create users table	SQL	V1__create_users_table.sql	-1595174656	postgres	2026-08-26 15:33:58.259559	9	t
2	2	create categories table	SQL	V2__create_categories_table.sql	-141347583	postgres	2026-08-26 15:33:58.287546	8	t
3	3	create tickets table	SQL	V3__create_tickets_table.sql	1934410911	postgres	2026-08-26 15:33:58.304708	14	t
4	4	create comments table	SQL	V4__create_comments_table.sql	-2141142194	postgres	2026-08-26 15:33:58.327119	8	t
5	5	create ticket status histories table	SQL	V5__create_ticket_status_histories_table.sql	1258965652	postgres	2026-08-26 15:33:58.341819	6	t
6	6	seed categories	SQL	V6__seed_categories.sql	1627048630	postgres	2026-08-26 15:33:58.354069	2	t
7	7	seed users	SQL	V7__seed_users.sql	-435325918	postgres	2026-08-26 15:33:58.360949	2	t
8	8	seed tickets	SQL	V8__seed_tickets.sql	1567973683	postgres	2026-08-26 15:33:58.367774	4	t
\.


--
-- Data for Name: ticket_status_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ticket_status_histories (id, ticket_id, old_status, new_status, changed_by, changed_at) FROM stdin;
1	1	\N	OPEN	3	2026-08-26 15:33:58.37063
2	2	OPEN	IN_PROGRESS	2	2026-08-26 15:33:58.37063
3	4	OPEN	IN_PROGRESS	2	2026-08-26 15:33:58.37063
4	4	IN_PROGRESS	RESOLVED	2	2026-08-26 15:33:58.37063
5	5	OPEN	IN_PROGRESS	2	2026-08-26 15:33:58.37063
6	5	IN_PROGRESS	CLOSED	2	2026-08-26 15:33:58.37063
7	1	OPEN	IN_PROGRESS	2	2026-08-26 20:13:30.706133
8	3	OPEN	CLOSED	2	2026-08-26 20:36:30.301172
9	6	\N	OPEN	3	2026-08-26 20:41:55.472798
10	7	\N	OPEN	3	2026-08-26 21:10:33.166704
11	8	\N	OPEN	2	2026-08-26 21:16:58.71091
12	5	CLOSED	REOPENED	2	2026-08-27 14:36:56.247709
13	3	CLOSED	REOPENED	1	2026-08-27 14:37:10.688991
14	9	\N	OPEN	1	2026-08-27 14:55:38.872622
15	1	IN_PROGRESS	RESOLVED	1	2026-08-27 14:56:45.825136
16	1	RESOLVED	CLOSED	1	2026-08-27 14:56:58.260564
\.


--
-- Data for Name: tickets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tickets (id, category_id, created_by, assigned_to, title, description, status, priority, created_at, updated_at) FROM stdin;
2	2	3	2	Invoice not received	I was charged but did not receive my invoice for last month.	IN_PROGRESS	MEDIUM	2026-08-26 15:33:58.37063	2026-08-26 15:33:58.37063
4	4	3	2	App crashes on mobile	The mobile app crashes every time I try to upload a file.	RESOLVED	URGENT	2026-08-26 15:33:58.37063	2026-08-26 15:33:58.37063
7	1	3	\N	Test snake_case	Testing	OPEN	MEDIUM	2026-08-26 21:10:33.160391	2026-08-26 21:10:33.160411
8	1	2	\N	test	lorem	OPEN	MEDIUM	2026-08-26 21:16:58.681772	2026-08-26 21:16:58.681791
6	1	3	1	Test ticket	Testing	OPEN	HIGH	2026-08-26 20:41:55.445512	2026-08-26 21:39:24.74932
5	5	3	2	How to export data	I need to export my data to CSV. Where is this feature?	REOPENED	LOW	2026-08-26 15:33:58.37063	2026-08-27 14:36:56.22591
3	3	3	\N	Add dark mode	It would be great to have a dark mode option for the dashboard.	REOPENED	LOW	2026-08-26 15:33:58.37063	2026-08-27 14:37:10.676098
9	1	1	\N	test	lorem	OPEN	MEDIUM	2026-08-27 14:55:38.864049	2026-08-27 14:55:38.864062
1	1	3	2	Login page not loading	The login page returns a 500 error when accessed from Chrome.	CLOSED	URGENT	2026-08-26 15:33:58.37063	2026-08-27 14:56:58.247289
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, name, email, role) FROM stdin;
1	Abdulrahman Saad	ahmed@ticketing.com	ADMIN
2	Sara Mohamed	sara@ticketing.com	AGENT
3	Omar Ali	omar@ticketing.com	USER
\.


--
-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categories_id_seq', 9, true);


--
-- Name: comments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.comments_id_seq', 10, true);


--
-- Name: ticket_status_histories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ticket_status_histories_id_seq', 16, true);


--
-- Name: tickets_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tickets_id_seq', 9, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 3, true);


--
-- Name: categories categories_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_name_key UNIQUE (name);


--
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);


--
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: ticket_status_histories ticket_status_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket_status_histories
    ADD CONSTRAINT ticket_status_histories_pkey PRIMARY KEY (id);


--
-- Name: tickets tickets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_pkey PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: comments_ticket_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX comments_ticket_id_index ON public.comments USING btree (ticket_id);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: ticket_status_histories_ticket_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ticket_status_histories_ticket_id_index ON public.ticket_status_histories USING btree (ticket_id);


--
-- Name: tickets_assigned_to_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX tickets_assigned_to_index ON public.tickets USING btree (assigned_to);


--
-- Name: tickets_category_id_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX tickets_category_id_index ON public.tickets USING btree (category_id);


--
-- Name: tickets_created_by_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX tickets_created_by_index ON public.tickets USING btree (created_by);


--
-- Name: tickets_priority_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX tickets_priority_index ON public.tickets USING btree (priority);


--
-- Name: tickets_status_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX tickets_status_index ON public.tickets USING btree (status);


--
-- Name: comments comments_ticket_id_foreign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_ticket_id_foreign FOREIGN KEY (ticket_id) REFERENCES public.tickets(id) ON DELETE CASCADE;


--
-- Name: comments comments_user_id_foreign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_user_id_foreign FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: ticket_status_histories ticket_status_histories_changed_by_foreign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket_status_histories
    ADD CONSTRAINT ticket_status_histories_changed_by_foreign FOREIGN KEY (changed_by) REFERENCES public.users(id);


--
-- Name: ticket_status_histories ticket_status_histories_ticket_id_foreign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket_status_histories
    ADD CONSTRAINT ticket_status_histories_ticket_id_foreign FOREIGN KEY (ticket_id) REFERENCES public.tickets(id) ON DELETE CASCADE;


--
-- Name: tickets tickets_assigned_to_foreign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_assigned_to_foreign FOREIGN KEY (assigned_to) REFERENCES public.users(id);


--
-- Name: tickets tickets_category_id_foreign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_category_id_foreign FOREIGN KEY (category_id) REFERENCES public.categories(id);


--
-- Name: tickets tickets_created_by_foreign; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_created_by_foreign FOREIGN KEY (created_by) REFERENCES public.users(id);


--
-- PostgreSQL database dump complete
--

\unrestrict CBK12BXJ7cdwx3a3EZlR1jyka8UynkZZEyAL1sKhaJhPH9spiW2K65UYrauFejt

