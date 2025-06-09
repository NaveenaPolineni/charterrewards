# Retailer Rewards

A Springboot-based application for managing and tracking customer rewards.

A retailer offers a rewards program to its customers, awarding points based on each recorded purchase.
A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent between $50 and $100 in each transaction.
Example: $120 → 2×20 + 1×50 = 90 points

	•	1 point for every dollar spent over $50
	•	2 points for every dollar spent over $100

## Features

- Customer rewards calculation
- Transaction management (Might be better off being in a different rest service. Implementing it here for this assignment.)
- Customer management (Might be better off being in a different rest service. Implementing it here for this assignment.)
- RESTful API endpoints
- Modular and extensible architecture

## API Endpoints

### Get Customer Rewards
```
GET /api/v1/customers/{customerId}/rewards?startDate={LocalDate}&endDate={LocalDate}
```
- Returns the total rewards points for a specific customer, also points grouped by month.
- StartDate and endDate are optional, If provided API will return total reward points between those date
- If only startDate is provided, then API will return total reward points from startDate till current Date
- If only endDate is provided, then API will return total reward points 3 months prior to endDate.

- without any Dates
```
GET http://localhost:8080/api/v1/customers/1/rewards

Response:
{
    "customerId": 1,
    "firstName": "Naveena",
    "lastName": "Polineni",
    "totalRewardsPoints": 461,
    "monthlyTransactions": [
        {
            "month": "JUNE",
            "totalMonthlyRewardsPoints": 171,
            "transactions": [
                {
                    "purchaseDate": "2025-06-05",
                    "amount": 85.45
                },
                {
                    "purchaseDate": "2025-06-05",
                    "amount": 143.25
                }
            ]
        },
        {
            "month": "MAY",
            "totalMonthlyRewardsPoints": 76,
            "transactions": [
                {
                    "purchaseDate": "2025-05-05",
                    "amount": 112
                },
                {
                    "purchaseDate": "2025-05-09",
                    "amount": 52
                }
            ]
        },
        {
            "month": "APRIL",
            "totalMonthlyRewardsPoints": 214,
            "transactions": [
                {
                    "purchaseDate": "2025-04-23",
                    "amount": 182
                }
            ]
        }
    ]
}

```
- with validDateRange
```
GET http://localhost:8080/api/v1/customers/1/rewards?startDate=2025-04-01&endDate=2025-05-09

Response:
{
    "customerId": 1,
    "firstName": "Naveena",
    "lastName": "Polineni",
    "totalRewardsPoints": 290,
    "monthlyTransactions": [
        {
            "month": "MAY",
            "totalMonthlyRewardsPoints": 76,
            "transactions": [
                {
                    "purchaseDate": "2025-05-05",
                    "amount": 112
                },
                {
                    "purchaseDate": "2025-05-09",
                    "amount": 52
                }
            ]
        },
        {
            "month": "APRIL",
            "totalMonthlyRewardsPoints": 214,
            "transactions": [
                {
                    "purchaseDate": "2025-04-23",
                    "amount": 182
                }
            ]
        }
    ]
}

```
- With only endDate
```
GET http://localhost:8080/api/v1/customers/2/rewards?endDate=2025-05-01

Response:
{
    "customerId": 2,
    "firstName": "Sai",
    "lastName": "Polineni",
    "totalRewardsPoints": 114,
    "monthlyTransactions": [
        {
            "month": "MARCH",
            "totalMonthlyRewardsPoints": 114,
            "transactions": [
                {
                    "purchaseDate": "2025-03-23",
                    "amount": 132
                }
            ]
        }
    ]
}

```

- With only startDate
```
GET http://localhost:8080/api/v1/customers/2/rewards?startDate=2025-01-01

Response:
{
    "customerId": 2,
    "firstName": "Sai",
    "lastName": "Polineni",
    "totalRewardsPoints": 310,
    "monthlyTransactions": [
        {
            "month": "JUNE",
            "totalMonthlyRewardsPoints": 154,
            "transactions": [
                {
                    "purchaseDate": "2025-06-08",
                    "amount": 152
                }
            ]
        },
        {
            "month": "JANUARY",
            "totalMonthlyRewardsPoints": 42,
            "transactions": [
                {
                    "purchaseDate": "2025-01-17",
                    "amount": 92
                }
            ]
        },
        {
            "month": "MARCH",
            "totalMonthlyRewardsPoints": 114,
            "transactions": [
                {
                    "purchaseDate": "2025-03-23",
                    "amount": 132
                }
            ]
        }
    ]
}

```

- Invalid date produces 400 Error

```
GET http://localhost:8080/api/v1/customers/2/rewards?startDate=2025-09-01

Response: 
code: 400 Bad request
message: Future dates are not allowed. startDate provided should be in the past or today.
```


### Get All Customers
```
GET /api/v1/customers/{customerId}
```
Returns customer Details.
```
Example:
GET http://localhost:8080/api/v1/customers/1

{
    "customerId": 1,
    "firstName": "Naveena",
    "lastName": "Polineni",
    "email": "naveenam2203@gmail.com"
}
```

### Add a Customer
```
POST /api/v1/customers
```
Creates a new transaction for a customer.  
**Request Body:**
```json
{
    "firstName": "Charter",
    "lastName": "Communications",
    "email": "chartercommunications@test.com"
}
```

```
Example:
GET http://localhost:8080/api/v1/customers

Request Body:
{
    "firstName": "Charter",
    "lastName": "Communications",
    "email": "chartercommunications@test.com"
}

Response:
{
    "customerId": 3,
    "firstName": "Charter",
    "lastName": "Communications",
    "email": "chartercommunications@test.com"
}
```
### Delete a Customer
```
DELETE /api/v1/customers/{customerId}
```
Deletes the customer by Id. 

### Get Customer Transactions
```
GET /api/v1/customers/{customerId}/transactions
```
Returns all transactions for a specific customer.

```
Example:
GET http://localhost:8080/api/v1/customers/1/transactions

[
    {
        "transactionId": 1,
        "purchaseDate": "2025-06-05",
        "customerId": 1,
        "amount": 85.45
    },
    {
        "transactionId": 2,
        "purchaseDate": "2025-06-05",
        "customerId": 1,
        "amount": 143.25
    },
    {
        "transactionId": 3,
        "purchaseDate": "2025-05-05",
        "customerId": 1,
        "amount": 112
    },
    {
        "transactionId": 4,
        "purchaseDate": "2025-05-09",
        "customerId": 1,
        "amount": 52
    },
    {
        "transactionId": 5,
        "purchaseDate": "2025-04-23",
        "customerId": 1,
        "amount": 182
    }
]
```

### Add a Transaction
```
POST /api/v1/transactions
```
Creates a new transaction for a customer.  
**Request Body:**
```json
{
    "customerId": 2,
    "amount": 152.00,
    "purchaseDate": "2025-06-08"
}
```

```
Example:
GET http://localhost:8080/api/v1/transactions

Request Body:
{
    "customerId": 2,
    "amount": 152.00,
    "purchaseDate": "2025-06-08"
}

Response:
{
    "transactionId": 8,
    "purchaseDate": "2025-06-08",
    "customerId": 2,
    "amount": 152
}
```
### Delete a Transaction
```
DELETE /api/v1/transactions/{transactionId}
```
Deletes a transaction by Id.  

## Technologies Used

- Java
- Spring Boot
- Maven
- Embedded H2
## Getting Started

1. **Clone the repository:**
    ```bash
    git clone https://github.com/NaveenaPolineni/charterrewards.git
    ```

2. **Navigate to the project directory:**
    ```bash
    cd charterrewards
    ```

3. **Build the project:**
    ```bash
    mvn clean install
    ```

4. **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

5. **Test the API:**
 -  Use the [Rewards Application Postman Collection](Rewards_application.postman_collection.json) to test the API.