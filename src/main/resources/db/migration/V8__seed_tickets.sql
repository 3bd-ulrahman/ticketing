INSERT INTO tickets (title, description, status, priority, category_id, created_by, assigned_to) VALUES
('Login page not loading', 'The login page returns a 500 error when accessed from Chrome.', 'OPEN', 'HIGH', 1, 3, NULL),
('Invoice not received', 'I was charged but did not receive my invoice for last month.', 'IN_PROGRESS', 'MEDIUM', 2, 3, 2),
('Add dark mode', 'It would be great to have a dark mode option for the dashboard.', 'OPEN', 'LOW', 3, 3, NULL),
('App crashes on mobile', 'The mobile app crashes every time I try to upload a file.', 'RESOLVED', 'URGENT', 4, 3, 2),
('How to export data', 'I need to export my data to CSV. Where is this feature?', 'CLOSED', 'LOW', 5, 3, 2);

INSERT INTO comments (ticket_id, user_id, content) VALUES
(1, 2, 'Investigating the server logs now.'),
(1, 3, 'The error started after the latest deploy.'),
(2, 2, 'Forwarded to the billing team.'),
(4, 2, 'Fixed in version 2.1.3. Please update the app.');

INSERT INTO ticket_status_histories (ticket_id, old_status, new_status, changed_by) VALUES
(1, NULL, 'OPEN', 3),
(2, 'OPEN', 'IN_PROGRESS', 2),
(4, 'OPEN', 'IN_PROGRESS', 2),
(4, 'IN_PROGRESS', 'RESOLVED', 2),
(5, 'OPEN', 'IN_PROGRESS', 2),
(5, 'IN_PROGRESS', 'CLOSED', 2);
