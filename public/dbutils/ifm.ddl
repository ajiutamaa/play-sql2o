-- up revolution --
CREATE TABLE IF NOT EXISTS master_provinsi (
    id serial PRIMARY KEY NOT NULL,
    name varchar(30) NOT NULL,
    code varchar(5) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS master_kabupaten (
    id serial PRIMARY KEY NOT NULL,
    name varchar(30) NOT NULL,
    code varchar(8) UNIQUE NOT NULL,
    provinsi_id int NOT NULL REFERENCES master_provinsi ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS master_kecamatan (
    id serial PRIMARY KEY NOT NULL,
    name varchar(30) NOT NULL,
    code varchar(10) UNIQUE NOT NULL,
    kabupaten_id int NOT NULL REFERENCES master_kabupaten ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS master_desa (
    id serial PRIMARY KEY NOT NULL,
    name varchar(30) NOT NULL,
    code varchar(15) UNIQUE NOT NULL,
    kecamatan_id int NOT NULL REFERENCES master_kecamatan ON DELETE RESTRICT,
    coordinates point NOT NULL
);

CREATE TABLE IF NOT EXISTS master_employee (
    id serial PRIMARY KEY NOT NULL,
    name varchar(50) NOT NULL,
    sex char(1),
    birth_date DATE,
    address varchar(50),
    phone_number varchar(15),
    company varchar(50),
    email varchar(50) NOT NULL,
    is_active boolean NOT NULL,
    role varchar(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS master_season (
    id serial PRIMARY KEY NOT NULL,
    name varchar(20) NOT NULL,
    commodity varchar(30) NOT NULL,
    year int NOT NULL,
    order_in_year int NOT NULL,
    code varchar(30) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS master_unit (
    id serial PRIMARY KEY NOT NULL,
    name varchar(30) NOT NULL,
    head int NOT NULL REFERENCES master_employee ON DELETE RESTRICT,
    coordinates point NOT NULL
);

CREATE TABLE IF NOT EXISTS master_unit_active_season (
	unit_active_season_id serial PRIMARY KEY NOT NULL,
	unit_id int NOT NULL REFERENCES master_unit ON DELETE CASCADE,
	season_id int NOT NULL REFERENCES master_season ON DELETE CASCADE,
	UNIQUE(unit_id, season_id)
);

CREATE TABLE IF NOT EXISTS master_desa_unit_assignment (
	desa_id int NOT NULL REFERENCES master_desa ON DELETE CASCADE,
	unit_active_season_id int NOT NULL REFERENCES master_unit_active_season (unit_active_season_id) ON DELETE CASCADE,
	UNIQUE (desa_id)
);

CREATE TABLE IF NOT EXISTS master_unit_agent_assignment (
	agent_id int NOT NULL REFERENCES master_employee ON DELETE CASCADE,
	unit_active_season_id int NOT NULL REFERENCES master_unit_active_season (unit_active_season_id) ON DELETE CASCADE,
	UNIQUE (agent_id)
);

-- down evolution --
DROP TABLE IF EXISTS master_provinsi CASCADE;
DROP TABLE IF EXISTS master_kabupaten CASCADE;
DROP TABLE IF EXISTS master_kecamatan CASCADE;
DROP TABLE IF EXISTS master_desa CASCADE;
DROP TABLE IF EXISTS master_employee CASCADE;
DROP TABLE IF EXISTS master_season CASCADE;
DROP TABLE IF EXISTS master_unit CASCADE;
DROP TABLE IF EXISTS master_unit_active_season CASCADE;
DROP TABLE IF EXISTS master_desa_unit_assignment CASCADE;
DROP TABLE IF EXISTS master_unit_agent_assignment CASCADE;