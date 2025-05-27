package example.springframeworkbible.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 * This is a common exception in REST APIs for 404 Not Found responses.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Creates a new ResourceNotFoundException with a specific message.
     *
     * @param message The error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Creates a new ResourceNotFoundException for a specific resource type and ID.
     *
     * @param resourceName The name of the resource type (e.g., "Product")
     * @param fieldName The name of the identifier field (e.g., "id")
     * @param fieldValue The value of the identifier that was not found
     * @return A new ResourceNotFoundException with a formatted message
     */
    public static ResourceNotFoundException create(String resourceName, String fieldName, Object fieldValue) {
        return new ResourceNotFoundException(
                String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
} 