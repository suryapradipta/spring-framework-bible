// Visualization script for "Get All Products" request
// Add this to the "Tests" tab in Postman to create a visualization
// This will create a simple table of all products

const template = `
<style>
    .product-table {
        border-collapse: collapse;
        width: 100%;
        font-family: Arial, sans-serif;
    }
    .product-table th, .product-table td {
        border: 1px solid #ddd;
        padding: 8px;
        text-align: left;
    }
    .product-table th {
        background-color: #f2f2f2;
        color: #333;
    }
    .product-table tr:nth-child(even) {
        background-color: #f9f9f9;
    }
    .product-table tr:hover {
        background-color: #f1f1f1;
    }
    .empty-message {
        color: #999;
        font-style: italic;
    }
</style>

<h2>Product List</h2>
<table class="product-table">
    <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>SKU</th>
            <th>Price</th>
            <th>Categories</th>
        </tr>
    </thead>
    <tbody>
        {{#each products}}
        <tr>
            <td>{{id}}</td>
            <td>{{name}}</td>
            <td>{{sku}}</td>
            <td>${{price}}</td>
            <td>
                {{#if categories.length}}
                    {{#each categories}}
                        {{name}}{{#unless @last}}, {{/unless}}
                    {{/each}}
                {{else}}
                    <span class="empty-message">No categories</span>
                {{/if}}
            </td>
        </tr>
        {{else}}
        <tr>
            <td colspan="5" class="empty-message">No products found</td>
        </tr>
        {{/each}}
    </tbody>
</table>
`;

// Get the response data
const response = pm.response.json();

// Check if the response has the expected structure
if (response && response.data) {
    // Create the visualization
    pm.visualizer.set(template, {
        products: response.data
    });
}

// Visualization script for "Get Product by ID" request
// This will create a detailed view of a single product

const template = `
<style>
    .product-card {
        border: 1px solid #ddd;
        border-radius: 8px;
        padding: 20px;
        margin: 10px 0;
        font-family: Arial, sans-serif;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .product-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
    }
    .product-title {
        margin: 0;
        color: #333;
    }
    .product-sku {
        color: #666;
        font-size: 14px;
    }
    .product-price {
        font-size: 24px;
        color: #2a7de1;
        font-weight: bold;
    }
    .product-description {
        color: #666;
        margin-bottom: 20px;
        line-height: 1.5;
    }
    .category-list {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
    }
    .category-tag {
        background-color: #f1f1f1;
        border-radius: 16px;
        padding: 4px 12px;
        font-size: 14px;
        color: #666;
    }
    .dates {
        margin-top: 20px;
        font-size: 12px;
        color: #999;
    }
</style>

<div class="product-card">
    <div class="product-header">
        <div>
            <h2 class="product-title">{{product.name}}</h2>
            <div class="product-sku">SKU: {{product.sku}}</div>
        </div>
        <div class="product-price">${{product.price}}</div>
    </div>
    
    <div class="product-description">
        {{#if product.description}}
            {{product.description}}
        {{else}}
            <em>No description available</em>
        {{/if}}
    </div>
    
    <h3>Categories</h3>
    <div class="category-list">
        {{#each product.categories}}
            <div class="category-tag">{{name}}</div>
        {{else}}
            <em>No categories assigned</em>
        {{/each}}
    </div>
    
    <div class="dates">
        <div>Created: {{formatDate product.createdAt}}</div>
        <div>Last Updated: {{formatDate product.updatedAt}}</div>
    </div>
</div>
`;

// Helper function to format dates
function formatDate(dateStr) {
    if (!dateStr) return 'N/A';
    const date = new Date(dateStr);
    return date.toLocaleString();
}

// Get the response data
const response = pm.response.json();

// Check if the response has the expected structure
if (response && response.data) {
    // Create the visualization
    pm.visualizer.set(template, {
        product: response.data,
        formatDate: formatDate
    });
}

// Visualization script for "Get All Categories" request
// This will create a simple list of categories

const template = `
<style>
    .category-list {
        font-family: Arial, sans-serif;
    }
    .category-item {
        border: 1px solid #ddd;
        border-radius: 4px;
        padding: 10px;
        margin-bottom: 10px;
    }
    .category-name {
        font-weight: bold;
        margin-bottom: 5px;
    }
    .category-description {
        color: #666;
    }
    .empty-message {
        color: #999;
        font-style: italic;
    }
</style>

<h2>Categories</h2>
<div class="category-list">
    {{#each categories}}
    <div class="category-item">
        <div class="category-name">{{name}} (ID: {{id}})</div>
        <div class="category-description">
            {{#if description}}
                {{description}}
            {{else}}
                <span class="empty-message">No description</span>
            {{/if}}
        </div>
    </div>
    {{else}}
    <div class="empty-message">No categories found</div>
    {{/each}}
</div>
`;

// Get the response data
const response = pm.response.json();

// Check if the response has the expected structure
if (response && response.data) {
    // Create the visualization
    pm.visualizer.set(template, {
        categories: response.data
    });
} 