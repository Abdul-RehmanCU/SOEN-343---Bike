package com.qwikride.prc.controller;

import com.qwikride.model.User;
import com.qwikride.prc.dto.DisputeResolutionRequest;
import com.qwikride.prc.dto.DisputeTicketRequest;
import com.qwikride.prc.dto.DisputeTicketResponse;
import com.qwikride.prc.service.DisputeService;
import com.qwikride.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prc/disputes")
@RequiredArgsConstructor
public class DisputeController {
    private final DisputeService disputeService;
    private final UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasAuthority('RIDER')")
    public ResponseEntity<DisputeTicketResponse> submit(@RequestBody DisputeTicketRequest request) {
        Long riderId = currentRiderId();
        return ResponseEntity.ok(disputeService.submit(riderId, request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('RIDER')")
    public ResponseEntity<List<DisputeTicketResponse>> listForRider() {
        return ResponseEntity.ok(disputeService.listForRider(currentRiderId()));
    }

    @GetMapping("/open")
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<List<DisputeTicketResponse>> listOpen() {
        return ResponseEntity.ok(disputeService.listOpenTickets());
    }

    @PostMapping("/{ticketId}/resolve")
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<DisputeTicketResponse> resolve(@PathVariable Long ticketId,
            @RequestBody DisputeResolutionRequest request) {
        return ResponseEntity.ok(disputeService.resolve(ticketId, request));
    }

    private Long currentRiderId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new org.springframework.security.access.AccessDeniedException("Unauthenticated");
        }
        return userRepository.findByUsername(authentication.getName())
                .map(User::getId)
                .orElseThrow(() -> new org.springframework.security.access.AccessDeniedException("User not found"));
    }
}
