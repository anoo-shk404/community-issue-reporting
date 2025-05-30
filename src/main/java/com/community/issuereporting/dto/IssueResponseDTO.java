package com.community.issuereporting.dto;

import com.community.issuereporting.entity.IssueStatus;
import java.time.LocalDateTime;

public class IssueResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String location;
    private Double latitude;
    private Double longitude;
    private IssueStatus status;
    private LocalDateTime createdAt;
    private UserBasicDTO reportedBy;
    private CategoryBasicDTO category;

    // Constructors
    public IssueResponseDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public IssueStatus getStatus() { return status; }
    public void setStatus(IssueStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public UserBasicDTO getReportedBy() { return reportedBy; }
    public void setReportedBy(UserBasicDTO reportedBy) { this.reportedBy = reportedBy; }

    public CategoryBasicDTO getCategory() { return category; }
    public void setCategory(CategoryBasicDTO category) { this.category = category; }
}