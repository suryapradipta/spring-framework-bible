# Spring Framework Bible Postman Collections

This directory contains Postman collections for testing and documenting the Spring Framework Bible API. These collections demonstrate best practices for API testing and documentation.

## Collections

- **Spring_Framework_Bible.postman_collection.json**: Main collection containing all API endpoints
- **Products.postman_collection.json**: Collection focusing on Product API endpoints
- **Categories.postman_collection.json**: Collection focusing on Category API endpoints
- **ProductCategory_Relationships.postman_collection.json**: Collection focusing on Product-Category relationship endpoints

## Environment

- **Spring_Framework_Bible.postman_environment.json**: Environment variables for the collections

## Getting Started

### Importing Collections and Environment

1. Open Postman
2. Click "Import" in the top left
3. Select the collection and environment files
4. Click "Import" to add them to your workspace

### Setting Up the Environment

1. In Postman, click on the "Environments" tab
2. Select "Spring Framework Bible Environment"
3. Ensure the `baseUrl` variable is set correctly (default: `http://localhost:8080`)
4. Click "Save" to apply the changes

### Using the Collections

1. Ensure the Spring Framework Bible application is running
2. Select "Spring Framework Bible Environment" from the environment dropdown
3. Open one of the collections
4. Run individual requests or use the "Run Collection" feature to execute multiple requests

## Working with Dynamic Variables

The collections use Postman variables to make testing easier:

- `{{baseUrl}}`: Base URL of the API (e.g., `http://localhost:8080`)
- `{{productId}}`: ID of a product (set automatically after creating a product)
- `{{categoryId}}`: ID of a category (set automatically after creating a category)
- `{{productSku}}`: SKU of a product (set automatically after creating a product)

These variables are automatically updated when you run the requests in sequence.

## Test Scripts

Many requests include test scripts that:

1. Validate the response status code
2. Validate the response structure
3. Extract data for use in subsequent requests
4. Set environment variables

## Documentation Features

The collections demonstrate several documentation best practices:

1. **Request Descriptions**: Each request includes a detailed description
2. **Example Responses**: Multiple example responses are provided for different scenarios
3. **Request Organization**: Requests are organized logically in folders
4. **Pre-request Scripts**: Some requests include pre-request scripts to set up the test environment
5. **Test Scripts**: Validate responses and extract data

## Testing Workflows

Here are some common testing workflows:

### Products Workflow

1. Get all products (initially empty)
2. Create a new product
3. Get the created product by ID
4. Search for the product by name
5. Update the product
6. Delete the product

### Categories Workflow

1. Get all categories (initially empty)
2. Create a new category
3. Get the created category by ID
4. Search for the category by name
5. Update the category
6. Delete the category

### Product-Category Relationship Workflow

1. Create a product
2. Create a category
3. Add the category to the product
4. Get the product to verify the category was added
5. Get categories for the product
6. Remove the category from the product

## Customizing the Collections

Feel free to modify these collections to fit your specific needs:

1. Add new requests
2. Modify existing requests
3. Add new test scripts
4. Create new workflows

## Best Practices Demonstrated

These collections demonstrate several API testing and documentation best practices:

1. **Consistent Naming**: All requests follow a consistent naming convention
2. **Clear Organization**: Requests are organized logically in folders
3. **Environment Variables**: Dynamic values are stored in environment variables
4. **Test Scripts**: Validate responses and extract data
5. **Documentation**: Detailed descriptions and examples
6. **Example Responses**: Multiple examples for different scenarios
7. **Error Handling**: Examples of error responses
8. **Workflows**: Logical sequences of requests that work together 