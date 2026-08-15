# Notification Service — API Endpoints

Base path: `/api/notifications`
Database: `notificationdb` (MongoDB) | Collection: `notifications`

## Quick Reference

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/notifications` | List my notifications |
| PATCH | `/api/notifications/{id}/read` | Mark notification as read |
| PATCH | `/api/notifications/read-all` | Mark all as read |
| POST | `/internal/notifications` | Create notification (internal, event-driven) |

---

## 1. List My Notifications
`GET /api/notifications?isRead=false&page=0&size=10`
Header: `Authorization: Bearer <accessToken>`

**Response `200 OK`**
```json
{
  "content": [
    {
      "id": "665f7a1e4a3b2c001f9g1111",
      "type": "ORDER_ACCEPTED",
      "channel": "IN_APP",
      "message": "Your order from Star Kabab has been accepted and is being prepared.",
      "isRead": false,
      "createdAt": "2026-08-15T10:32:00Z"
    },
    {
      "id": "665f7a1e4a3b2c001f9g1112",
      "type": "ORDER_PLACED",
      "channel": "IN_APP",
      "message": "Your order has been placed successfully.",
      "isRead": false,
      "createdAt": "2026-08-15T10:30:00Z"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 2,
  "totalPages": 1
}
```

---

## 2. Mark Notification as Read
`PATCH /api/notifications/{id}/read`
Header: `Authorization: Bearer <accessToken>`

**Response `200 OK`**
```json
{
  "id": "665f7a1e4a3b2c001f9g1111",
  "isRead": true
}
```

**Error `404 Not Found`**
```json
{ "error": "NOTIFICATION_NOT_FOUND", "message": "No notification found with given id" }
```

---

## 3. Mark All as Read
`PATCH /api/notifications/read-all`
Header: `Authorization: Bearer <accessToken>`

**Response `200 OK`**
```json
{ "message": "All notifications marked as read", "updatedCount": 2 }
```

---

## 4. Create Notification (Internal)
`POST /internal/notifications`
*(called directly via REST by Order Service, Delivery Service, and Payment Service whenever a notifiable action happens — not exposed via the API Gateway)*

**Request**
```json
{
  "userId": "665f1c2e4a3b2c001f9a1111",
  "type": "RIDER_ASSIGNED",
  "channel": "IN_APP",
  "message": "A rider has been assigned to your order and is heading to the restaurant."
}
```

**Response `201 Created`**
```json
{
  "id": "665f7a1e4a3b2c001f9g1113",
  "userId": "665f1c2e4a3b2c001f9a1111",
  "type": "RIDER_ASSIGNED",
  "channel": "IN_APP",
  "message": "A rider has been assigned to your order and is heading to the restaurant.",
  "isRead": false,
  "createdAt": "2026-08-15T10:53:00Z"
}
```

---

## Callers (synchronous REST, current implementation)

| Called by | Notification `type` created | Message example |
|---|---|---|
| Order Service (order placed) | `ORDER_PLACED` | "Your order has been placed successfully." |
| Order Service (status → `ACCEPTED`) | `ORDER_ACCEPTED` | "Your order has been accepted and is being prepared." |
| Order Service (status → `REJECTED`) | `ORDER_REJECTED` | "Sorry, your order was rejected by the restaurant." |
| Delivery Service (rider assigned) | `RIDER_ASSIGNED` | "A rider has been assigned to your order." |
| Delivery Service (status → `DELIVERED`) | `ORDER_DELIVERED` | "Your order has been delivered. Enjoy your meal!" |

*Since notifications are secondary/non-critical (per the proposal), wrap these calls in try/catch on the caller side so a Notification Service failure never blocks order or delivery processing. Message broker (RabbitMQ/Kafka) can replace these direct calls later as an enhancement.*
