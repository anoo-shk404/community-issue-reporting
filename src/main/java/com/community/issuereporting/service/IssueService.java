package com.community.issuereporting.service;
import com.community.issuereporting.entity.Category;
import com.community.issuereporting.entity.Issue;
import com.community.issuereporting.entity.IssueStatus;
import com.community.issuereporting.entity.User;
import com.community.issuereporting.repository.IssueRepository;
import com.community.issuereporting.repository.UserRepository;
import com.community.issuereporting.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;
    
    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository
    
    @Autowired
    private CategoryRepository categoryRepository; // Assuming you have a CategoryRepository

    public Issue createIssue(Issue issue) {
        return issueRepository.save(issue);
    }

    // Updated method to handle username-based user identification
    public Issue createIssueWithImage(String title, String description, String location,
            String userId, Long categoryId, MultipartFile image,
            Double latitude, Double longitude) {
System.out.println("=== DEBUG: Service Method Called ===");
System.out.println("Service - Title: " + title);
System.out.println("Service - Description: " + description);
System.out.println("Service - Location: " + location);
System.out.println("Service - UserId: " + userId);
System.out.println("Service - CategoryId: " + categoryId);
System.out.println("Service - Latitude: " + latitude);
System.out.println("Service - Longitude: " + longitude);

try {
// Find user by username or ID
System.out.println("DEBUG: Looking for user with identifier: " + userId);
User user = findUserByIdOrUsername(userId);
if (user == null) {
System.err.println("ERROR: User not found with identifier: " + userId);
throw new RuntimeException("User not found with identifier: " + userId);
}
System.out.println("SUCCESS: Found user - ID: " + user.getId() + ", Username: " + user.getUsername());

// Find category
System.out.println("DEBUG: Looking for category with ID: " + categoryId);
Category category = categoryRepository.findById(categoryId)
.orElseThrow(() -> {
System.err.println("ERROR: Category not found with id: " + categoryId);
return new RuntimeException("Category not found with id: " + categoryId);
});
System.out.println("SUCCESS: Found category - ID: " + category.getId() + ", Name: " + category.getName());

// Create new Issue
System.out.println("DEBUG: Creating new Issue object");
Issue issue = new Issue();
issue.setTitle(title);
issue.setDescription(description);
issue.setLocation(location);
issue.setReportedBy(user);
issue.setCategory(category);

System.out.println("DEBUG: Setting issue status to PENDING");
try {
issue.setStatus(IssueStatus.PENDING);
System.out.println("SUCCESS: Status set to PENDING");
} catch (Exception e) {
System.err.println("ERROR setting status: " + e.getMessage());
throw e;
}

issue.setCreatedAt(LocalDateTime.now());
System.out.println("DEBUG: Set createdAt to: " + issue.getCreatedAt());

// Set coordinates if provided
if (latitude != null && longitude != null) {
System.out.println("DEBUG: Setting coordinates - Lat: " + latitude + ", Lng: " + longitude);
issue.setLatitude(latitude);
issue.setLongitude(longitude);
} else {
System.out.println("DEBUG: No coordinates provided");
}

// Handle image if provided
if (image != null && !image.isEmpty()) {
System.out.println("DEBUG: Processing image upload");
System.out.println("Image original filename: " + image.getOriginalFilename());
System.out.println("Image size: " + image.getSize() + " bytes");
System.out.println("Image content type: " + image.getContentType());

try {
String imageUrl = saveImage(image);
issue.setImageUrl(imageUrl);
System.out.println("SUCCESS: Image saved with URL: " + imageUrl);
} catch (Exception e) {
System.err.println("ERROR saving image: " + e.getMessage());
e.printStackTrace();
throw new RuntimeException("Failed to save image: " + e.getMessage());
}
} else {
System.out.println("DEBUG: No image provided");
}

System.out.println("DEBUG: About to save issue to database");
System.out.println("Issue details before save:");
System.out.println("- Title: " + issue.getTitle());
System.out.println("- Description: " + issue.getDescription());
System.out.println("- Location: " + issue.getLocation());
System.out.println("- Status: " + issue.getStatus());
System.out.println("- User ID: " + issue.getReportedBy().getId());
System.out.println("- Category ID: " + issue.getCategory().getId());
System.out.println("- Created At: " + issue.getCreatedAt());
System.out.println("- Image URL: " + issue.getImageUrl());
System.out.println("- Latitude: " + issue.getLatitude());
System.out.println("- Longitude: " + issue.getLongitude());

Issue savedIssue = issueRepository.save(issue);
System.out.println("SUCCESS: Issue saved with ID: " + savedIssue.getId());
System.out.println("=== END Service Debug ===");

return savedIssue;

} catch (Exception e) {
System.err.println("ERROR in createIssueWithImage: " + e.getMessage());
System.err.println("Error type: " + e.getClass().getSimpleName());
e.printStackTrace();
throw new RuntimeException("Failed to create issue: " + e.getMessage(), e);
}
}

    // Helper method to find user by ID (numeric) or username (string)
    private User findUserByIdOrUsername(String identifier) {
        System.out.println("DEBUG: findUserByIdOrUsername called with: " + identifier);
        
        // Try to find by username first
        System.out.println("DEBUG: Searching by username: " + identifier);
        Optional<User> userByUsername = userRepository.findByUsername(identifier);
        if (userByUsername.isPresent()) {
            User user = userByUsername.get();
            System.out.println("SUCCESS: Found user by username - ID: " + user.getId() + ", Username: " + user.getUsername());
            return user;
        }
        System.out.println("DEBUG: User not found by username");
        
        // If not found by username, try parsing as ID
        try {
            Long id = Long.parseLong(identifier);
            System.out.println("DEBUG: Searching by ID: " + id);
            Optional<User> userById = userRepository.findById(id);
            if (userById.isPresent()) {
                User user = userById.get();
                System.out.println("SUCCESS: Found user by ID - ID: " + user.getId() + ", Username: " + user.getUsername());
                return user;
            }
            System.out.println("DEBUG: User not found by ID");
        } catch (NumberFormatException e) {
            System.out.println("DEBUG: Identifier is not a valid number: " + identifier);
        }
        
        System.err.println("ERROR: User not found with identifier: " + identifier);
        return null;
    }

    // Helper method to save image (implement based on your storage strategy)
    private String saveImage(MultipartFile image) {
        try {
            // Simple implementation - save to uploads directory
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            String uploadDir = "uploads/images/";
            
            // Create directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Save file
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            return uploadDir + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    public Optional<Issue> getIssueById(Long id) {
        return issueRepository.findById(id);
    }


    public List<Issue> getIssuesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return issueRepository.findByReportedBy(user);
    }

    public List<Issue> getIssuesByStatus(IssueStatus status) {
        return issueRepository.findByStatus(status);
    }


    public Issue updateIssueStatus(Long id, IssueStatus status) {
        Issue issue = issueRepository.findById(id).orElseThrow();
        issue.setStatus(status);
        return issueRepository.save(issue);
    }
    public List<Issue> getByStatus(IssueStatus status) {
        return issueRepository.findByStatus(status);
    }
 // Add this method to your IssueService.java

    public List<Issue> getIssuesByUsername(String username) {
        System.out.println("DEBUG: Getting issues for username: " + username);
        
        // Find user by username
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                System.err.println("ERROR: User not found with username: " + username);
                return new RuntimeException("User not found with username: " + username);
            });
        
        System.out.println("SUCCESS: Found user - ID: " + user.getId() + ", Username: " + user.getUsername());
        
        // Get issues for this user
        List<Issue> issues = issueRepository.findByReportedBy(user);
        System.out.println("SUCCESS: Found " + issues.size() + " issues for user: " + username);
        
        return issues;
    }
}