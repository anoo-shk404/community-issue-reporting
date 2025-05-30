package com.community.issuereporting.controller;

import com.community.issuereporting.dto.IssueResponseDTO;
import com.community.issuereporting.entity.Issue;
import com.community.issuereporting.entity.IssueStatus;
import com.community.issuereporting.service.IssueService;
import com.community.issuereporting.util.IssueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*")
public class IssueController {
    
    @Autowired
    private IssueService issueService;
    
    @Autowired
    private IssueMapper issueMapper;

    // Get all issues - returns DTOs
    @GetMapping
    public ResponseEntity<List<IssueResponseDTO>> getAllIssues() {
        try {
            List<Issue> issues = issueService.getAllIssues();
            List<IssueResponseDTO> responseDTOs = issues.stream()
                .map(issueMapper::toResponseDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get issue by ID - returns DTO
    @GetMapping("/{id}")
    public ResponseEntity<IssueResponseDTO> getIssueById(@PathVariable Long id) {
        try {
            Issue issue = issueService.getIssueById(id)
                .orElseThrow(() -> new RuntimeException("Issue not found with id: " + id));
            
            IssueResponseDTO responseDTO = issueMapper.toResponseDTO(issue);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    // Regular JSON POST endpoint - returns DTO
    @PostMapping
    public ResponseEntity<IssueResponseDTO> createIssue(@RequestBody Issue issue) {
        try {
            Issue createdIssue = issueService.createIssue(issue);
            IssueResponseDTO responseDTO = issueMapper.toResponseDTO(createdIssue);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // Create issue with image - returns DTO
    @PostMapping("/with-image")
    public ResponseEntity<IssueResponseDTO> createIssueWithImage(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("userId") String userId,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude) {
        
        System.out.println("=== DEBUG: Controller Method Called ===");
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Location: " + location);
        System.out.println("UserId: " + userId);
        System.out.println("CategoryId: " + categoryId);
        System.out.println("Image present: " + (image != null && !image.isEmpty()));
        if (image != null) {
            System.out.println("Image name: " + image.getOriginalFilename());
            System.out.println("Image size: " + image.getSize());
            System.out.println("Image content type: " + image.getContentType());
        }
        System.out.println("Latitude: " + latitude);
        System.out.println("Longitude: " + longitude);
        System.out.println("=== END Controller Debug ===");
        
        try {
            Issue createdIssue = issueService.createIssueWithImage(
                title, description, location, userId, categoryId, image, latitude, longitude
            );
            
            System.out.println("SUCCESS: Issue created with ID: " + createdIssue.getId());
            
            IssueResponseDTO responseDTO = issueMapper.toResponseDTO(createdIssue);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            System.err.println("ERROR in controller: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
 // Add these methods to your IssueController.java

 // Get issues by user ID/username
 @GetMapping("/user/{userId}")
 public ResponseEntity<List<IssueResponseDTO>> getIssuesByUser(@PathVariable String userId) {
     try {
         System.out.println("DEBUG: Getting issues for user: " + userId);
         
         // Try to parse as Long first, then fall back to username
         List<Issue> issues;
         try {
             Long userIdLong = Long.parseLong(userId);
             issues = issueService.getIssuesByUser(userIdLong);
         } catch (NumberFormatException e) {
             // If not a number, treat as username and get user ID first
             // You'll need to add this method to your service
             issues = issueService.getIssuesByUsername(userId);
         }
         
         List<IssueResponseDTO> responseDTOs = issues.stream()
             .map(issueMapper::toResponseDTO)
             .collect(Collectors.toList());
             
         System.out.println("SUCCESS: Found " + responseDTOs.size() + " issues for user: " + userId);
         return ResponseEntity.ok(responseDTOs);
     } catch (Exception e) {
         System.err.println("ERROR getting issues for user " + userId + ": " + e.getMessage());
         e.printStackTrace();
         return ResponseEntity.notFound().build();
     }
 }

 // Get issues by status
 @GetMapping("/status/{status}")
 public ResponseEntity<List<IssueResponseDTO>> getIssuesByStatus(@PathVariable String status) {
     try {
         System.out.println("DEBUG: Getting issues with status: " + status);
         
         // Convert string to enum
         IssueStatus issueStatus;
         try {
             issueStatus = IssueStatus.valueOf(status.toUpperCase());
         } catch (IllegalArgumentException e) {
             throw new RuntimeException("Invalid status: " + status);
         }
         
         List<Issue> issues = issueService.getIssuesByStatus(issueStatus);
         List<IssueResponseDTO> responseDTOs = issues.stream()
             .map(issueMapper::toResponseDTO)
             .collect(Collectors.toList());
             
         System.out.println("SUCCESS: Found " + responseDTOs.size() + " issues with status: " + status);
         return ResponseEntity.ok(responseDTOs);
     } catch (Exception e) {
         System.err.println("ERROR getting issues by status " + status + ": " + e.getMessage());
         e.printStackTrace();
         return ResponseEntity.internalServerError().build();
     }
 }

 // Update issue status
 @PutMapping("/{id}/status")
 public ResponseEntity<IssueResponseDTO> updateIssueStatus(
         @PathVariable Long id, 
         @RequestBody Map<String, String> statusUpdate) {
     try {
         System.out.println("DEBUG: Updating status for issue ID: " + id);
         String statusString = statusUpdate.get("status");
         
         if (statusString == null) {
             throw new RuntimeException("Status is required");
         }
         
         // Convert string to enum
         IssueStatus newStatus;
         try {
             newStatus = IssueStatus.valueOf(statusString.toUpperCase());
         } catch (IllegalArgumentException e) {
             throw new RuntimeException("Invalid status: " + statusString);
         }
         
         Issue updatedIssue = issueService.updateIssueStatus(id, newStatus);
         IssueResponseDTO responseDTO = issueMapper.toResponseDTO(updatedIssue);
         
         System.out.println("SUCCESS: Updated issue " + id + " status to: " + newStatus);
         return ResponseEntity.ok(responseDTO);
     } catch (Exception e) {
         System.err.println("ERROR updating issue status: " + e.getMessage());
         e.printStackTrace();
         return ResponseEntity.internalServerError().build();
     }
 }
}