package com.community.issuereporting.util;

import com.community.issuereporting.dto.CategoryBasicDTO;
import com.community.issuereporting.dto.IssueResponseDTO;
import com.community.issuereporting.dto.UserBasicDTO;
import com.community.issuereporting.entity.Issue;
import org.springframework.stereotype.Component;

@Component
public class IssueMapper {
    
    public IssueResponseDTO toResponseDTO(Issue issue) {
        if (issue == null) {
            return null;
        }
        
        IssueResponseDTO dto = new IssueResponseDTO();
        dto.setId(issue.getId());
        dto.setTitle(issue.getTitle());
        dto.setDescription(issue.getDescription());
        dto.setImageUrl(issue.getImageUrl());
        dto.setLocation(issue.getLocation());
        dto.setLatitude(issue.getLatitude());
        dto.setLongitude(issue.getLongitude());
        dto.setStatus(issue.getStatus());
        dto.setCreatedAt(issue.getCreatedAt());
        
        // Safely convert User to UserBasicDTO
        if (issue.getReportedBy() != null) {
            UserBasicDTO userDTO = new UserBasicDTO(
                issue.getReportedBy().getId(),
                issue.getReportedBy().getName(),
                issue.getReportedBy().getEmail(),
                issue.getReportedBy().getUsername(),
                issue.getReportedBy().getRole()
            );
            dto.setReportedBy(userDTO);
        }
        
        // Safely convert Category to CategoryBasicDTO
        if (issue.getCategory() != null) {
            CategoryBasicDTO categoryDTO = new CategoryBasicDTO(
                issue.getCategory().getId(),
                issue.getCategory().getName()
            );
            dto.setCategory(categoryDTO);
        }
        
        return dto;
    }
}