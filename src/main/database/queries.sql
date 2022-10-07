
--Query examples
--List all Guests

SELECT *
FROM Naklaken.Guest;

SELECT id, surname, type
FROM Naklaken.Guest
WHERE guest.name= 'Kristel';


UPDATE naklaken.transactions SET state=false WHERE id=(SELECT id FROM naklaken.transactions WHERE user_id='B444B' ORDER BY id DESC LIMIT 1) RETURNING *;

SELECT * FROM naklaken.transactions WHERE datetime BETWEEN '2022-05-18 00:00:00.000' and '2022-05-18 23:59:59.999' ORDER BY datetime DESC;

SELECT * FROM naklaken.rfid WHERE code NOT IN (SELECT code_rfid FROM naklaken.match WHERE match.status=true);

SELECT DISTINCT rfid.* FROM naklaken.rfid WHERE rfid.status = 'valid' AND rfid.code NOT IN (SELECT match.code_rfid FROM match JOIN naklaken.booking b ON b.id = match.booking_id WHERE now() BETWEEN b.check_in AND b.check_out and match.status=true);