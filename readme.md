# 📚 __**BOOKSHOP**__ 📚
The application has been created to streamline the operations of an online bookstore. Its functionalities encompass:

* ***Inventory Management***:

Adding new books to the inventory.
Removing outdated or no longer available items.
Organizing books into categories for efficient cataloging. 
+ ***Shopping Cart Operations***:

Populating shopping carts with selected items.
Facilitating the completion of orders by processing the contents of the carts.
* ***Order Management (Admin Perspective)***:

Oversight and management of user orders.
Providing administrators with tools to monitor and handle the entire order fulfillment process.
## Technologies used
### Core Technologies:
* ***Java***
* ***Maven***
### Spring Framework:
* ***Spring Boot***
* ***Spring Data JPA***
* ***Spring Boot Security***
* ***Spring Boot Validation***
* ***Spring Boot Docker***
### Database and Persistence:
* ***MySQL***
* ***Hibernate***
* ***Liquibase***
### Testing:
* ***JUnit 5***
* ***Mockito***
* ***Docker***
* ***Test Containers***
### API Documentation:
* ***Swagger***
### Auxiliary Libraries:
* ***Lombok***
* ***MapStruct***
## Examples of endpoints
+ `POST: /api/auth/registration` - endpoint for user registration
+ `POST: /api/auth/login` - endpoint for user login
+ `GET: /api/books` - endpoint for getting all books (User)
+ `GET: /api/books/{id}` - endpoint for searching a specific book by id (User)
+ `POST: /api/books` - endpoint for creating a new book (Admin)
+ `PUT: /api/books/{id}` - endpoint for updating information about book (Admin)
+ `DELETE: /api/books/{id}` - endpoint for deleting books (Admin)
+ `GET: /api/books/search` - endpoint for searching books by parameters (User)
+ `GET: /api/categories` - endpoint getting getting  all categories (User)
+ `GET: /api/categories/{id}` - endpoint for searching a specific category (User)
+ `POST: /api/categories` - endpoint for creating a new category (Admin authority) (User)
+ `PUT: /api/categories/{id}` - endpoint for updating information about category (Admin)
+ `DELETE: /api/categories/{id}` - endpoint for deleting categories (Admin)
+ `GET: /api/categories/{id}/books` - endpoint for getting all books by category (User)
+ `GET: /api/orders` - endpoint for getting all orders (User)
+ `POST: /api/orders` - endpoint for placing orders (User)
+ `PATCH: /api/orders/{id}` - endpoint for updating orders status (Admin)
+ `GET: /api/orders/{orderId}/items` - endpoint for getting order items from order (User)
+ `GET: /api/orders/{orderId}/items/{itemId}` - endpoint for getting specific item from order (User)
+ `GET: /api/cart` - endpoint for getting all items into shopping cart (User)
+ `POST: /api/cart` - endpoint for adding items in shopping cart (User)
+ `PUT: /api/cart/cart-items/{itemId}` - endpoint for updating items quantity (User)
+ `DELETE: /api/cart/cart-items/{itemId}` - endpoint for deleting items from shopping cart (User)
## How to run the project from your side?
* Start by making a local copy of the project's repository
* Create the .env file with the corresponding variables
* Build images using ***docker-compose build*** and run the service in containers using ***docker-compose up***
* Start the application and have a good use)
## What was difficult about creating the project?
Initially, comprehending the entirety of the application's architecture posed a considerable challenge. However, through persistent attempts and dedicating a substantial amount of time to research and consulting with experienced professionals, I successfully identified a suitable structure.
Additionally, delving into Spring Security proved to be a demanding endeavor.
Regardless of the complexity, with perseverance, I eventually mastered its configuration for application integration. Despite encountering formidable challenges, I must acknowledge that crafting this project was an immensely gratifying experience. I eagerly look forward to further opportunities for practice and refinement.
## Possible improvements
1. **Deployment on AWS:**
- Examine the possibility of deploying the application using Amazon Web Services (AWS) servers. Utilizing AWS infrastructure has the potential to improve scalability, reliability, and performance, thereby strengthening the overall hosting solution.
2. **Create Telegram Bot for Sending Notifications:**
- Build a Telegram bot for efficient notification delivery. Define the bot's capabilities, such as handling system alerts, updates, and user-specific messages. Ensure secure communication, implement user interaction for subscription preferences, and conduct thorough testing for reliable performance.
3. **Implement Payment Using Stripe API:**
- Enable payment functionality by integrating the Stripe API. Implement secure and seamless transactions, leveraging Stripe's robust payment infrastructure. Ensure adherence to industry standards for payment security and provide a smooth user experience throughout the payment process.
4. **Comprehensive Test Coverage:**
- Enhance test coverage by creating additional unit, integration, and end-to-end tests. Comprehensive testing helps identify and address potential issues, ensuring a robust and reliable application.
