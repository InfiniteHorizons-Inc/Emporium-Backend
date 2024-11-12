USE emporium_reporting_service;
CREATE TABLE emporium_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_data JSON,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);