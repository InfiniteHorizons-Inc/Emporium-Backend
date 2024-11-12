USE emporium_order_placement_service;
CREATE TABLE emporium_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME
);

CREATE TABLE emporium_order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    product_id BIGINT,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES emporium_orders(id)
);