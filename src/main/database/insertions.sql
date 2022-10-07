
/*Insert into guest table*/

INSERT INTO Naklaken.Guest (id, name, surname, type)
VALUES ('AX0001', 'Kristel', 'Cocoli', 'disabled'),
       ('AX0002', 'Nicolo', 'Scialpi', 'normal'),
       ('AX0003', 'Asmaa', 'Mirkhan', 'normal'),
       ('AX0004', 'Nur', 'Bal', 'normal'),
       ('AX0005', 'Kader', 'Cicek', 'normal'),
       ('AX0006', 'Lennin', 'Cardozo', 'normal'),
       ('AX0007', 'Enrico', 'Sgarbossa', 'normal'),
       ('AX0008', 'Alessandro', 'Cusinato', 'normal');

/*Insert into user table*/

INSERT INTO Naklaken.User (id, email, password, active,permission_level)
VALUES('BX001','kristel@gmail.com','Kristel123',true,'receptionist');

/*Insert into RFID table*/

INSERT INTO naklaken.rfid(code, type, status)
VALUES('b123b','band','valid');

/*Insert into booking table*/

INSERT INTO Naklaken.booking (check_in, check_out, room, balance, "guest_id", "user_id")
VALUES ('2022-03-14 04:05:06','2022-03-15 04:05:06','B13',1000,'AX0002','BX001'),
('2022-04-04','2022-05-18','B13',2000,'AX0003','BX001');

/*Booking Id should be the id generated in your table */
/*Insert into transaction table*/
/*TODO: decide on datetime format*/

INSERT INTO naklaken.transactions(state, amount, description, datetime, type, "user_id", "booking_id")
VALUES(true,1000,'A drink','2022-04-04 04:05:06','payment','BX001',2);

/*Insert into match table*/

INSERT INTO naklaken.match(status, datetime, "booking_id", "code_rfid")
VALUES(true,'2022-04-04 04:05:06',6,'b123b');
