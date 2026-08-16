# Foodpanda Clone - Microservices Architecture

This project is a microservices-based backend for a food delivery application (Foodpanda clone), built with Spring Boot, MongoDB, and RabbitMQ.

## 🚀 Prerequisites
Before running the project, ensure you have the following installed and running:
- **Java 17+**
- **Maven**
- **MongoDB**: Running on `localhost:27017`
- **RabbitMQ**: Running on `localhost:5672` (Management UI on `15672`). 
  - *Docker command:* `docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:3-management`

---

## 🏃‍♂️ Step-by-Step Run Guide

The order in which you start the services is important.

### 1. Start the Infrastructure Services
First, start the Service Registry so other services can find each other.
```bash
cd service-registry
mvn spring-boot:run
```
*(Runs on `http://localhost:8761`)*

Next, start the API Gateway (this handles routing and global CORS).
```bash
cd api-gateway
mvn spring-boot:run
```
*(Runs on `http://localhost:8080`)*

### 2. Start the Business Microservices
Open a new terminal for each of the following and run `mvn spring-boot:run`:
- `user-service` (Port: 8081)
- `restaurant-catalog-service` (Port: 8082)
- `cart-service` (Port: 8083)
- `order-service` (Port: 8084)
- `delivery-dispatch-service` (Port: 8085)
- `notification-service` (Port: 8086)
- `payment-service` (Port: 8087)

> **Note:** All API testing below uses the API Gateway (`localhost:8080`). You can also hit the microservices directly on their specific ports.

---

## 🧪 API Testing Guide & Demo Data

> ⚠️ **IMPORTANT**: For all endpoints except Register and Login, you must include the JWT token in the Headers:
> `Authorization: Bearer <your_access_token>`

### 1. User Service (`/api/users`)

**Register a Customer**
- **POST** `http://localhost:8080/api/users/auth/register`
```json
{
  "name": "Riyad H. Hosen",
  "email": "riyad@example.com",
  "password": "Secret@123",
  "phone": "+8801700000000",
  "role": "CUSTOMER"
}
```

**Login**
- **POST** `http://localhost:8080/api/users/auth/login`
```json
{
  "email": "riyad@example.com",
  "password": "Secret@123"
}
```
*(Copy the `accessToken` from the response for the following requests).*

**Add Address**
- **POST** `http://localhost:8080/api/users/me/addresses`
```json
{
  "label": "Home",
  "street": "123 Main St",
  "city": "Dhaka",
  "lat": 23.8103,
  "lng": 90.4125,
  "isDefault": true
}
```

---

### 2. Restaurant Catalog Service (`/api/restaurants`)

*(Requires `RESTAURANT_OWNER` role, so register a new user with that role first!)*

**Create a Restaurant**
- **POST** `http://localhost:8080/api/restaurants`
```json
{
  "name": "Pizza Hut",
  "description": "Best pizzas in town",
  "cuisine": ["Italian", "Fast Food"],
  "address": {
    "street": "Gulshan Avenue",
    "city": "Dhaka",
    "lat": 23.7925,
    "lng": 90.4078
  },
  "operatingHours": {
    "open": "10:00",
    "close": "23:00"
  }
}
```
*(Copy the generated `_id` from the response, e.g., `60d5ec...`)*

**Add a Menu Item**
- **POST** `http://localhost:8080/api/restaurants/<RESTAURANT_ID>/menu-items`
```json
{
  "name": "Pepperoni Pizza",
  "description": "Large pepperoni with extra cheese",
  "price": 850.00,
  "isAvailable": true
}
```
*(Copy the generated menu item `id` for the next steps).*

---

### 3. Cart Service (`/api/cart`)

**Add Item to Cart**
- **POST** `http://localhost:8080/api/cart/items`
```json
{
  "menuItemId": "<MENU_ITEM_ID>",
  "quantity": 2
}
```

**View Cart**
- **GET** `http://localhost:8080/api/cart`

---

### 4. Order Service (`/api/orders`)

**Place an Order**
- **POST** `http://localhost:8080/api/orders`
```json
{
  "restaurantId": "<RESTAURANT_ID>",
  "items": [
    {
      "menuItemId": "<MENU_ITEM_ID>",
      "quantity": 2,
      "price": 850.00
    }
  ],
  "deliveryAddress": {
    "street": "123 Main St",
    "city": "Dhaka",
    "lat": 23.8103,
    "lng": 90.4125
  }
}
```
*(Copy the generated `orderId` from the response. Notice that the Notification Service instantly logs an "Order Placed" message via RabbitMQ!)*

---

### 5. Payment Service (`/api/payments`)

**Initiate Payment**
- **POST** `http://localhost:8080/api/payments`
```json
{
  "orderId": "<ORDER_ID>",
  "amount": 1700.00,
  "method": "CREDIT_CARD"
}
```
*(Notice in the logs that `payment-service` publishes a `PaymentEvent`. The `order-service` listens to this and updates the order status to `ACCEPTED`!)*

---

### 6. Delivery Dispatch Service (`/api/delivery`)

*(Requires `RIDER` role, so register a rider user first)*

**Register Rider Vehicle**
- **POST** `http://localhost:8080/api/delivery/riders`
```json
{
  "vehicleType": "MOTORCYCLE"
}
```

**Assign Delivery**
- **POST** `http://localhost:8080/api/delivery/deliveries`
```json
{
  "orderId": "<ORDER_ID>"
}
```
*(Notice in the logs that a `DeliveryEvent` is published. The `notification-service` alerts the customer, and the `order-service` updates the status!)*

**Update Delivery Status**
- **PATCH** `http://localhost:8080/api/delivery/deliveries/<DELIVERY_ID>/status`
```json
{
  "status": "DELIVERED",
  "location": {
    "lat": 23.8103,
    "lng": 90.4125
  }
}
```
