package fr.hoenheimsports.instagramservice.repositories;


import fr.hoenheimsports.instagramservice.models.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, String>{
}
