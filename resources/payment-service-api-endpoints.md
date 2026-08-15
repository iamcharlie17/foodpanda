# Payment Service — API Endpoints
*(Optional service — per proposal, payment can otherwise be simulated inside the Order Service)*

Base path: `/api/payments`
Database: `paymentdb` (MongoDB) | Collection: `payments`

## Quick Reference

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/payments` | Initiate payment for an order |
| GET | `/api/payments/{id}` | Get payment details |
| GET | `/api/payments/order/{orderId}` | Get payment by order |
| POST | `/api/payments/{id}/refund` | Refund a payment |

---

## 1. Initiate Payment
`POST /api/payments`
Header: `Authorization: Bearer <customerAccessToken>`

**Request**
```json
{
  "orderId": "665f4a1e4a3b2c001f9d1111",
  "amount": 500.0,
  "method": "MOBILE_BANKING"
}
```

**Response `201 Created`**
```json
{
  "id": "665f8a1e4a3b2c001f9h1111",
  "orderId": "665f4a1e4a3b2c001f9d1111",
  "customerId": "665f1c2e4a3b2c001f9a1111",
  "amount": 500.0,
  "status": "PENDING",
  "method": "MOBILE_BANKING",
  "transactionRef": null,
  "createdAt": "2026-08-15T10:30:30Z"
}
```

**Response `201 Created` — Cash on Delivery**
```json
{
  "id": "665f8a1e4a3b2c001f9h1112",
  "orderId": "665f4a1e4a3b2c001f9d1112",
  "customerId": "665f1c2e4a3b2c001f9a1111",
  "amount": 750.0,
  "status": "PENDING",
  "method": "COD",
  "transactionRef": null,
  "createdAt": "2026-08-15T11:00:00Z"
}
```

---

## 2. Get Payment Details
`GET /api/payments/{id}`
Header: `Authorization: Bearer <accessToken>`

**Response `200 OK`**
```json
{
  "id": "665f8a1e4a3b2c001f9h1111",
  "orderId": "665f4a1e4a3b2c001f9d1111",
  "customerId": "665f1c2e4a3b2c001f9a1111",
  "amount": 500.0,
  "status": "SUCCESS",
  "method": "MOBILE_BANKING",
  "transactionRef": "bKash-TXN-9988776655",
  "createdAt": "2026-08-15T10:30:30Z",
  "updatedAt": "2026-08-15T10:31:00Z"
}
```

**Error `404 Not Found`**
```json
{ "error": "PAYMENT_NOT_FOUND", "message": "No payment found with given id" }
```

---

## 3. Get Payment by Order
`GET /api/payments/order/{orderId}`
Header: `Authorization: Bearer <accessToken>`

**Response `200 OK`**
```json
{
  "id": "665f8a1e4a3b2c001f9h1111",
  "orderId": "665f4a1e4a3b2c001f9d1111",
  "amount": 500.0,
  "status": "SUCCESS",
  "method": "MOBILE_BANKING",
  "transactionRef": "bKash-TXN-9988776655"
}
```

**Error `404 Not Found`**
```json
{ "error": "PAYMENT_NOT_FOUND", "message": "No payment found for given order" }
```

---

## 4. Refund a Payment
`POST /api/payments/{id}/refund`
Header: `Authorization: Bearer <adminOrSystemAccessToken>`

**Request**
```json
{
  "reason": "Order rejected by restaurant"
}
```

**Response `200 OK`**
```json
{
  "id": "665f8a1e4a3b2c001f9h1111",
  "orderId": "665f4a1e4a3b2c001f9d1111",
  "status": "REFUNDED",
  "amount": 500.0,
  "transactionRef": "bKash-TXN-9988776655",
  "refundRef": "bKash-RFD-1122334455",
  "updatedAt": "2026-08-15T10:35:00Z"
}
```

**Error `400 Bad Request` — already refunded / not eligible**
```json
{
  "error": "REFUND_NOT_ELIGIBLE",
  "message": "Payment is not in a refundable state"
}
```

---

## Payment Status Flow

```
PENDING → SUCCESS → REFUNDED
    ↓
  FAILED
```

## Inter-Service Calls (synchronous REST, current implementation)

| Trigger | Call made | Purpose |
|---|---|---|
| Order placed (online payment) | Order Service calls `POST /api/payments` | Initiate payment |
| Payment succeeds | Payment Service calls `PATCH /api/orders/{id}/status` (or a dedicated payment-status endpoint) on Order Service | Update `paymentStatus` to `PAID` |
| Payment fails | Payment Service calls back to Order Service | Order may be cancelled or held |
| Order rejected | Order Service calls `POST /api/payments/{id}/refund` | Auto-refund if already paid |

*Message broker (RabbitMQ/Kafka) can replace these direct calls later as an enhancement — not required for the current build.*
