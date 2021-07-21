insert into item(name, description, market,stock, price_tag, state) values ('item 1', '', 'PT',20,30.0,'AVAILABLE');
insert into item(name, description, market,stock, price_tag, state) values ('item 2', '', 'PT',10,10.0,'AVAILABLE');
insert into item(name, description, market,stock, price_tag, state) values ('item 3', '', 'PT',100,5.0,'AVAILABLE');
insert into item(name, description, market,stock, price_tag, state) values ('item 4', '', 'PT',2,100.0,'AVAILABLE');
insert into item(name, description, market,stock, price_tag, state) values ('item 5', '', 'PT',20,2.5,'AVAILABLE');

insert into users(name, username, password) values ('user 1', 'username1', '$2a$10$BbhRgyvAJN7jv8s4u4Pb4e54JIpW2YugwjTbA71.u47CO60gtET7K');
insert into users(name, username, password) values ('user 2', 'username2', '$2a$10$C.63k7SK3wyZk45OgXMyfuhy359gNJZoE86Xu/0zZWIWW5tdU1.7e');
insert into users(name, username, password) values ('user 3', 'username3', '$2a$10$WkZ6dd.Y4pBey.xWOdmjSu44Mh4bUWMCmr7aPxyFaqwAy2FnOkw7C');
insert into users(name, username, password) values ('user 4', 'username4', '$2a$10$g58pUPnOI.U9RJOniZDPOuTM3FxHnJ58lTN7/11./5NK2xkBdyZSS');

insert into roles(id, role_name) values(1, 'ADMIN');
insert into roles(id, role_name) values(2, 'USER');

insert into users_roles(users_id_user, roles_id) values(1, 1);
insert into users_roles(users_id_user, roles_id) values(2, 2);