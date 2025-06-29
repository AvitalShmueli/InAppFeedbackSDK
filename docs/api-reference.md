# API Service Documentation
The In-App Feedback Backend provides a RESTful API for managing feedback forms and collecting user feedback across multiple applications.
All endpoints return JSON responses and use standard HTTP status codes.

## Base URL
```
https://feedback-backend-one.vercel.app
```

## Authentication
Currently, the API does not require authentication for most endpoints.
This might be changed in the future.


## Form Management

### Create Feedback Form
Creates a new feedback form for a specific application package.

**Endpoint:** `POST /admin/forms`

**Request Body:**
```json
{
    "package_name": "com.example.app",
    "title": "We value your feedback",
    "form_type": "rating_text"
}
```

**Parameters:**

- `package_name` (string, required): Unique identifier for the application
- `title` (string, required): Display description for the feedback form
- `form_type` (string, required): Type of form - rating, free_text, or rating_text

**Response Codes:**

- **201** - Form created successfully
- **400** - Invalid form data
- **500** - Internal server error

**Example Response:**
```json
{
    "form_id": "64f8a1b2c3d4e5f6a7b8c9d0",
    "package_name": "com.example.app",
    "title": "Feedback Form",
    "form_type": "rating_text",
    "is_active": true,
    "created_at": "2025-06-15T10:30:00Z"
}
```

### Get All Package Names
Retrieves all unique package names that have feedback forms.

**Endpoint:** `GET /forms/packages`

**Response Codes:**
- **200** - Successfully retrieved package names
- **500** - Internal server error

**Example Response:**
```json
[
    "com.example.app1",
    "com.example.app2",
    "com.mycompany.mobileapp"
]
```

### Get Active Form
Retrieves the currently active feedback form for a specific package.

**Endpoint:** `GET /forms/{package_name}`

**Path Parameters:**
- `package_name` (string): The application package identifier

**Response Codes:**
- **200** - Active form retrieved successfully
- **404** - Package not found or no active form
- **500** - Internal server error

**Example Response:**
```json
{
    "form_id": "64f8a1b2c3d4e5f6a7b8c9d0",
    "package_name": "com.example.app",
    "title": "Help us improve your experience",
    "form_type": "rating_text",
    "is_active": true,
    "created_at": "2025-06-15T10:30:00Z"
}
```

### Get All Forms by Package
Retrieves all feedback forms for a specific package, with optional filtering by active status.

**Endpoint:** `GET /forms/{package_name}/all`

**Path Parameters:**
- `package_name` (string): The application package identifier

**Query Parameters:**
- `active` (string, optional): Filter by status - active or inactive

**Response Codes:**
- **200** - Forms retrieved successfully
- **404** - Package not found
- **500** - Internal server error


### Get All Forms
Retrieves all feedback forms across all packages.
*Endpoint:* `GET /forms/all`

**Query Parameters:**
- `active` (string, optional): Filter by status - active or inactive

**Response Codes:**
- **200** - Forms retrieved successfully
- **400** - Invalid status parameter
- **404** - No forms found
- **500** - Internal server error


### Activate Form
Updates the active status of a feedback form. Only one form can be active per package.

**Endpoint:** `PUT /forms/{form_id}/activate`

**Path Parameters:**
- `form_id` (string): The unique form identifier

**Request Body:**
```json
{
    "is_active": true
}
```

**Response Codes:**
- **200** - Status updated successfully
- **400** - Missing or invalid parameter
- **404** - Form not found
- **500** - Internal server error


### Search Forms
Search for forms using various filters.

**Endpoint:** `GET /forms/search`

**Query Parameters:**
- `package_name` (string, optional): Filter by package name
- `title` (string, optional): Search in form titles
- `form_type` (string, optional): Filter by type - rating, free_text, or rating_text
- `active` (string, optional): Filter by status - active or inactive

**Response Codes:**
- **200** - Search results retrieved successfully
- **500** - Internal server error

---

## Feedback Submission and Analysis

### Submit Feedback
Submits new user feedback for a specific application.

**Endpoint:** `POST /feedback`

**Request Body:**
```json
{
    "app_version": "1.0.5",
    "device_info": "iPhone 14 Pro",
    "form_id": "64f8a1b2c3d4e5f6a7b8c9d0",
    "message": "The app is great but could use dark mode",
    "package_name": "com.example.myapp",
    "rating": 4,
    "user_id": "user123"
}
```

**Parameters:**
- `app_version` (string, optional): Version of the application
- `device_info` (string, optional): Information about the user's device
- `form_id` (string, required): ID of the feedback form
- `message` (string, optional): User's text feedback
- `package_name` (string, required): Application package identifier
- `rating` (integer, optional): User rating from 1-5
- `user_id` (string, optional): Unique identifier for the user

**Response Codes:**
- **201** - Feedback submitted successfully
- **400** - Invalid feedback data
- **404** - Form not found
- **500** - Internal server error

**Example Response:**
```json
{
    "feedback_id": "64f8a1b2c3d4e5f6a7b8c9d1",
    "submitted_at": "2024-01-15T14:30:00Z"
}
```


### Get Package Feedback
Retrieves all feedback submissions for a specific package.

**Endpoint:** `GET /feedback/{package_name}`

**Path Parameters:**
- `package_name` (string): The application package identifier

**Query Parameters:**
- `form_id` (string, optional): Filter by specific form ID

**Response Codes:**
- **200** - Feedback retrieved successfully
- **404** - Package not found
- **500** - Internal server error


### Get Feedback Details
Retrieves detailed information about a specific feedback submission.

**Endpoint:** `GET /feedback/{package_name}/{feedback_id}`

**Path Parameters:**
- `package_name` (string): The application package identifier
- `feedback_id` (string): The unique feedback identifier

**Response Codes:**
- **200** - Feedback details retrieved successfully
- **404** - Feedback not found
- **500** - Internal server error


### Get User Feedback
Retrieves all feedback submissions from a specific user for a package.

**Endpoint:** `GET /feedback/{package_name}/user/{user_id}`

**Path Parameters:**
- `package_name` (string): The application package identifier
- `user_id` (string): The unique user identifier

**Response Codes:**
- **200** - User feedback retrieved successfully
- **404** - Package not found
- **500** - Internal server error


### Get Average Rating
Calculates and returns the average rating for a package.

**Endpoint:** `GET /feedback/{package_name}/average-rating`

**Path Parameters:**
- `package_name` (string): The application package identifier

**Query Parameters:**
- `form_id` (string, optional): Calculate average for specific form only

**Response Codes:**
- **200** - Average rating calculated successfully
- **404** - Package or form not found
- **500** - Internal server error

**Example Response:**
```json
{
    "average_rating": 4.2,
    "total_ratings": 150
}
```


### Get Feedback Statistics
Retrieves comprehensive statistics for package feedback.

**Endpoint:** `GET /feedback/{package_name}/stats`

**Path Parameters:**
- `package_name` (string): The application package identifier

**Query Parameters:**
- `form_id` (string, optional): Get statistics for specific form only

**Response Codes:**
- **200** - Statistics retrieved successfully
- **404** - Package or form not found
- **500** - Internal server error

**Example Response:**
```json
{
    "average_rating": 4.2,
    "rating_breakdown": {
    "1": 5,
    "2": 10,
    "3": 25,
    "4": 110,
    "5": 100
    },
    "total_feedback": 250
}
```

### Search Feedback
Searches through feedback messages for specific content.

**Endpoint:** `GET /feedback/{package_name}/search`

**Path Parameters:**
- `package_name` (string): The application package identifier

**Query Parameters:**
- `query` (string, optional): Search term to find in feedback messages

**Response Codes:**
- **200** - Search results retrieved successfully
- **404** - Package not found
- **500** - Internal server error


### Get Recent Feedback
Retrieves the most recent feedback submissions for a package.

**Endpoint:** `GET /feedback/{package_name}/recent`

**Path Parameters:**
- `package_name` (string): The application package identifier

**Query Parameters:**
- `form_id` (string, optional): Filter by specific form ID
- `limit` (integer, optional): Number of recent items to return (default: 10)

**Response Codes:**
- **200** - Recent feedback retrieved successfully
- **404** - Package not found
- **500** - Internal server error


### Delete Feedback
Deletes a specific feedback submission.

**Endpoint:** `DELETE /feedback/{package_name}/{feedback_id}`

**Path Parameters:**
- `package_name` (string): The application package identifier
- `feedback_id` (string): The unique feedback identifier

**Response Codes:**
- **200** - Feedback deleted successfully
- **404** - Feedback not found
- **500** - Internal server error


### Delete Form Feedback
Deletes all feedback submissions for a specific form.

**Endpoint:** `DELETE /feedback/{package_name}/form/{form_id}`

**Path Parameters:**
- `package_name` (string): The application package identifier
- `form_id` (string): The unique form identifier

**Response Codes:**
- **200** - Form feedback deleted successfully
- **404** - Package or form not found
- **500** - Internal server error


### Delete All Package Feedback
Deletes all feedback submissions for a package.

**Endpoint:** `DELETE /feedback/{package_name}`

**Path Parameters:**
- `package_name` (string): The application package identifier

**Response Codes:**
- **200** - Package feedback deleted successfully
- **404** - Package not found
- **500** - Internal server error

---

## Form Types
The system supports three types of feedback forms:
- Rating Form (rating)
    Allows users to provide a numeric rating from 1-5
    Useful for quick satisfaction surveys
    Minimal user interaction required

- Free Text Form (free_text)
    Allows users to provide written feedback
    Ideal for detailed user insights and suggestions
    No character limits enforced

- Combined Form (rating_text)
    Combines both rating and text feedback
    Provides quantitative and qualitative data
    Most comprehensive feedback option


## Interactive API Documentation
For interactive API testing and detailed request/response examples, visit the Swagger UI documentation at:

[http://feedback-backend-one.vercel.app/apidocs/](http://feedback-backend-one.vercel.app/apidocs/)

This provides a web interface where you can test API endpoints directly and see real-time responses.
