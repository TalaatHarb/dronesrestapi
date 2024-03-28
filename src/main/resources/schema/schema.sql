CREATE TABLE IF NOT EXISTS Drone
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    serial_number    VARCHAR(100) UNIQUE NOT NULL,
    model            VARCHAR(50)  NOT NULL,
    weight_limit     DOUBLE       NOT NULL,
    battery_capacity DOUBLE       NOT NULL,
    current_load_weight DOUBLE   NOT NULL,
    state            VARCHAR(20)  NOT NULL
);

CREATE TABLE IF NOT EXISTS Medication
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    weight   DOUBLE NOT NULL,
    code     VARCHAR(20) NOT NULL,
    image    VARCHAR(255),
    drone_id BIGINT,
    state    VARCHAR(20)  NOT NULL DEFAULT 'LOADED',
    FOREIGN KEY (drone_id) REFERENCES Drone (id)
);
