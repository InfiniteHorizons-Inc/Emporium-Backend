USE emporium_payment_service;
CREATE TABLE emporium_payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50),
    status VARCHAR(50),
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP
);