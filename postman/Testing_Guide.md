# Spring Framework Bible API Testing Guide

This guide provides detailed instructions for using Postman to test the Spring Framework Bible API. It covers basic testing workflows, advanced techniques, and best practices.

## Table of Contents

1. [Setting Up Postman](#setting-up-postman)
2. [Basic Testing Workflow](#basic-testing-workflow)
3. [Testing Product Endpoints](#testing-product-endpoints)
4. [Testing Category Endpoints](#testing-category-endpoints)
5. [Testing Relationship Endpoints](#testing-relationship-endpoints)
6. [Advanced Testing Techniques](#advanced-testing-techniques)
7. [Troubleshooting](#troubleshooting)

## Setting Up Postman

### Prerequisites

- Postman installed (Download from [postman.com](https://www.postman.com/downloads/))
- Spring Framework Bible application running locally

### Import Collections and Environment

1. Open Postman
2. Click "Import" in the top left
3. Select all files from the `postman` directory
4. Click "Import" to add them to your workspace

### Configure Environment

1. Click the Environment dropdown in the top right
2. Select "Spring Framework Bible Environment"
3. Verify the `baseUrl` is set to `http://localhost:8080` (or your custom URL)
4. Click the checkmark to save any changes

## Basic Testing Workflow

Follow this general workflow to test the API:

1. **Start with a clean database** (restart the application if needed)
2. **Create test data** (products and categories)
3. **Test read operations** (GET endpoints)
4. **Test update operations** (PUT endpoints)
5. **Test relationship operations** (add/remove categories)
6. **Test delete operations** (DELETE endpoints)

## Testing Product Endpoints

### Create a Product

1. Open the "Products" collection
2. Select "Create Product"
3. Verify the request body contains valid product data:
   ```json
   {
     "name": "Test Product",
     "description": "A product for testing",
     "price": 19.99,
     "sku": "TEST-001"
   }
   ```
4. Click "Send"
5. Verify the response has a 201 status code
6. Verify the response body contains the created product with an ID
7. Note that the `productId` and `productSku` environment variables are automatically set

### Get All Products

1. Select "Get All Products"
2. Click "Send"
3. Verify the response has a 200 status code
4. Verify the response body contains an array of products

### Get Product by ID

1. Select "Get Product by ID"
2. Verify that `{{productId}}` is in the URL
3. Click "Send"
4. Verify the response has a 200 status code
5. Verify the response body contains the requested product

### Update a Product

1. Select "Update Product"
2. Verify that `{{productId}}` is in the URL
3. Modify the request body:
   ```json
   {
     "name": "Updated Product",
     "description": "This product has been updated",
     "price": 29.99
   }
   ```
4. Click "Send"
5. Verify the response has a 200 status code
6. Verify the response body contains the updated product

### Delete a Product

1. Select "Delete Product"
2. Verify that `{{productId}}` is in the URL
3. Click "Send"
4. Verify the response has a 204 status code (No Content)

## Testing Category Endpoints

### Create a Category

1. Open the "Categories" collection
2. Select "Create Category"
3. Verify the request body contains valid category data:
   ```json
   {
     "name": "Test Category",
     "description": "A category for testing"
   }
   ```
4. Click "Send"
5. Verify the response has a 201 status code
6. Verify the response body contains the created category with an ID
7. Note that the `categoryId` environment variable is automatically set

### Get All Categories

1. Select "Get All Categories"
2. Click "Send"
3. Verify the response has a 200 status code
4. Verify the response body contains an array of categories

### Get Category by ID

1. Select "Get Category by ID"
2. Verify that `{{categoryId}}` is in the URL
3. Click "Send"
4. Verify the response has a 200 status code
5. Verify the response body contains the requested category

### Update a Category

1. Select "Update Category"
2. Verify that `{{categoryId}}` is in the URL
3. Modify the request body:
   ```json
   {
     "name": "Updated Category",
     "description": "This category has been updated"
   }
   ```
4. Click "Send"
5. Verify the response has a 200 status code
6. Verify the response body contains the updated category

### Delete a Category

1. Select "Delete Category"
2. Verify that `{{categoryId}}` is in the URL
3. Click "Send"
4. Verify the response has a 204 status code (No Content)

## Testing Relationship Endpoints

### Add Category to Product

1. First, create a product (if not already done)
2. Then, create a category (if not already done)
3. Open the "Product-Category Relationships" collection
4. Select "Add Category to Product"
5. Verify that `{{productId}}` and `{{categoryId}}` are in the URL
6. Click "Send"
7. Verify the response has a 200 status code
8. Verify the response body contains the product with the category in its `categories` array

### Get Categories for a Product

1. Open the "Categories" collection
2. Select "Get Categories by Product ID"
3. Verify that `{{productId}}` is in the URL
4. Click "Send"
5. Verify the response has a 200 status code
6. Verify the response body contains the category you added

### Remove Category from Product

1. Open the "Product-Category Relationships" collection
2. Select "Remove Category from Product"
3. Verify that `{{productId}}` and `{{categoryId}}` are in the URL
4. Click "Send"
5. Verify the response has a 200 status code
6. Verify the response body contains the product with an empty `categories` array

## Advanced Testing Techniques

### Running a Collection

You can run an entire collection to test all endpoints in sequence:

1. Click the "..." (more actions) next to the collection name
2. Select "Run collection"
3. Configure the run settings
4. Click "Run"
5. Review the results

### Using Pre-request Scripts

Pre-request scripts can be used to set up the request:

```javascript
// Set a random SKU for each run
pm.environment.set("randomSku", "SKU-" + Math.floor(Math.random() * 10000));
```

Then, in your request body, use `{{randomSku}}` as the SKU value.

### Using Test Scripts

Test scripts validate the response:

```javascript
// Check status code
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

// Check response structure
pm.test("Response has correct structure", function () {
    const responseJson = pm.response.json();
    pm.expect(responseJson.status).to.equal("OK");
    pm.expect(responseJson.data).to.be.an('object');
});
```

### Using Visualizations

You can create visualizations to display response data:

```javascript
const template = `
<div>
    <h3>{{product.name}}</h3>
    <p>Price: ${{product.price}}</p>
</div>
`;

const response = pm.response.json();
if (response && response.data) {
    pm.visualizer.set(template, { product: response.data });
}
```

## Troubleshooting

### Common Issues

1. **Environment variables not set**
   - Check the environment is selected
   - Verify the variables have values

2. **404 Not Found errors**
   - Verify the application is running
   - Check the `baseUrl` environment variable

3. **400 Bad Request errors**
   - Check your request body for validation errors
   - Verify required fields are present

4. **Relationship issues**
   - Ensure both the product and category exist
   - Check IDs are correct

### Getting Help

If you encounter issues:

1. Check the Spring Framework Bible README
2. Review the API documentation
3. Examine the server logs for errors

## Further Resources

- [Postman Learning Center](https://learning.postman.com/)
- [Spring Framework Documentation](https://docs.spring.io/spring-framework/reference/)
- [REST API Best Practices](https://restfulapi.net/) 