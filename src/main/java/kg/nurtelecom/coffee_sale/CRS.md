# Software Requirements Specification (SRS) for Coffee Shop Project

## Table of Contents
1. [Introduction](#1-introduction)
    - [Purpose](#11-purpose)
    - [Scope](#12-scope)
    - [Definitions, Acronyms, and Abbreviations](#13-definitions-acronyms-and-abbreviations)
    - [References](#14-references)
    - [Overview](#15-overview)
2. [System Overview](#2-system-overview)
3. [System Architecture](#3-system-architecture)
    - [High-level Design](#31-high-level-design)
    - [Technologies Used](#32-technologies-used)
4. [Functional Requirements](#4-functional-requirements)
    - [Coffee](#41-coffee)
    - [Coffeehouse](#42-coffeehouse)
    - [Coffee Inventory](#43-coffee-inventory)
    - [Merchandise Inventory](#44-merchandise-inventory)
    - [Supplier](#45-supplier)
    - [User](#46-user)
5. [Non-Functional Requirements](#5-non-functional-requirements)
    - [Performance](#51-performance)
    - [Reliability](#52-reliability)
    - [Availability](#53-availability)
    - [Security](#54-security)
6. [System Features](#6-system-features)
    - [CRUD Operations](#61-crud-operations)
    - [REST API](#62-rest-api)
7. [Database Design](#7-database-design)
    - [Database Models](#71-database-models)
    - [Entity Relationship Diagram (ERD)](#72-entity-relationship-diagram-erd)
8. [Use Cases](#8-use-cases)
    - [Use Case Diagram](#81-use-case-diagram)
    - [Use Case Descriptions](#82-use-case-descriptions)
9. [Constraints](#9-constraints)
    - [Database Constraints](#91-database-constraints)
    - [External System Constraints](#92-external-system-constraints)
10. [Appendices](#10-appendices)
- [Glossary](#101-glossary)
- [Acronyms](#102-acronyms)

---

## 1. Introduction

### 1.1 Purpose
This Software Requirements Specification (SRS) document outlines the requirements for the Coffee Shop project, which involves managing various coffee shop entities such as Coffee, Coffeehouse, Coffee Inventory, Merchandise Inventory, Suppliers, and Users. The system will be developed using Java, employing JPA for database management and RESTful services for the API layer.

### 1.2 Scope
The Coffee Shop system will provide CRUD (Create, Read, Update, Delete) functionality for all defined entities. It will integrate with two databases, H2 and PostgreSQL, and offer both a web interface and REST APIs for interaction. The project will be a web-based application with features for inventory management, supplier management, coffeehouse management, and user administration.

### 1.3 Definitions, Acronyms, and Abbreviations
- **JPA**: Java Persistence API
- **CRUD**: Create, Read, Update, Delete
- **REST**: Representational State Transfer
- **H2**: A relational database management system (in-memory)
- **PostgreSQL**: An open-source, object-relational database management system
- **MVC**: Model-View-Controller

### 1.4 References
- JPA Documentation
- PostgreSQL Official Documentation
- H2 Database Documentation

### 1.5 Overview
The document provides an overview of the requirements for each entity, including their functional and non-functional requirements, the system's architecture, and database design.

---

## 2. System Overview

The Coffee Shop application is a system designed to manage the operations of a coffee shop. It includes functionalities for:
- Managing different types of coffee and beverages
- Tracking inventory (both coffee and merchandise)
- Managing coffeehouse and user operations
- Maintaining records of suppliers and their products

The system will be built using Java with Spring Boot, integrating REST APIs for external communication and a database (H2 and PostgreSQL) for persistence.

---

## 3. System Architecture

### 3.1 High-level Design
The system follows the **MVC (Model-View-Controller)** design pattern. The **Model** handles the business logic and data interaction through the JPA entities, the **View** will be represented by a web interface, and the **Controller** will manage the communication between the model and the view via RESTful API endpoints.

### 3.2 Technologies Used
- **Backend**: Spring Boot, JPA (Hibernate), RESTful API
- **Frontend**: Thymeleaf, HTML5, CSS
- **Databases**: H2, PostgreSQL
- **Server**: Tomcat (Embedded with Spring Boot)
- **Authentication**: Spring Security
- **Version Control**: Git
- **Development Environment**: IntelliJ IDEA, Postman (for API testing)

---

## 4. Functional Requirements

### 4.1 Coffee
**Description**: Represents the different types of coffee available at the coffee shop.

**CRUD Operations**:
- **Create**: Add new coffee types with details like name, description, price, and stock.
- **Read**: Retrieve a list of all coffee types or a specific coffee by its ID.
- **Update**: Modify the details of a coffee type (e.g., price or description).
- **Delete**: Remove a coffee type from the system.

### 4.2 Coffeehouse
**Description**: Represents the coffee shop itself, including information like name, location, and operational hours.

**CRUD Operations**:
- **Create**: Add a new coffeehouse with details like name, address, and hours of operation.
- **Read**: Retrieve the coffeehouse details.
- **Update**: Modify the coffeehouse details (e.g., change hours or address).
- **Delete**: Remove the coffeehouse from the system.

### 4.3 Coffee Inventory
**Description**: Tracks the inventory of coffee beans and related materials.

**CRUD Operations**:
- **Create**: Add coffee beans or inventory materials with quantity and description.
- **Read**: Retrieve the current inventory status of coffee and related materials.
- **Update**: Update inventory levels based on sales or supply.
- **Delete**: Remove inventory items that are no longer in stock or obsolete.

### 4.4 Merchandise Inventory
**Description**: Manages other merchandise items like mugs, T-shirts, etc., sold at the coffeehouse.

**CRUD Operations**:
- **Create**: Add merchandise items with details like name, price, and quantity.
- **Read**: View a list of all merchandise items.
- **Update**: Modify merchandise details, such as price or quantity.
- **Delete**: Remove merchandise items from the inventory.

### 4.5 Supplier
**Description**: Represents the suppliers of coffee beans, merchandise, and other goods.

**CRUD Operations**:
- **Create**: Add new suppliers with details such as name, contact info, and products supplied.
- **Read**: Retrieve a list of all suppliers or details of a specific supplier.
- **Update**: Update supplier information, such as contact details or supplied products.
- **Delete**: Remove a supplier from the system.

### 4.6 User
**Description**: Represents the users of the system (both employees and customers).

**CRUD Operations**:
- **Create**: Register a new user with details like name, email, and role (admin, employee, or customer).
- **Read**: View user details and roles.
- **Update**: Modify user details or reset passwords.
- **Delete**: Remove a user from the system.

---

## 5. Non-Functional Requirements

### 5.1 Performance
- The system should handle up to 1000 concurrent users.
- API responses should be under 2 seconds for 95% of requests.

### 5.2 Reliability
- The system must be available 99.9% of the time.
- Data must be consistently stored and retrievable from both databases (H2 and PostgreSQL).

### 5.3 Availability
- The system should be available 24/7 with scheduled downtime for maintenance.

### 5.4 Security
- User data should be stored securely (passwords must be hashed).
- Role-based access control must be implemented to ensure that only authorized users can access certain functionalities.

---

## 6. System Features

### 6.1 CRUD Operations
All entities (Coffee, Coffeehouse, Inventory, Merchandise, Supplier, User) will implement standard CRUD operations via RESTful APIs and a web-based user interface.

### 6.2 REST API
- **GET**: Retrieve information (e.g., list of coffees, suppliers, users).
- **POST**: Create new records (e.g., add a new coffee or user).
- **PUT**: Update existing records (e.g., update inventory or user info).
- **DELETE**: Delete records (e.g., remove a coffee or supplier).

---

## 7. Database Design

### 7.1 Database Models
Each entity will correspond to a table in the database. The following relationships will exist:
- **Coffee**: One-to-many relationship with Coffee Inventory (a coffee type can have multiple inventory records).
- **Supplier**: One-to-many relationship with Coffee and Merchandise Inventory.
- **User**: One-to-many relationship with orders and other operations.

### 7.2 Entity Relationship Diagram (ERD)
(Include diagram here)

---

## 8. Use Cases

### 8.1 Use Case Diagram
(Include diagram of use cases like Create Coffee, Update Inventory, etc.)

### 8.2 Use Case Descriptions
- **Use Case 1**: Create Coffee
    - **Actors**: Admin
    - **Description**: Admin can create a new coffee type with relevant details (name, price, stock).

---

## 9. Constraints

### 9.1 Database Constraints
- Each entity will have a unique identifier (primary key).
- Foreign keys will be used for relationships between entities (e.g., coffee and inventory).

### 9.2 External System Constraints
- Integration with external systems (e.g., third-party suppliers or payment gateways) may be considered in future versions.

---

## 10. Appendices

### 10.1 Glossary
- **CRUD**: Create, Read, Update, Delete.
- **MVC**: Model-View-Controller design pattern.

### 10.2 Acronyms
- **API**: Application Programming Interface
- **JPA**: Java Persistence API
