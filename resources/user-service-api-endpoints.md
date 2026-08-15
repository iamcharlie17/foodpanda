# User Service — API Endpoints

Base path: `/api/users`
Database: `userdb` (MongoDB) | Collection: `users`

## Quick Reference

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/users/auth/register` | Register new user |
| POST | `/api/users/auth/login` | Login, get JWT |
| GET | `/api/users/me` | Get own profile |
| PUT | `/api/users/me` | Update own profile |
| DELETE | `/api/users/me` | Deactivate account |
| POST | `/api/users/me/addresses` | Add address |
| GET | `/api/users/me/addresses` | List addresses |

---

## 1. Register
`POST /api/users/auth/register`

**Request**
```json
{
  "name": "Riyad Hosen",
  "email": "riyad@example.com",
  "password": "Secret@123",
  "phone": "01712345678",
  "role": "CUSTOMER"
}
```

**Response `201 Created`**
```json
{
  "id": "665f1c2e4a3b2c001f9a1111",
  "name": "Riyad Hosen",
  "email": "riyad@example.com",
  "role": "CUSTOMER",
  "createdAt": "2026-08-15T10:12:00Z"
}
```

**Error `409 Conflict`**
```json
{ "error": "EMAIL_ALREADY_EXISTS", "message": "Email is already registered" }
```

---

## 2. Login
`POST /api/users/auth/login`

**Request**
```json
{
  "email": "riyad@example.com",
  "password": "Secret@123"
}
```

**Response `200 OK`**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2NjVmMWMy...",
  "expiresIn": 3600,
  "user": {
    "id": "665f1c2e4a3b2c001f9a1111",
    "name": "Riyad Hosen",
    "role": "CUSTOMER"
  }
}
```

**Error `401 Unauthorized`**
```json
{ "error": "INVALID_CREDENTIALS", "message": "Email or password is incorrect" }
```

---

## 3. Get Profile
`GET /api/users/me`
Header: `Authorization: Bearer <accessToken>`

**Response `200 OK`** — maps directly to the `users` document
```json
{
  "id": "665f1c2e4a3b2c001f9a1111",
  "name": "Riyad Hosen",
  "email": "riyad@example.com",
  "phone": "01712345678",
  "role": "CUSTOMER",
  "addresses": [
    {
      "label": "Home",
      "street": "House 12, Road 4, Gazipur",
      "city": "Gazipur",
      "lat": 23.9999,
      "lng": 90.4203,
      "isDefault": true
    }
  ],
  "isActive": true,
  "createdAt": "2026-08-15T10:12:00Z",
  "updatedAt": "2026-08-15T10:12:00Z"
}
```

---

## 4. Update Profile
`PUT /api/users/me`
Header: `Authorization: Bearer <accessToken>`

**Request**
```json
{
  "name": "Riyad H. Hosen",
  "phone": "01799998888"
}
```

**Response `200 OK`**
```json
{
  "id": "665f1c2e4a3b2c001f9a1111",
  "name": "Riyad H. Hosen",
  "phone": "01799998888",
  "updatedAt": "2026-08-15T11:00:00Z"
}
```

---

## 5. Deactivate Account
`DELETE /api/users/me`
Header: `Authorization: Bearer <accessToken>`

**Response `200 OK`**
```json
{ "message": "Account deactivated successfully" }
```

*(sets `isActive: false` on the document rather than a hard delete)*

---

## 6. Add Address
`POST /api/users/me/addresses`
Header: `Authorization: Bearer <accessToken>`

**Request**
```json
{
  "label": "Work",
  "street": "IUT Campus, Board Bazar",
  "city": "Gazipur",
  "lat": 23.9587,
  "lng": 90.4203,
  "isDefault": false
}
```

**Response `201 Created`**
```json
{
  "message": "Address added successfully",
  "addresses": [
    {
      "label": "Home",
      "street": "House 12, Road 4, Gazipur",
      "city": "Gazipur",
      "lat": 23.9999,
      "lng": 90.4203,
      "isDefault": true
    },
    {
      "label": "Work",
      "street": "IUT Campus, Board Bazar",
      "city": "Gazipur",
      "lat": 23.9587,
      "lng": 90.4203,
      "isDefault": false
    }
  ]
}
```

---

## 7. List Addresses
`GET /api/users/me/addresses`
Header: `Authorization: Bearer <accessToken>`

**Response `200 OK`** — `addresses` array from the `users` document
```json
[
  {
    "label": "Home",
    "street": "House 12, Road 4, Gazipur",
    "city": "Gazipur",
    "lat": 23.9999,
    "lng": 90.4203,
    "isDefault": true
  },
  {
    "label": "Work",
    "street": "IUT Campus, Board Bazar",
    "city": "Gazipur",
    "lat": 23.9587,
    "lng": 90.4203,
    "isDefault": false
  }
]
```
