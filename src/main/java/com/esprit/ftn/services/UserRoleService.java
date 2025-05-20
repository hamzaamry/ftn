package com.esprit.ftn.services;
import com.esprit.ftn.dto.UserRoleAssignmentRequest;
import com.esprit.ftn.entities.*;
import com.esprit.ftn.Repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRepository userRepository;
    private final CoachRepository coachRepository;
    private final AthleteRepository athleteRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public void assignRole(UserRoleAssignmentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        switch (request.getRole().toUpperCase()) {
            case "COACH":
                if (coachRepository.existsByUser(user))
                    throw new RuntimeException("Cet utilisateur est déjà Coach");
                Coach coach = new Coach();
                coach.setUser(user);
                coachRepository.save(coach);
                user.setType(User.UserType.COACH);
                break;

            case "ATHLETE":
                if (athleteRepository.existsByUser(user))
                    throw new RuntimeException("Cet utilisateur est déjà Athlète");
                Athlete athlete = new Athlete();
                athlete.setUser(user);
                athleteRepository.save(athlete);
                user.setType(User.UserType.ATHLETE);
                break;

            default:
                throw new RuntimeException("Rôle non reconnu");
        }

        userRepository.save(user);
    }

    public List<User> getAllUsersWithRoles() {
        return userRepository.findAll(); // Tu peux enrichir avec projection DTO si besoin
    }

    public Admin createAdmin(Long userId, boolean superAdmin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (adminRepository.existsByUser(user))
            throw new RuntimeException("Déjà admin");

        Admin admin = new Admin();
        admin.setUser(user);
        admin.setSuperAdmin(superAdmin);
        user.setType(User.UserType.ADMIN);

        userRepository.save(user);
        return adminRepository.save(admin);
    }

    public void deleteUserById(Long userId) {
        // Avant de supprimer le user, supprimer ses rôles liés (Admin, Coach, Athlete) si ils existent
        adminRepository.findAll().stream()
                .filter(admin -> admin.getUser().getId().equals(userId))
                .forEach(admin -> adminRepository.delete(admin));

        coachRepository.findAll().stream()
                .filter(coach -> coach.getUser().getId().equals(userId))
                .forEach(coach -> coachRepository.delete(coach));

        athleteRepository.findAll().stream()
                .filter(athlete -> athlete.getUser().getId().equals(userId))
                .forEach(athlete -> athleteRepository.delete(athlete));

        // Puis supprimer le user
        userRepository.deleteById(userId);
    }

}
