package com.esprit.ftn.Controllers;
import com.esprit.ftn.dto.UserRoleAssignmentRequest;
import com.esprit.ftn.entities.*;
import com.esprit.ftn.Repositories.*;
import com.esprit.ftn.services.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class Admincontroller {
    private final UserRoleService userRoleService;
    private final AdminRepository adminRepository;

    // ✅ Affecter un rôle à un utilisateur
    @PostMapping("/assign-role")
    public String assignRoleToUser(@RequestBody UserRoleAssignmentRequest request) {
        userRoleService.assignRole(request);
        return "Rôle attribué avec succès";
    }

    // ✅ Lister tous les utilisateurs avec leur rôle
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRoleService.getAllUsersWithRoles();
    }



    // Supprimer un user par son id
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRoleService.deleteUserById(id);
    }

}
