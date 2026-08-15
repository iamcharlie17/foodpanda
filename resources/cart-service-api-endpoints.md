# Cart Service — API Endpoints

Base path: `/api/cart`
Database: `cartdb` (MongoDB) | Collection: `carts`

## Quick Reference

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/cart` | Get current cart |
| POST | `/api/cart/items` | Add item to cart |
| PUT | `/api/cart/items/{menuItemId}` | Update item quantity |
| DELETE | `/api/cart/items/{menuItemId}` | Remove item from cart |
| DELETE | `/api/cart` | Clear cart |

---

## 1. Get Current Cart
`GET /api/cart`
Header: `Authorization: Bearer <accessToken>`

**Response `200 OK`**
```json
{
  "id": "665f3a1e4a3b2c001f9c1111",
  "customerId": "665f1c2e4a3b2c001f9a1111",
  "restaurantId": "665f2a1e4a3b2c001f9b1111",
  "items": [
    {
      "menuItemId": "665f2b3e4a3b2c001f9b2222",
      "name": "Chicken Biryani",
      "price": 250.0,
      "quantity": 2
    }
  ],
  "totalAmount": 500.0,
  "updatedAt": "2026-08-15T10:20:00Z"
}
```

**Response `200 OK` (empty cart)**
```json
{
  "customerId": "665f1c2e4a3b2c001f9a1111",
  "restaurantId": null,
  "items": [],
  "totalAmount": 0.0
}
```

---

## 2. Add Item to Cart
`POST /api/cart/items`
Header: `Authorization: Bearer <accessToken>`

**Request**
```json
{
  "restaurantId": "665f2a1e4a3b2c001f9b1111",
  "menuItemId": "665f2b3e4a3b2c001f9b2222",
  "name": "Chicken Biryani",
  "price": 250.0,
  "quantity": 2
}
```

**Response `201 Created`**
```json
{
  "id": "665f3a1e4a3b2c001f9c1111",
  "customerId": "665f1c2e4a3b2c001f9a1111",
  "restaurantId": "665f2a1e4a3b2c001f9b1111",
  "items": [
    {
      "menuItemId": "665f2b3e4a3b2c001f9b2222",
      "name": "Chicken Biryani",
      "price": 250.0,
      "quantity": 2
    }
  ],
  "totalAmount": 500.0,
  "updatedAt": "2026-08-15T10:20:00Z"
}
```

**Error `409 Conflict` — different restaurant already in cart**
```json
{
  "error": "DIFFERENT_RESTAURANT_IN_CART",
  "message": "Cart already contains items from another restaurant. Clear cart to continue."
}
```

---

## 3. Update Item Quantity
`PUT /api/cart/items/{menuItemId}`
Header: `Authorization: Bearer <accessToken>`

**Request**
```json
{ "quantity": 3 }
```

**Response `200 OK`**
```json
{
  "menuItemId": "665f2b3e4a3b2c001f9b2222",
  "name": "Chicken Biryani",
  "price": 250.0,
  "quantity": 3,
  "totalAmount": 750.0,
  "updatedAt": "2026-08-15T10:25:00Z"
}
```

---

## 4. Remove Item from Cart
`DELETE /api/cart/items/{menuItemId}`
Header: `Authorization: Bearer <accessToken>`

**Response `200 OK`**
```json
{
  "message": "Item removed from cart",
  "items": [],
  "totalAmount": 0.0
}
```

---

## 5. Clear Cart
`DELETE /api/cart`
Header: `Authorization: Bearer <accessToken>`

**Response `200 OK`**
```json
{ "message": "Cart cleared successfully" }
```
