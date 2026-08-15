# Order Service — API Endpoints

Base path: `/api/orders`
Database: `orderdb` (MongoDB) | Collection: `orders`

## Quick Reference

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/orders` | Place order (from cart) |
| GET | `/api/orders/{id}` | Get order details |
| GET | `/api/orders` | List my orders |
| PATCH | `/api/orders/{id}/status` | Update order status (restaurant/rider) |
| GET | `/api/restaurants/{id}/orders` | List orders for a restaurant |

---

## 1. Place Order
`POST /api/orders`
Header: `Authorization: Bearer <customerAccessToken>`

**Request**
```json
{
  "restaurantId": "665f2a1e4a3b2c001f9b1111",
  "items": [
    {
      "menuItemId": "665f2b3e4a3b2c001f9b2222",
      "name": "Chicken Biryani",
      "price": 250.0,
      "quantity": 2
    }
  ],
  "deliveryAddress": {
    "street": "House 12, Road 4, Gazipur",
    "city": "Gazipur",
    "lat": 23.9999,
    "lng": 90.4203
  }
}
```

**Response `201 Created`**
```json
{
  "id": "665f4a1e4a3b2c001f9d1111",
  "customerId": "665f1c2e4a3b2c001f9a1111",
  "restaurantId": "665f2a1e4a3b2c001f9b1111",
  "riderId": null,
  "items": [
    {
      "menuItemId": "665f2b3e4a3b2c001f9b2222",
      "name": "Chicken Biryani",
      "price": 250.0,
      "quantity": 2
    }
  ],
  "deliveryAddress": {
    "street": "House 12, Road 4, Gazipur",
    "city": "Gazipur",
    "lat": 23.9999,
    "lng": 90.4203
  },
  "totalAmount": 500.0,
  "status": "PLACED",
  "statusHistory": [
    { "status": "PLACED", "timestamp": "2026-08-15T10:30:00Z" }
  ],
  "paymentStatus": "PENDING",
  "createdAt": "2026-08-15T10:30:00Z"
}
```

**Error `400 Bad Request` — validation failure**
```json
{
  "error": "MENU_ITEM_UNAVAILABLE",
  "message": "One or more items are no longer available"
}
```

---

## 2. Get Order Details
`GET /api/orders/{id}`
Header: `Authorization: Bearer <accessToken>`

**Response `200 OK`**
```json
{
  "id": "665f4a1e4a3b2c001f9d1111",
  "customerId": "665f1c2e4a3b2c001f9a1111",
  "restaurantId": "665f2a1e4a3b2c001f9b1111",
  "riderId": "665f1c2e4a3b2c001f9a3333",
  "items": [
    {
      "menuItemId": "665f2b3e4a3b2c001f9b2222",
      "name": "Chicken Biryani",
      "price": 250.0,
      "quantity": 2
    }
  ],
  "totalAmount": 500.0,
  "status": "PICKED_UP",
  "statusHistory": [
    { "status": "PLACED", "timestamp": "2026-08-15T10:30:00Z" },
    { "status": "ACCEPTED", "timestamp": "2026-08-15T10:32:00Z" },
    { "status": "PREPARING", "timestamp": "2026-08-15T10:33:00Z" },
    { "status": "READY", "timestamp": "2026-08-15T10:50:00Z" },
    { "status": "PICKED_UP", "timestamp": "2026-08-15T10:55:00Z" }
  ],
  "paymentStatus": "PAID",
  "createdAt": "2026-08-15T10:30:00Z",
  "updatedAt": "2026-08-15T10:55:00Z"
}
```

**Error `404 Not Found`**
```json
{ "error": "ORDER_NOT_FOUND", "message": "No order found with given id" }
```

---

## 3. List My Orders
`GET /api/orders?status=DELIVERED&page=0&size=10`
Header: `Authorization: Bearer <customerAccessToken>`

**Response `200 OK`**
```json
{
  "content": [
    {
      "id": "665f4a1e4a3b2c001f9d1111",
      "restaurantId": "665f2a1e4a3b2c001f9b1111",
      "totalAmount": 500.0,
      "status": "DELIVERED",
      "createdAt": "2026-08-15T10:30:00Z"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

---

## 4. Update Order Status
`PATCH /api/orders/{id}/status`
Header: `Authorization: Bearer <restaurantOrRiderAccessToken>`

**Request (restaurant accepting order)**
```json
{ "status": "ACCEPTED" }
```

**Response `200 OK`**
```json
{
  "id": "665f4a1e4a3b2c001f9d1111",
  "status": "ACCEPTED",
  "statusHistory": [
    { "status": "PLACED", "timestamp": "2026-08-15T10:30:00Z" },
    { "status": "ACCEPTED", "timestamp": "2026-08-15T10:32:00Z" }
  ],
  "updatedAt": "2026-08-15T10:32:00Z"
}
```

**Request (restaurant rejecting order)**
```json
{ "status": "REJECTED", "reason": "Item out of stock" }
```

**Response `200 OK`**
```json
{
  "id": "665f4a1e4a3b2c001f9d1111",
  "status": "REJECTED",
  "updatedAt": "2026-08-15T10:32:00Z"
}
```

**Error `400 Bad Request` — invalid transition**
```json
{
  "error": "INVALID_STATUS_TRANSITION",
  "message": "Cannot move from DELIVERED to PREPARING"
}
```

---

## 5. List Orders for a Restaurant
`GET /api/restaurants/{id}/orders?status=PLACED`
Header: `Authorization: Bearer <ownerAccessToken>`

**Response `200 OK`**
```json
[
  {
    "id": "665f4a1e4a3b2c001f9d1111",
    "customerId": "665f1c2e4a3b2c001f9a1111",
    "items": [
      { "name": "Chicken Biryani", "quantity": 2, "price": 250.0 }
    ],
    "totalAmount": 500.0,
    "status": "PLACED",
    "createdAt": "2026-08-15T10:30:00Z"
  }
]
```

---

## Order Status Flow

```
PLACED → ACCEPTED → PREPARING → READY → PICKED_UP → DELIVERED
              ↓
          REJECTED
```

## Inter-Service Calls (synchronous REST, current implementation)

| Trigger | Call made | Purpose |
|---|---|---|
| Order created (`PLACED`) | `POST` to Notification Service internal endpoint | Send order-placed notification |
| Order created | `GET` to User Service internal endpoint | Validate customer |
| Order created | `GET` to Restaurant Catalog Service | Validate restaurant, items, prices |
| Status → `ACCEPTED` / `REJECTED` | `POST` to Notification Service internal endpoint | Notify customer |
| Status → `READY` | `POST` to Delivery Service `/api/delivery/deliveries` | Assign rider |
| Status → `DELIVERED` | `POST` to Notification Service internal endpoint | Send delivery confirmation |

*Message broker (RabbitMQ/Kafka) can replace these direct calls later as an enhancement — not required for the current build.*
