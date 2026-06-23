package com.re.controller.admin;

import com.re.dto.UserDetailsDto;
import com.re.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDetailsDto>> getUserList() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable Long userId,
                                                   @PathVariable Long roleId) {
        userService.removeRole(userId, roleId);
        return ResponseEntity.noContent().build();
    }
}
