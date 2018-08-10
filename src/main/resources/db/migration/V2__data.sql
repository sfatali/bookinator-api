-- countries
INSERT INTO countries(id, name) VALUES(1, 'Finland');
INSERT INTO countries(id, name) VALUES(2, 'Sweden');
INSERT INTO countries(id, name) VALUES(3, 'Norway');
INSERT INTO countries(id, name) VALUES(4, 'Germany');
INSERT INTO countries(id, name) VALUES(5, 'UK');
INSERT INTO countries(id, name) VALUES(6, 'Spain');
INSERT INTO countries(id, name) VALUES(7, 'France');
INSERT INTO countries(id, name) VALUES(8, 'Italy');
INSERT INTO countries(id, name) VALUES(9, 'Poland');
INSERT INTO countries(id, name) VALUES(10, 'Azerbaijan');

-- cities
INSERT INTO cities(id, country_id, name) VALUES(1, 1, 'Oulu');
INSERT INTO cities(id, country_id, name) VALUES(2, 1, 'Helsinki');
INSERT INTO cities(id, country_id, name) VALUES(3, 1, 'Tampere');
INSERT INTO cities(id, country_id, name) VALUES(4, 1, 'Turku');
INSERT INTO cities(id, country_id, name) VALUES(5, 1, 'Rovaniemi');
INSERT INTO cities(id, country_id, name) VALUES(6, 2, 'Stockholm');
INSERT INTO cities(id, country_id, name) VALUES(7, 2, 'Gothenburg');
INSERT INTO cities(id, country_id, name) VALUES(8, 2, 'Malmo');
INSERT INTO cities(id, country_id, name) VALUES(9, 3, 'Oslo');
INSERT INTO cities(id, country_id, name) VALUES(10, 4, 'Berlin');
INSERT INTO cities(id, country_id, name) VALUES(11, 4, 'Munich');
INSERT INTO cities(id, country_id, name) VALUES(12, 4, 'Frankfurt');
INSERT INTO cities(id, country_id, name) VALUES(13, 5, 'London');
INSERT INTO cities(id, country_id, name) VALUES(14, 5, 'Manchester');
INSERT INTO cities(id, country_id, name) VALUES(15, 5, 'Oxford');
INSERT INTO cities(id, country_id, name) VALUES(16, 5, 'Edinburgh');
INSERT INTO cities(id, country_id, name) VALUES(17, 6, 'Madrid');
INSERT INTO cities(id, country_id, name) VALUES(18, 6, 'Barcelona');
INSERT INTO cities(id, country_id, name) VALUES(19, 6, 'Seville');
INSERT INTO cities(id, country_id, name) VALUES(20, 7, 'Paris');
INSERT INTO cities(id, country_id, name) VALUES(21, 7, 'Toulouse');
INSERT INTO cities(id, country_id, name) VALUES(22, 8, 'Rome');
INSERT INTO cities(id, country_id, name) VALUES(23, 8, 'Milan');
INSERT INTO cities(id, country_id, name) VALUES(24, 9, 'Warsaw');
INSERT INTO cities(id, country_id, name) VALUES(25, 9, 'Krakow');
INSERT INTO cities(id, country_id, name) VALUES(26, 9, 'Wroclaw');
INSERT INTO cities(id, country_id, name) VALUES(27, 10, 'Baku');
INSERT INTO cities(id, country_id, name) VALUES(28, 10, 'Sumqait');
INSERT INTO cities(id, country_id, name) VALUES(29, 10, 'Quba');
INSERT INTO cities(id, country_id, name) VALUES(30, 10, 'Shaki');
INSERT INTO cities(id, country_id, name) VALUES(31, 10, 'Hachmaz');
INSERT INTO cities(id, country_id, name) VALUES(32, 10, 'Gandja');

-- users
INSERT INTO users(id, username, password, name, surname, city_id, phone, email)
    VALUES (1, null, null, 'Chouaib', 'Hamek', 1, null, 'somemail@gmail.com'); -- unregistered user
INSERT INTO users(id, username, password, name, surname, city_id, phone, email)
  VALUES (2, 'alfitra', '$2a$10$lYT0mOImwvCwtUyQwef2RO.JFjlqkzg/YgI5qbrz0VP6virgwzCjy', -- 1 is default password
          'Alfitra', 'Rahman', 1, '+358460000001', 'somemail2@gmail.com');
INSERT INTO users(id, username, password, name, surname, city_id, phone, email)
  VALUES (3, 'daniel', '$2a$10$lYT0mOImwvCwtUyQwef2RO.JFjlqkzg/YgI5qbrz0VP6virgwzCjy',
          'Daniel', 'Toniuc', 1, '+358460000002', 'somemail3@gmail.com');
INSERT INTO users(id, username, password, name, surname, city_id, phone, email)
  VALUES (4, 'sabina', '$2a$10$lYT0mOImwvCwtUyQwef2RO.JFjlqkzg/YgI5qbrz0VP6virgwzCjy',
          'Sabina', 'Fataliyeva', 1, '+358460000003', 'somemail4@gmail.com');

INSERT INTO users(id, username, password, name, surname, city_id, phone, email)
VALUES (5, 'johndoe', '$2a$10$WLuIM6O9qJ6pE04Y3SU4iuPTsMn3N4sPfFlehEn8EKjFGrWM5Shtq',
        'John', 'Doe', 1, '+358460000004', 'somemail5@gmail.com');
select setval('users_id_seq', 5);

-- book status types
INSERT INTO book_status_types(id, name) VALUES(0, 'Unknown');
INSERT INTO book_status_types(id, name) VALUES(1, 'Available');
INSERT INTO book_status_types(id, name) VALUES(2, 'Taken to read');
INSERT INTO book_status_types(id, name) VALUES(3, 'Given away');
INSERT INTO book_status_types(id, name) VALUES(4, 'Temporarily unavailable');
INSERT INTO book_status_types(id, name) VALUES(5, 'In search');

-- book holding types
INSERT INTO book_holding_types(id, name) VALUES(0, 'Unknown');
INSERT INTO book_holding_types(id, name) VALUES(1, 'Give away');
INSERT INTO book_holding_types(id, name) VALUES(2, 'Read and return');

-- book fields
INSERT INTO fields(id, name) VALUES(0, 'Unknown');
INSERT INTO fields(id, name) VALUES(1, 'Fiction literature');
INSERT INTO fields(id, name) VALUES(2, 'Historical literature');
INSERT INTO fields(id, name) VALUES(3, 'Scientific literature');
INSERT INTO fields(id, name) VALUES(4, 'School books');
INSERT INTO fields(id, name) VALUES(5, 'Learning and development');

-- books
INSERT INTO books(id, owner_id, holding_type_id, status_id, name, authors, year_published, field_id, topics, description)
 VALUES(1, 4, 2, 2, 'The lord of the rings. Book 1', 'J.R.R. Tolkien;', null, 1, 'fantasy', null);
INSERT INTO books(id, owner_id, holding_type_id, status_id, name, authors, year_published, field_id, topics, description)
  VALUES(2, 4, 2, 1, 'The lord of the rings. Book 2', 'J.R.R. Tolkien;', null, 1, 'fantasy', null);
INSERT INTO books(id, owner_id, holding_type_id, status_id, name, authors, year_published, field_id, topics, description)
  VALUES(3, 4, 2, 4, 'The lord of the rings. Book 3', 'J.R.R. Tolkien;', null, 1, 'fantasy', null);
INSERT INTO books(id, owner_id, holding_type_id, status_id, name, authors, year_published, field_id, topics, description)
  VALUES(4, 2, 2, 5, 'Python for beginners', 'Any', null, 5, 'python;programming;urgent', 'I need a book to gain solid knowledge in Python');
INSERT INTO books(id, owner_id, holding_type_id, status_id, name, authors, year_published, field_id, topics, description)
  VALUES(5, 3, 2, 1, 'Learning Python', 'Mark Lutz', 2011, 5, 'Python', 'Best book to learn Python. I read it, it was awesome.');
INSERT INTO books(id, owner_id, holding_type_id, status_id, name, authors, year_published, field_id, topics, description)
  VALUES(6, 5, 1, 1, 'Pancakes cookbook', null, null, 5, 'Cooking;Pancakes', 'I used this book to learn how to make pancakes. Results are awesome.');
INSERT INTO books(id, owner_id, holding_type_id, status_id, name, authors, year_published, field_id, topics, description)
  VALUES(7, 1, 1, 5, 'Pancakes recipes', null, null, 5, 'Pancakes;Recipes', 'I need a book with different pncakes recipes.');
INSERT INTO books(id, owner_id, holding_type_id, status_id, name, authors, year_published, field_id, topics, description)
  VALUES(8, 2, 2, 1, 'A brief history of time', 'Stephen Hawking', 1988, 3, 'Popular science;Cosmology;Universe;Physics', 'Amazing book, guys!');
select setval('books_id_seq', 8);

-- books wish list
INSERT INTO books_wish_list(id, user_id, book_id) VALUES(1, 3, 1);
INSERT INTO books_wish_list(id, user_id, book_id) VALUES(2, 3, 2);
INSERT INTO books_wish_list(id, user_id, book_id) VALUES(3, 3, 8);
select setval('books_wish_list_id_seq', 3);

-- holding request status types
INSERT INTO holding_request_status_types(id, name) VALUES(1, 'Pending');
INSERT INTO holding_request_status_types(id, name) VALUES(2, 'Approved');
INSERT INTO holding_request_status_types(id, name) VALUES(3, 'Declined');
INSERT INTO holding_request_status_types(id, name) VALUES(4, 'Archived');

-- holding requests status types
INSERT INTO holding_requests(id, sender_id, receiver_id, book_id, parent_book_id, status_id, request_message)
    VALUES(1, 3, 1, 6, 7, 1, 'I think I have a book you were looking for.');
INSERT INTO holding_requests(id, sender_id, receiver_id, book_id, parent_book_id, status_id, request_message)
  VALUES(2, 3, 4, 1, null, 4 , null);
INSERT INTO holding_requests(id, sender_id, receiver_id, book_id, parent_book_id, status_id, request_message)
  VALUES(3, 3, 4, 2, null, 1 , 'Could you give me the 2nd book now?');
INSERT INTO holding_requests(id, sender_id, receiver_id, book_id, parent_book_id, status_id, request_message)
  VALUES(4, 3, 2, 5, 4, 2, 'Check out this book!');
INSERT INTO holding_requests(id, sender_id, receiver_id, book_id, parent_book_id, status_id, request_message)
VALUES(5, 3, 2, 8, null, 3, 'Give me the book!!!');
select setval('holding_requests_id_seq', 5);

-- reviews
INSERT INTO reviews(id, holding_request_id, reviewer_id, reviewee_id, review_text, score)
    VALUES(1, 2, 3, 4, 'Sabina replied quickly and communication was smooth.', 5);
INSERT INTO reviews(id, holding_request_id, reviewer_id, reviewee_id, review_text, score)
  VALUES(2, 2, 4, 3, 'Daniel returned the book in a perfect condition.', 5);
select setval('reviews_id_seq', 2);

-- messages
INSERT INTO messages(id, holding_request_id, sender_id, receiver_id, message_text)
    VALUES(1, 2, 4, 3, 'Okay. Would you like the book by post or to take in person?');
INSERT INTO messages(id, holding_request_id, sender_id, receiver_id, message_text)
  VALUES(2, 2, 3, 4, 'Would be easier for me to take in person. How about meeting this Friday around 6pm?');
INSERT INTO messages(id, holding_request_id, sender_id, receiver_id, message_text)
  VALUES(3, 2, 4, 3, 'Okay, works for me.');
INSERT INTO messages(id, holding_request_id, sender_id, receiver_id, message_text)
VALUES(4, 2, 3, 4, 'I finished the book and would like to return it soon. Could we meet?');
INSERT INTO messages(id, holding_request_id, sender_id, receiver_id, message_text)
  VALUES(5, 2, 4, 3, 'Sure, lets meet tomorrow at 5pm');
INSERT INTO messages(id, holding_request_id, sender_id, receiver_id, message_text)
  VALUES(6, 2, 3, 4, 'Thanks, see you.');
select setval('messages_id_seq', 6);
