## Hair_Salon:
A Web App that let's you add hair-stylists to a webpage and a database. It also let's you add clients to each stylist's page.

## By Peter

## Database Set-up:

In PSQL:
* CREATE DATABASE hair_salon;
* CREATE TABLE stylists (id serial PRIMARY KEY, name varchar);
* CREATE TABLE clients (id serial PRIMARY KEY, name varchar, stylistd int);
* CREATE DATABASE hair_salon_test WITH TEMPLATE hair_salon;

##Set-Up: 
Clone the repository to your desktop and run gradle, then open your browser and go to localhost:4567/

