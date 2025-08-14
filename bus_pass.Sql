create database Buspass
use Buspass;
create table bus_pass(PassId int primary key auto_increment,Name varchar(50),issue_date date not null,expiry_date date not null,Active boolean);
insert into bus_pass (PassId,Name,issue_date,expiry_date,Active)
values(1,'Nancy','2024-03-19','2025-05-26',true),
(2,'rani','2025-08-13','2025-12-26',true),
(3,'Sham','2024-03-04','2025-11-26',false),
(5,'Amit','2024-02-28','2025-01-26',true)
insert into bus_pass values(4,'jackelin','2023-12-06','2025-01-12',false)
select*from bus_pass
