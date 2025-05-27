// Test script for "Create Product" request
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Response has correct structure", function () {
    const responseJson = pm.response.json();
    
    pm.expect(responseJson).to.be.an('object');
    pm.expect(responseJson.status).to.equal("OK");
    pm.expect(responseJson.statusCode).to.equal(200);
    pm.expect(responseJson.message).to.equal("Product created successfully");
    pm.expect(responseJson.data).to.be.an('object');
    
    pm.expect(responseJson.data.id).to.exist;
    pm.expect(responseJson.data.name).to.exist;
    pm.expect(responseJson.data.price).to.exist;
    pm.expect(responseJson.data.sku).to.exist;
});

// Save product ID for use in other requests
const responseJson = pm.response.json();
if (responseJson && responseJson.data && responseJson.data.id) {
    pm.environment.set("productId", responseJson.data.id);
    pm.environment.set("productSku", responseJson.data.sku);
}

// Test script for "Get Product by ID" request
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has correct structure", function () {
    const responseJson = pm.response.json();
    
    pm.expect(responseJson).to.be.an('object');
    pm.expect(responseJson.status).to.equal("OK");
    pm.expect(responseJson.statusCode).to.equal(200);
    pm.expect(responseJson.message).to.equal("Product retrieved successfully");
    pm.expect(responseJson.data).to.be.an('object');
    
    pm.expect(responseJson.data.id).to.exist;
    pm.expect(responseJson.data.name).to.exist;
    pm.expect(responseJson.data.price).to.exist;
    pm.expect(responseJson.data.sku).to.exist;
});

// Test script for "Create Category" request
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Response has correct structure", function () {
    const responseJson = pm.response.json();
    
    pm.expect(responseJson).to.be.an('object');
    pm.expect(responseJson.status).to.equal("OK");
    pm.expect(responseJson.statusCode).to.equal(200);
    pm.expect(responseJson.message).to.equal("Category created successfully");
    pm.expect(responseJson.data).to.be.an('object');
    
    pm.expect(responseJson.data.id).to.exist;
    pm.expect(responseJson.data.name).to.exist;
});

// Save category ID for use in other requests
const responseJson = pm.response.json();
if (responseJson && responseJson.data && responseJson.data.id) {
    pm.environment.set("categoryId", responseJson.data.id);
}

// Test script for "Add Category to Product" request
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has correct structure", function () {
    const responseJson = pm.response.json();
    
    pm.expect(responseJson).to.be.an('object');
    pm.expect(responseJson.status).to.equal("OK");
    pm.expect(responseJson.statusCode).to.equal(200);
    pm.expect(responseJson.message).to.equal("Category added to product successfully");
    pm.expect(responseJson.data).to.be.an('object');
    pm.expect(responseJson.data.categories).to.be.an('array');
    pm.expect(responseJson.data.categories.length).to.be.at.least(1);
});

// Test script for "Get Categories by Product ID" request
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has correct structure", function () {
    const responseJson = pm.response.json();
    
    pm.expect(responseJson).to.be.an('object');
    pm.expect(responseJson.status).to.equal("OK");
    pm.expect(responseJson.statusCode).to.equal(200);
    pm.expect(responseJson.message).to.equal("Categories retrieved successfully");
    pm.expect(responseJson.data).to.be.an('array');
});

// Pre-request script for "Update Product" request
// This can be used to set default values if they're not already set
if (!pm.environment.get("productId")) {
    console.log("Warning: productId is not set. This request may fail.");
}

// Example of a complex test script for validating error responses
pm.test("Status code is 400 for validation error", function () {
    pm.response.to.have.status(400);
});

pm.test("Error response has correct structure", function () {
    const responseJson = pm.response.json();
    
    pm.expect(responseJson).to.be.an('object');
    pm.expect(responseJson.status).to.equal("BAD_REQUEST");
    pm.expect(responseJson.statusCode).to.equal(400);
    pm.expect(responseJson.message).to.include("Validation error");
});

// Example of a workflow test that sets up the next request
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

// Setup for the next request in the workflow
const responseJson = pm.response.json();
if (responseJson && responseJson.data) {
    // Store data for use in the next request
    pm.environment.set("someValue", responseJson.data.someField);
    
    // Create a new request body based on the response
    const nextRequestBody = {
        name: responseJson.data.name + " (Updated)",
        description: "Updated from previous response",
        price: responseJson.data.price * 1.1 // Increase price by 10%
    };
    
    // Store the request body for the next request
    pm.environment.set("nextRequestBody", JSON.stringify(nextRequestBody));
} 