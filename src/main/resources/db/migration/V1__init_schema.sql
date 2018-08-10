CREATE TABLE countries(
  id serial UNIQUE,
  name VARCHAR(50)
);

CREATE TABLE cities(
  id serial UNIQUE,
  country_id INTEGER REFERENCES countries(id) ON DELETE CASCADE,
  name VARCHAR(50)
);

CREATE TABLE fields(
  id serial UNIQUE,
  name VARCHAR(50)
);

CREATE TABLE users(
  id serial UNIQUE,
  date_created TIMESTAMP DEFAULT NOW(),
  username VARCHAR(100),
  password VARCHAR(256),
  name VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  city_id INTEGER REFERENCES cities(id) NOT NULL,
  phone VARCHAR(20),
  email VARCHAR(250)
);

CREATE TABLE book_status_types(
  id serial UNIQUE,
  name VARCHAR(50)
);

CREATE TABLE book_holding_types(
  id serial UNIQUE,
  name VARCHAR(50)
);

CREATE TABLE books(
  id serial UNIQUE,
  date_created TIMESTAMP DEFAULT NOW(),
  owner_id INTEGER REFERENCES users(id) ON DELETE CASCADE NOT NULL,
  holding_type_id INTEGER REFERENCES book_holding_types(id) NOT NULL,
  status_id INTEGER REFERENCES book_status_types(id) NOT NULL,
  name VARCHAR(350),
  authors text,
  year_published INTEGER,
  field_id INTEGER REFERENCES fields(id),
  topics text,
  description text DEFAULT ''
);

CREATE TABLE books_wish_list(
  id serial UNIQUE,
  date_created TIMESTAMP DEFAULT NOW(),
  user_id INTEGER REFERENCES users(id) ON DELETE CASCADE NOT NULL,
  book_id INTEGER REFERENCES books(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE holding_request_status_types(
  id serial UNIQUE,
  name VARCHAR(50)
);

CREATE TABLE holding_requests(
  id serial UNIQUE,
  date_created TIMESTAMP DEFAULT NOW(),
  date_updated TIMESTAMP DEFAULT NOW(),
  sender_id INTEGER REFERENCES users(id) ON DELETE SET NULL,
  receiver_id INTEGER REFERENCES users(id) ON DELETE SET NULL,
  book_id INTEGER REFERENCES books(id) ON DELETE CASCADE NOT NULL,
  parent_book_id INTEGER,
  status_id INTEGER REFERENCES holding_request_status_types(id),
  request_message VARCHAR(500)
);

CREATE TABLE reviews(
  id serial UNIQUE,
  date_created TIMESTAMP DEFAULT NOW(),
  holding_request_id INTEGER REFERENCES holding_requests(id),
  reviewer_id INTEGER REFERENCES users(id),
  reviewee_id INTEGER REFERENCES users(id),
  review_text VARCHAR(350),
  score INTEGER
);

CREATE TABLE messages(
  id serial UNIQUE,
  date_created TIMESTAMP DEFAULT NOW(),
  holding_request_id INTEGER REFERENCES holding_requests(id),
  sender_id INTEGER REFERENCES users(id) NOT NULL,
  receiver_id INTEGER REFERENCES users(id) NOT NULL,
  message_text text
);


