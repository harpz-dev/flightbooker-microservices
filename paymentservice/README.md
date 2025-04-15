# PaymentService

## Overview
PaymentService handles all payment transactions for the flight booking system.

## Features
- Processes payments for bookings.
- Validates transactions.
- Notifies users about payment status.

## API Endpoints
- `POST /payment/process` - Processes a payment.
- `GET /payment/status/{id}` - Retrieves the status of a payment.

## Configuration
- Uses Kafka for payment event handling.
- Database: PostgreSQL.

## Running the Service
```sh
docker-compose up -d payment-service
