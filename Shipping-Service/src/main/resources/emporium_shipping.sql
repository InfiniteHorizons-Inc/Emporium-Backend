USE emporium_shipping_service;
CREATE TABLE emporium_shipping (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100),
    shipping_date DATETIME,
    delivery_date DATETIME,
    status VARCHAR(50)
);