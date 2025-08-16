package com.somo.pms.services;

import com.somo.pms.dto.request.IssueRequest;
import com.somo.pms.dto.response.IssueResponse;
import com.somo.pms.models.Issue;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface IssueService{
    IssueResponse getIssueById( String id) throws BadRequestException;
    List<IssueResponse> getIssuesByProjectId(String projectId) throws BadRequestException;
    IssueResponse createIssue(IssueRequest request,String userId) throws BadRequestException;
    String deleteIssue(String id) throws BadRequestException;
    IssueResponse addUserToIssue(String issueId, String userId) throws BadRequestException;
    IssueResponse updateStatus(String issueId, String status) throws BadRequestException;
}
