USE emporium_inventory_service;
CREATE TABLE emporium_inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT,
    quantity INT NOT NULL,
    updated_at DATETIME
);