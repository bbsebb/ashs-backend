package fr.hoenheimsports.trainingservice.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityServiceImpl implements UserSecurityService {
    /**
     * Vérifie si l'utilisateur est connecté.
     *
     * @return true si l'utilisateur est connecté, false sinon.
     */
    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String &&
                        "anonymousUser".equals(authentication.getPrincipal()));
    }

    /**
     * Vérifie si l'utilisateur connecté possède un rôle spécifique.
     * Le préfixe "ROLE_" est automatiquement ajouté à la chaîne de rôle.
     *
     * @param role le rôle à vérifier (ex. "ADMIN").
     * @return true si l'utilisateur possède le rôle, false sinon.
     */
    @Override
    public boolean hasRole(String role) {
        role = "ROLE_".concat(role);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role::equals);
        }
        return false;
    }
}
