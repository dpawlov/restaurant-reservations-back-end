 INSERT INTO restaurant (description, image, name, rating) VALUES ('description', 'image', 'name', 4.4)
 INSERT INTO table_info (description, person_capacity, table_number, restaurant_id) VALUES ('description', 5, 7, 1)
 INSERT INTO reservation (customer_name, is_completed, persons, time, restaurant_id) VALUES ('John', true, 5, '2012-09-17 18:47:52.069', 1)
 INSERT INTO rating (description, rating_type) VALUES ('nice restaurant', 5)
 INSERT INTO restaurant_rating (rating_id, restaurant_id) VALUES (1, 1)
 INSERT INTO working_time (day_of_week, description, end_time, start_time, restaurant_id) VALUES ('monday-friday', 'description', '21:00:00.00', '09:00:00.00', 1)
 INSERT INTO non_working_day (non_working_day_date, description, restaurant_id) VALUES ('2015-05-20', 'vacation day', 1)
 INSERT INTO table_info_reservations (table_info_id, reservation_id) VALUES (1, 1)


