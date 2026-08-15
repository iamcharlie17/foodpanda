# Delivery & Dispatch Service — API Endpoints

Base path: `/api/delivery`
Database: `deliverydb` (MongoDB) | Collections: `riders`, `deliveries`

## Quick Reference

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/delivery/riders` | Register rider profile |
| PATCH | `/api/delivery/riders/me/availability` | Toggle rider availability |
| PATCH | `/api/delivery/riders/me/location` | Update rider live location |
| POST | `/api/delivery/deliveries` | Assign rider to order |
| PATCH | `/api/delivery/deliveries/{id}/status` | Update delivery status |
| GET | `/api/delivery/deliveries/{id}` | Get delivery/tracking details |

---

## 1. Register Rider Profile
`POST /api/delivery/riders`
Header: `Authorization: Bearer <riderAccessToken>`

**Request**
```json
{
  "userId": "665f1c2e4a3b2c001f9a3333",
  "vehicleType": "MOTORCYCLE"
}
```

**Response `201 Created`**
```json
{
  "id": "665f5a1e4a3b2c001f9e1111",
  "userId": "665f1c2e4a3b2c001f9a3333",
  "vehicleType": "MOTORCYCLE",
  "isAvailable": false,
  "currentLocation": null
}
```

---

## 2. Toggle Rider Availability
`PATCH /api/delivery/riders/me/availability`
Header: `Authorization: Bearer <riderAccessToken>`

**Request**
```json
{ "isAvailable": true }
```

**Response `200 OK`**
```json
{
  "riderId": "665f5a1e4a3b2c001f9e1111",
  "isAvailable": true
}
```

---

## 3. Update Rider Location
`PATCH /api/delivery/riders/me/location`
Header: `Authorization: Bearer <riderAccessToken>`

**Request**
```json
{
  "lat": 23.9600,
  "lng": 90.4180
}
```

**Response `200 OK`**
```json
{
  "riderId": "665f5a1e4a3b2c001f9e1111",
  "currentLocation": {
    "lat": 23.9600,
    "lng": 90.4180,
    "updatedAt": "2026-08-15T10:52:00Z"
  }
}
```

*Note: this is typically called frequently (e.g. every 5–10s) via HTTP or a WebSocket channel, cached in Redis, and periodically flushed to MongoDB.*

---

## 4. Assign Rider to Order
`POST /api/delivery/deliveries`
*(called synchronously by Order Service when order status changes to `READY` — can also be exposed for manual/admin assignment)*

**Request**
```json
{
  "orderId": "665f4a1e4a3b2c001f9d1111",
  "riderId": "665f5a1e4a3b2c001f9e1111"
}
```

**Response `201 Created`**
```json
{
  "id": "665f6a1e4a3b2c001f9f1111",
  "orderId": "665f4a1e4a3b2c001f9d1111",
  "riderId": "665f5a1e4a3b2c001f9e1111",
  "status": "ASSIGNED",
  "route": [],
  "assignedAt": "2026-08-15T10:50:00Z"
}
```

**Error `409 Conflict` — no rider available**
```json
{
  "error": "NO_RIDER_AVAILABLE",
  "message": "No available rider found near restaurant location"
}
```

---

## 5. Update Delivery Status
`PATCH /api/delivery/deliveries/{id}/status`
Header: `Authorization: Bearer <riderAccessToken>`

**Request**
```json
{
  "status": "PICKED_UP",
  "location": { "lat": 23.9587, "lng": 90.4203 }
}
```

**Response `200 OK`**
```json
{
  "id": "665f6a1e4a3b2c001f9f1111",
  "orderId": "665f4a1e4a3b2c001f9d1111",
  "status": "PICKED_UP",
  "route": [
    { "lat": 23.9587, "lng": 90.4203, "timestamp": "2026-08-15T10:55:00Z" }
  ]
}
```

**Request (final status)**
```json
{
  "status": "DELIVERED",
  "location": { "lat": 23.9999, "lng": 90.4203 }
}
```

**Response `200 OK`**
```json
{
  "id": "665f6a1e4a3b2c001f9f1111",
  "orderId": "665f4a1e4a3b2c001f9d1111",
  "status": "DELIVERED",
  "deliveredAt": "2026-08-15T11:10:00Z"
}
```

---

## 6. Get Delivery / Tracking Details
`GET /api/delivery/deliveries/{id}`
Header: `Authorization: Bearer <accessToken>`

**Response `200 OK`**
```json
{
  "id": "665f6a1e4a3b2c001f9f1111",
  "orderId": "665f4a1e4a3b2c001f9d1111",
  "riderId": "665f5a1e4a3b2c001f9e1111",
  "status": "ON_THE_WAY",
  "route": [
    { "lat": 23.9587, "lng": 90.4203, "timestamp": "2026-08-15T10:55:00Z" },
    { "lat": 23.9700, "lng": 90.4190, "timestamp": "2026-08-15T11:00:00Z" },
    { "lat": 23.9850, "lng": 90.4195, "timestamp": "2026-08-15T11:05:00Z" }
  ],
  "assignedAt": "2026-08-15T10:50:00Z"
}
```

**Error `404 Not Found`**
```json
{ "error": "DELIVERY_NOT_FOUND", "message": "No delivery found for given id" }
```

---

## Delivery Status Flow

```
ASSIGNED → PICKED_UP → ON_THE_WAY → DELIVERED
```

## Inter-Service Calls (synchronous REST, current implementation)

| Trigger | Call made | Purpose |
|---|---|---|
| Order status → `READY` (from Order Service) | Order Service calls `POST /api/delivery/deliveries` | Assign an available rider |
| Rider assigned | `POST` to Notification Service internal endpoint | Alert customer a rider is on the way |
| Delivery status → `DELIVERED` | `PATCH` to Order Service `/api/orders/{id}/status` | Mark order `DELIVERED` |
| Delivery status → `DELIVERED` | `POST` to Notification Service internal endpoint | Send delivery confirmation |

*Message broker (RabbitMQ/Kafka) can replace these direct calls later as an enhancement — not required for the current build.*
