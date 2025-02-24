# Discount Calculator

This application allows you to save and retrieve orders. When an order is saved, a discount is automatically calculated based on the total amount.

## Prerequisites
- Docker

## Getting Started

1. Clone the repository:
   ```sh
   git clone https://github.com/alex-schiller1990/DiscountCalculator.git
   ```  
2. Navigate to the project directory:
   ```sh
   cd DiscountCalculator
   ```  
3. Start the application using Docker Compose:
   ```sh
   docker-compose up
   ```  

## API Endpoints

### Create an Order
**POST** `/orders`
- Calculates the discount and saves the order in the database.
- **Request Body:**
  ```json
  {
    "customerName": "customer",
    "items": [
      {
        "productName": "product",
        "price": 1000.0,
        "quantity": 1
      }
    ]
  }
  ```  

### Retrieve All Orders
**GET** `/orders`
- Returns a list of all orders.

### Retrieve a Specific Order
**GET** `/order/{id}`
- Retrieves the order with the given ID.

### Sample Response
```json
{
  "orderId": 1,
  "customerName": "customer",
  "totalAmount": 1000.0,
  "discountedAmount": 850.0,
  "discountPercentage": 10
}
```  

## Discount Logic

| Total Amount ($) | Discount (%) |
|-----------------|-------------|
| Less than 100   | 0%          |
| 100 - 500       | 5%          |
| 500 - 1000      | 10%         |
| More than 1000  | 15%         |

