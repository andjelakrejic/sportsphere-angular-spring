ALTER TABLE individual_training 
ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'PENDING';

UPDATE reservation SET date = CURDATE(), time_from = SUBTIME(CURTIME(), '00:03:00') WHERE id = 6;

-- reservation tabela
ALTER TABLE reservation 
  MODIFY COLUMN status ENUM('PENDING','BOOKED','CONFIRMED','NO_SHOW','CANCELLED') NOT NULL;

UPDATE reservation SET status = 'BOOKED' WHERE status = 'PENDING';

ALTER TABLE reservation 
  MODIFY COLUMN status ENUM('BOOKED','CONFIRMED','NO_SHOW','CANCELLED') NOT NULL;

INSERT INTO user (username, password, firstname, lastname, email, phone, profile_image, status, role, created_at)
VALUES (
  'admin',
  '$2a$10$AR9Cz8q3O4O/rkmvKjYUQeYvZhir87nBjZ7AcEDUyL6mH20zA.1MS',
  'Admin',
  'Admin',
  'admin@sportsphere.com',
  '+381600000000',
  'default-avatar.png',
  'APPROVED',
  'ADMIN',
  NOW()
);
ALTER TABLE user AUTO_INCREMENT = 14;
-- 1. Kreiraj novu tabelu
CREATE TABLE worker_facility (
    worker_id INT NOT NULL,
    facility_id INT NOT NULL,
    PRIMARY KEY (worker_id, facility_id),
    FOREIGN KEY (worker_id) REFERENCES worker(user_id),
    FOREIGN KEY (facility_id) REFERENCES facility(id)
);

-- 2. Prebaci postojece podatke (koristeci facility.worker_id koji jos postoji)
INSERT INTO worker_facility (worker_id, facility_id)
SELECT worker_id, id FROM facility WHERE worker_id IS NOT NULL;

-- 3. Tek sad obrisi stare kolone
ALTER TABLE facility DROP COLUMN worker_id;
ALTER TABLE worker DROP COLUMN facility_id;

ALTER TABLE trainer 
  ADD COLUMN status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE';

INSERT INTO admin (user_id)
VALUES (LAST_INSERT_ID());

ALTER TABLE individual_training 
ADD COLUMN training_date DATE NOT NULL DEFAULT (CURRENT_DATE),
ADD COLUMN time_from TIME NOT NULL DEFAULT '00:00:00',
ADD COLUMN time_to TIME NOT NULL DEFAULT '00:00:00';

ALTER TABLE individual_training 
  ADD COLUMN court_id INT NULL,
  ADD CONSTRAINT fk_training_court FOREIGN KEY (court_id) REFERENCES court(id);

CREATE TABLE athlete_facility_block (
    id INT AUTO_INCREMENT PRIMARY KEY,
    athlete_id INT NOT NULL,
    facility_id INT NOT NULL,
    no_show_count INT NOT NULL DEFAULT 0,
    blocked BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE KEY uq_athlete_facility (athlete_id, facility_id),
    CONSTRAINT fk_afb_athlete FOREIGN KEY (athlete_id) REFERENCES athlete(id),
    CONSTRAINT fk_afb_facility FOREIGN KEY (facility_id) REFERENCES facility(id)
);

ALTER TABLE worker
ADD COLUMN facility_id INT NULL,
ADD CONSTRAINT fk_worker_facility
    FOREIGN KEY (facility_id) REFERENCES facility(id);
    
ALTER TABLE court
ADD COLUMN sport_id INT NULL,
ADD CONSTRAINT fk_court_sport FOREIGN KEY (sport_id) REFERENCES sport(id);
    
SELECT id, scheduled_at, NOW() FROM individual_training;

SELECT id, scheduled_at, 
       CASE WHEN scheduled_at < NOW() THEN 'COMPLETED' ELSE 'SCHEDULED' END AS status
FROM individual_training;

-- Trainer needs a user account first (assuming user id 50 doesn't exist yet)
INSERT INTO user (id, username, firstname, lastname, email, password, profile_image, role)
VALUES (50, 'marko_pet', 'Marko', 'Petrović', 'marko.trainer@test.com', '$2a$10$dummyhashforsakeoftesting', 'trainer1.jpg', 'TRAINER');

-- Trainer profile
INSERT INTO trainer (user_id, specialization, price_per_hour, facility_id)
VALUES (50, 'Strength & Conditioning', 25.00, 1);

-- Trainer's sport(s)
INSERT INTO trainer_sport (trainer_id, sport_id)
VALUES (50, 1);

-- A second trainer for the same facility/sport, to test the list rendering
INSERT INTO user (id, username, firstname, lastname, email, password, profile_image, role)
VALUES (51, 'ana_jov', 'Ana', 'Jovanović', 'ana.trainer@test.com', '5, jelena_w, $2a$10$AR9Cz8q3O4O/rkmvKjYUQeYvZhir87nBjZ7AcEDUyL6mH20zA.1MS, Jelena, Stanić, jelena@email.com, +381641234567, , APPROVED, WORKER, 2026-06-17 12:34:53
', 'trainer2.jpg', 'TRAINER');

INSERT INTO trainer (user_id, specialization, price_per_hour, facility_id)
VALUES (51, 'Basketball Coaching', 30.00, 1);

INSERT INTO trainer_sport (trainer_id, sport_id)
VALUES (51, 1);

-- Individual trainings for athlete_id = 1 (one past = COMPLETED, one future = SCHEDULED)
INSERT INTO individual_training (athlete_id, trainer_id, facility_id, sport_id, scheduled_at)
VALUES (1, 50, 1, 1, '2026-06-20 10:00:00');

INSERT INTO individual_training (athlete_id, trainer_id, facility_id, sport_id, scheduled_at)
VALUES (1, 51, 1, 1, '2026-07-15 14:00:00');

CREATE TABLE trainer (
    user_id INT PRIMARY KEY,              -- FK -> user.id (trainer logs in as a user)
    specialization VARCHAR(255),
    price_per_hour DECIMAL(10,2),
    facility_id INT NOT NULL,             -- FK -> facility.id (facility they work at)
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (facility_id) REFERENCES facility(id)
);

CREATE TABLE trainer_sport (
    trainer_id INT NOT NULL,
    sport_id INT NOT NULL,
    PRIMARY KEY (trainer_id, sport_id),
    FOREIGN KEY (trainer_id) REFERENCES trainer(user_id),
    FOREIGN KEY (sport_id) REFERENCES sport(id)
);

CREATE TABLE individual_training (
    id INT AUTO_INCREMENT PRIMARY KEY,
    athlete_id INT NOT NULL,
    trainer_id INT NOT NULL,
    facility_id INT NOT NULL,
    sport_id INT NOT NULL,
    scheduled_at DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,          -- e.g. 'SCHEDULED', 'COMPLETED', 'CANCELLED'
    FOREIGN KEY (athlete_id) REFERENCES user(id),
    FOREIGN KEY (trainer_id) REFERENCES trainer(user_id),
    FOREIGN KEY (facility_id) REFERENCES facility(id),
    FOREIGN KEY (sport_id) REFERENCES sport(id)
);

INSERT INTO facility_reaction (facility_id, athlete_id, type, comment, created_at) VALUES
-- facility 1 (most comments here)
(1, 1, 'COMMENT', 'Really well maintained courts, will book again.', '2026-06-18 09:10:00'),
(1, 2, 'COMMENT', 'Good location, easy to find parking.', '2026-06-19 12:00:00'),
(1, 1, 'COMMENT', 'Came back for a second session, still great.', '2026-06-20 17:45:00'),
(1, 3, 'COMMENT', 'Staff were friendly and helpful.', '2026-06-21 08:30:00'),
(1, 2, 'COMMENT', 'Bit crowded on weekends but worth it.', '2026-06-22 19:15:00'),
(1, 1, 'COMMENT', 'Lighting in the evening could be improved.', '2026-06-23 20:00:00'),

-- facility 2
(2, 1, 'COMMENT', 'Nice smaller facility, less crowded.', '2026-06-19 10:00:00'),
(2, 2, 'COMMENT', 'Equipment was a bit outdated.', '2026-06-20 11:30:00'),

-- facility 3
(3, 1, 'COMMENT', 'Best courts I have used so far.', '2026-06-21 14:00:00'),
(3, 2, 'COMMENT', 'Clean facilities, would recommend.', '2026-06-22 16:20:00'),

-- facility 5
(5, 1, 'COMMENT', 'Solid experience overall, good value.', '2026-06-24 13:10:00');

-- Equipment
INSERT INTO equipment (name, sport_id, price, stock_quantity, image_url, description) VALUES
('Football Pro', 1, 2999.00, 15, 'uploads/football.jpg', 'Professional match football'),
('Shin Guards', 1, 899.00, 30, 'uploads/shinguards.jpg', 'Lightweight protective shin guards'),
('Tennis Racket', 2, 5499.00, 10, 'uploads/racket.jpg', 'Carbon fiber tennis racket'),
('Tennis Balls (3pk)', 2, 499.00, 50, 'uploads/tennisballs.jpg', 'Official pressure tennis balls'),
('Basketball', 3, 3299.00, 12, 'uploads/basketball.jpg', 'Indoor/outdoor basketball size 7'),
('Basketball Jersey', 3, 1899.00, 20, 'uploads/jersey.jpg', 'Breathable mesh jersey'),
('Volleyball', 4, 2499.00, 8, 'uploads/volleyball.jpg', 'Official size volleyball'),
('Knee Pads', 4, 1199.00, 25, 'uploads/kneepads.jpg', 'Protective volleyball knee pads'),
('Dumbbell Set 10kg', 5, 4999.00, 6, 'uploads/dumbbells.jpg', 'Rubber coated dumbbell pair'),
('Resistance Bands', 5, 799.00, 40, 'uploads/bands.jpg', 'Set of 5 resistance bands');



-- Orders za athlete_id = 1
INSERT INTO orders (athlete_id, total_price, status, created_at) VALUES
(1, 6398.00, 'ORDERED', '2025-06-20 10:30:00'),
(1, 5499.00, 'PICKED UP', '2025-06-10 14:00:00'),
(1, 3698.00, 'CANCELED', '2025-06-05 09:15:00');

-- Equipment orders (stavke)
-- Porudžbina 1: Football Pro + Shin Guards
INSERT INTO equipment_orders (order_id, equipment_id, quantity, price_at_purchase) VALUES
(1, 1, 1, 2999.00),
(1, 2, 2, 899.00);

-- Porudžbina 2: Tennis Racket
INSERT INTO equipment_orders (order_id, equipment_id, quantity, price_at_purchase) VALUES
(2, 3, 1, 5499.00);

-- Porudžbina 3: Basketball + Knee Pads
INSERT INTO equipment_orders (order_id, equipment_id, quantity, price_at_purchase) VALUES
(3, 5, 1, 3299.00),
(3, 8, 1, 1199.00);

CREATE TABLE equipment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    sport_id INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    image_url VARCHAR(255),
    description TEXT,
    FOREIGN KEY (sport_id) REFERENCES sport(id)
);

-- 2. Glavna tabela porudžbina
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    athlete_id INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ORDERED', -- 'ORDERED', 'PICKED_UP', 'CANCELLED'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (athlete_id) REFERENCES athlete(user_id)
);

-- 3. Stavke porudžbine (veza Više na Više između porudžbina i opreme)
CREATE TABLE equipment_orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    equipment_id INT NOT NULL,
    quantity INT NOT NULL,
    price_at_purchase DECIMAL(10, 2) NOT NULL, -- Čuvamo cenu u trenutku kupovine ako se u katalogu promeni
    FOREIGN KEY (order_id) REFERENCES equipment_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES equipment(id)
);

drop table equipment_orders;

CREATE TABLE teammate_ad (
    id INT AUTO_INCREMENT PRIMARY KEY,
    athlete_id INT NOT NULL,
    sport_id INT NOT NULL,
    city VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    time_slot VARCHAR(50) NOT NULL,
    total_players_needed INT NOT NULL,
    missing_players INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (athlete_id) REFERENCES athlete(user_id),
    FOREIGN KEY (sport_id) REFERENCES sport(id)
);

CREATE TABLE teammate_request (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ad_id INT NOT NULL,
    athlete_id INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ad_id) REFERENCES teammate_ad(id),
    FOREIGN KEY (athlete_id) REFERENCES athlete(user_id)
);

INSERT INTO teammate_ad (athlete_id, sport_id, city, date, time_slot, total_players_needed, missing_players, status)
VALUES 
(1, 1, 'Beograd', '2026-06-25', '18:00-19:00', 4, 2, 'ACTIVE'), -- Ima aktivne zahteve ispod
(1, 2, 'Novi Sad', '2026-06-26', '20:00-21:30', 2, 2, 'ACTIVE'), -- Nema još nijedan zahtev
(1, 3, 'Beograd', '2026-06-20', '16:00-17:00', 5, 0, 'INACTIVE'); -- Već zatvoren/kompletiran oglas

-- obriši duplikate, zadrži samo najstariji (najmanji id)
DELETE FROM teammate_request
WHERE id NOT IN (
    SELECT * FROM (
        SELECT MIN(id)
        FROM teammate_request
        GROUP BY ad_id, athlete_id
    ) AS temp
);

-- sad dodaj constraint
ALTER TABLE teammate_request 
ADD CONSTRAINT uq_ad_athlete UNIQUE (ad_id, athlete_id);

INSERT INTO teammate_ad (athlete_id, sport_id, city, date, time_slot, total_players_needed, missing_players, status)
VALUES 
(2, 1, 'Beograd', '2026-06-27', '19:00-20:00', 6, 3, 'ACTIVE'), -- Aktivni tuđi oglas
(3, 4, 'Niš',     '2026-06-28', '17:00-18:30', 3, 1, 'ACTIVE'), -- Aktivni tuđi oglas
(4, 5, 'Kragujevac', '2026-06-29', '21:00-22:00', 2, 2, 'ACTIVE'); -- Aktivni tuđi oglas

INSERT INTO teammate_request (ad_id, athlete_id, status)
VALUES 
(1, 2, 'PENDING'),  -- Korisnik 2 želi da igra sa tvojim korisnikom (vidi se Odobri/Odbij)
(1, 3, 'PENDING'),  -- Korisnik 3 želi da igra sa tvojim korisnikom (vidi se Odobri/Odbij)
(1, 4, 'APPROVED'); -- Korisnik 4 je već odobren (zato je gore na oglasu 1 missing_players spušten na 2)

-- Zahtevi koje je TVOJ korisnik (athlete_id = 1) poslao za tuđe oglase
INSERT INTO teammate_request (ad_id, athlete_id, status)
VALUES 
(4, 1, 'PENDING'),  -- Tvoj korisnik je poslao zahtev korisniku 2 za oglas broj 4
(5, 1, 'REJECTED');

TRUNCATE TABLE reservation;

INSERT INTO reservation (court_id, athlete_id, sport_id, date, time_from, time_to, status) VALUES
(1, 1, 3, '2026-05-23', '18:00:00', '19:00:00', 'CONFIRMED'),
(1, 1, 3, '2026-05-25', '10:00:00', '11:00:00', 'CONFIRMED');

INSERT INTO reservation (id, court_id, athlete_id, sport_id, date, time_from, time_to, status) VALUES
(1, 1, 1, 1, '2026-06-23', '18:00:00', '19:00:00', 'CONFIRMED'),
(2, 1, 1, 3, '2026-06-25', '10:00:00', '11:00:00', 'PENDING'),
(3, 2, 2, 2, '2026-06-22', '17:00:00', '18:00:00', 'CONFIRMED'),
(4, 2, 2, 2, '2026-06-29', '09:00:00', '10:00:00', 'PENDING'),
(5, 3, 1, 1, '2026-06-23', '18:00:00', '19:00:00', 'CONFIRMED'),
(6, 3, 1, 3, '2026-06-25', '10:00:00', '11:00:00', 'PENDING'),
(7, 4, 2, 2, '2026-06-22', '17:00:00', '18:00:00', 'CONFIRMED'),
(8, 4, 2, 2, '2026-06-29', '09:00:00', '10:00:00', 'PENDING'),
(9, 4, 3, 4, '2026-06-24', '20:00:00', '21:00:00', 'CONFIRMED');

truncate table court;

INSERT INTO court (facility_id, name, type, capacity, equipment_description) VALUES
(1, 'Main Indoor Court A', 'CLOSED', 200, 'basketball hoops, electronic scoreboard, wooden flooring'),
(1, 'Main Indoor Court B', 'OPEN', 180, 'basketball hoops, training equipment, seating benches'),
(1, 'Indoor Training Court', 'CLOSED', 150, 'adjustable hoops, cones, agility ladders'),
(2, 'Outdoor Court North', 'OPEN', 120, 'basic hoops, concrete surface, night lighting'),
(3, 'Stadium Court 1', 'OPEN', 300, 'professional hoops, bleachers, sound system'),
(4, 'Community Court', 'OPEN', 100, 'basic hoops, asphalt surface'),
(2, 'Outdoor Court South', 'CLOSED', 160, 'multi-sport flooring, portable hoops, scoreboard'),
(5, 'Recreation Court', 'OPEN', 140, 'standard hoops, fenced perimeter, lighting system'),
(2, 'Secondary Training Court', 'OPEN', 130, 'training cones, adjustable hoops, rubber surface');