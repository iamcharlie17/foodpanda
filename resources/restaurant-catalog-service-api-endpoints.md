# Restaurant Catalog Service — API Endpoints

Base path: `/api/restaurants`
Database: `catalogdb` (MongoDB) | Collections: `restaurants`, `menu_items`

## Quick Reference

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/restaurants` | Create restaurant (owner) |
| GET | `/api/restaurants` | List/browse restaurants |
| GET | `/api/restaurants/{id}` | Get restaurant details |
| PUT | `/api/restaurants/{id}` | Update restaurant |
| POST | `/api/restaurants/{id}/menu-items` | Add menu item |
| GET | `/api/restaurants/{id}/menu-items` | List menu items |
| PUT | `/api/restaurants/{id}/menu-items/{itemId}` | Update menu item |
| DELETE | `/api/restaurants/{id}/menu-items/{itemId}` | Delete menu item |

---

## 1. Create Restaurant
`POST /api/restaurants`
Header: `Authorization: Bearer <ownerAccessToken>`

**Request**
```json
{
  "name": "Star Kabab",
  "description": "Authentic Bangladeshi cuisine",
  "cuisine": ["Bangladeshi", "Biryani"],
  "address": {
    "street": "Board Bazar, Gazipur",
    "city": "Gazipur",
    "lat": 23.9587,
    "lng": 90.4203
  },
  "operatingHours": {
    "open": "10:00",
    "close": "23:00"
  }
}
```

**Response `201 Created`**
```json
{
  "id": "665f2a1e4a3b2c001f9b1111",
  "ownerId": "665f1c2e4a3b2c001f9a2222",
  "name": "Star Kabab",
  "description": "Authentic Bangladeshi cuisine",
  "cuisine": ["Bangladeshi", "Biryani"],
  "address": {
    "street": "Board Bazar, Gazipur",
    "city": "Gazipur",
    "lat": 23.9587,
    "lng": 90.4203
  },
  "operatingHours": { "open": "10:00", "close": "23:00" },
  "rating": 0.0,
  "isOpen": true,
  "isApproved": false,
  "createdAt": "2026-08-15T10:00:00Z"
}
```

---

## 2. List / Browse Restaurants
`GET /api/restaurants?city=Gazipur&cuisine=Biryani&page=0&size=10`

**Response `200 OK`**
```json
{
  "content": [
    {
      "id": "665f2a1e4a3b2c001f9b1111",
      "name": "Star Kabab",
      "cuisine": ["Bangladeshi", "Biryani"],
      "city": "Gazipur",
      "rating": 4.3,
      "isOpen": true
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

---

## 3. Get Restaurant Details
`GET /api/restaurants/{id}`

**Response `200 OK`**
```json
{
  "id": "665f2a1e4a3b2c001f9b1111",
  "ownerId": "665f1c2e4a3b2c001f9a2222",
  "name": "Star Kabab",
  "description": "Authentic Bangladeshi cuisine",
  "cuisine": ["Bangladeshi", "Biryani"],
  "address": {
    "street": "Board Bazar, Gazipur",
    "city": "Gazipur",
    "lat": 23.9587,
    "lng": 90.4203
  },
  "operatingHours": { "open": "10:00", "close": "23:00" },
  "rating": 4.3,
  "isOpen": true,
  "isApproved": true,
  "createdAt": "2026-08-15T10:00:00Z",
  "updatedAt": "2026-08-15T10:00:00Z"
}
```

**Error `404 Not Found`**
```json
{ "error": "RESTAURANT_NOT_FOUND", "message": "No restaurant found with given id" }
```

---

## 4. Update Restaurant
`PUT /api/restaurants/{id}`
Header: `Authorization: Bearer <ownerAccessToken>`

**Request**
```json
{
  "description": "Authentic Bangladeshi cuisine, now with delivery",
  "isOpen": false,
  "operatingHours": { "open": "11:00", "close": "22:00" }
}
```

**Response `200 OK`**
```json
{
  "id": "665f2a1e4a3b2c001f9b1111",
  "description": "Authentic Bangladeshi cuisine, now with delivery",
  "isOpen": false,
  "operatingHours": { "open": "11:00", "close": "22:00" },
  "updatedAt": "2026-08-15T12:00:00Z"
}
```

---

## 5. Add Menu Item
`POST /api/restaurants/{id}/menu-items`
Header: `Authorization: Bearer <ownerAccessToken>`

**Request**
```json
{
  "name": "Chicken Biryani",
  "description": "Fragrant rice with tender chicken",
  "category": "Main",
  "price": 250.0,
  "isAvailable": true,
  "imageUrl": "https://example.com/images/chicken-biryani.jpg"
}
```

**Response `201 Created`**
```json
{
  "id": "665f2b3e4a3b2c001f9b2222",
  "restaurantId": "665f2a1e4a3b2c001f9b1111",
  "name": "Chicken Biryani",
  "description": "Fragrant rice with tender chicken",
  "category": "Main",
  "price": 250.0,
  "isAvailable": true,
  "imageUrl": "https://example.com/images/chicken-biryani.jpg",
  "createdAt": "2026-08-15T10:05:00Z"
}
```

---

## 6. List Menu Items
`GET /api/restaurants/{id}/menu-items?category=Main`

**Response `200 OK`**
```json
[
  {
    "id": "665f2b3e4a3b2c001f9b2222",
    "name": "Chicken Biryani",
    "category": "Main",
    "price": 250.0,
    "isAvailable": true,
    "imageUrl": "https://example.com/images/chicken-biryani.jpg"
  },
  {
    "id": "665f2b3e4a3b2c001f9b3333",
    "name": "Beef Kabab",
    "category": "Main",
    "price": 300.0,
    "isAvailable": true,
    "imageUrl": "https://example.com/images/beef-kabab.jpg"
  }
]
```

---

## 7. Update Menu Item
`PUT /api/restaurants/{id}/menu-items/{itemId}`
Header: `Authorization: Bearer <ownerAccessToken>`

**Request**
```json
{
  "price": 270.0,
  "isAvailable": false
}
```

**Response `200 OK`**
```json
{
  "id": "665f2b3e4a3b2c001f9b2222",
  "price": 270.0,
  "isAvailable": false,
  "updatedAt": "2026-08-15T13:00:00Z"
}
```

---

## 8. Delete Menu Item
`DELETE /api/restaurants/{id}/menu-items/{itemId}`
Header: `Authorization: Bearer <ownerAccessToken>`

**Response `200 OK`**
```json
{ "message": "Menu item deleted successfully" }
```
