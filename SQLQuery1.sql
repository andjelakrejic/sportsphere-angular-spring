DELETE FROM facility_reaction WHERE facility_id = 1 AND athlete_id = 2 AND type = 'LIKE';
-- step 1: raw data, no joins
SELECT * FROM facility_reaction WHERE facility_id = 1 AND type = 'COMMENT';

-- step 2: with the joins the query actually uses
SELECT fr.id, fr.athlete_id, fr.comment, a.id AS athlete_pk, a.user_id, u.id AS user_pk
FROM facility_reaction fr
JOIN athlete a ON fr.athlete_id = a.id
JOIN user u ON a.user_id = u.id
WHERE fr.facility_id = 1 AND fr.type = 'COMMENT';

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