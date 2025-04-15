CREATE TABLE IF NOT EXISTS flights (
    id SERIAL PRIMARY KEY,
    flight_number VARCHAR(10) UNIQUE NOT NULL,
    airline VARCHAR(100) NOT NULL,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    departure_date DATE NOT NULL,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

-- Manually inserting flights between major North American cities
INSERT INTO flights (flight_number, airline, source, destination, departure_time, arrival_time, departure_date, total_seats, available_seats, price) VALUES
    ('FL001', 'Air Canada', 'New York', 'Los Angeles', '2025-03-10 08:00:00', '2025-03-10 11:30:00', '2025-03-10', 180, 180, 350.00),
    ('FL002', 'American Airlines', 'Los Angeles', 'New York', '2025-03-10 12:00:00', '2025-03-10 15:30:00', '2025-03-10', 200, 200, 360.00),
    ('FL003', 'Delta', 'Chicago', 'Toronto', '2025-03-11 09:30:00', '2025-03-11 11:00:00', '2025-03-11', 150, 150, 220.00),
    ('FL004', 'United', 'Toronto', 'Chicago', '2025-03-11 13:00:00', '2025-03-11 14:30:00', '2025-03-11', 160, 160, 210.00),
    ('FL005', 'WestJet', 'Vancouver', 'Miami', '2025-03-12 07:45:00', '2025-03-12 15:00:00', '2025-03-12', 140, 140, 430.00),
    ('FL006', 'Aeromexico', 'Miami', 'Vancouver', '2025-03-12 16:00:00', '2025-03-12 23:30:00', '2025-03-12', 135, 135, 420.00),
    ('FL007', 'Air Canada', 'Dallas', 'Mexico City', '2025-03-13 10:00:00', '2025-03-13 13:00:00', '2025-03-13', 175, 175, 290.00),
    ('FL008', 'American Airlines', 'Mexico City', 'Dallas', '2025-03-13 14:30:00', '2025-03-13 17:30:00', '2025-03-13', 180, 180, 280.00),
    ('FL009', 'Delta', 'Montreal', 'San Francisco', '2025-03-14 06:00:00', '2025-03-14 12:00:00', '2025-03-14', 165, 165, 410.00),
    ('FL010', 'United', 'San Francisco', 'Montreal', '2025-03-14 13:30:00', '2025-03-14 19:30:00', '2025-03-14', 170, 170, 400.00),
    ('FL011', 'Air Canada', 'New York', 'Chicago', '2025-03-15 07:00:00', '2025-03-15 09:00:00', '2025-03-15', 180, 180, 250.00),
    ('FL012', 'American Airlines', 'Chicago', 'New York', '2025-03-15 10:00:00', '2025-03-15 12:00:00', '2025-03-15', 200, 200, 260.00),
    ('FL013', 'Delta', 'Los Angeles', 'Dallas', '2025-03-16 08:30:00', '2025-03-16 12:00:00', '2025-03-16', 160, 160, 280.00),
    ('FL014', 'United', 'Dallas', 'Los Angeles', '2025-03-16 13:30:00', '2025-03-16 17:00:00', '2025-03-16', 170, 170, 275.00),
    ('FL015', 'WestJet', 'Toronto', 'Vancouver', '2025-03-17 06:45:00', '2025-03-17 09:30:00', '2025-03-17', 150, 150, 320.00),
    ('FL016', 'Aeromexico', 'Vancouver', 'Toronto', '2025-03-17 10:00:00', '2025-03-17 12:45:00', '2025-03-17', 145, 145, 315.00),
    ('FL017', 'Air Canada', 'Miami', 'Montreal', '2025-03-18 07:00:00', '2025-03-18 10:30:00', '2025-03-18', 180, 180, 270.00),
    ('FL018', 'American Airlines', 'Montreal', 'Miami', '2025-03-18 12:00:00', '2025-03-18 15:30:00', '2025-03-18', 200, 200, 275.00);
