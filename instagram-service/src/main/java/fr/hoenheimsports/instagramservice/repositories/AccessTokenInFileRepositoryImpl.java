package fr.hoenheimsports.instagramservice.repositories;

import fr.hoenheimsports.instagramservice.models.AccessToken;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class AccessTokenInFileRepositoryImpl implements AccessTokenRepository {
    private final Path filePath = Paths.get("token.ser");

    public void save(AccessToken token) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(token);
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public AccessToken get() {
        if (!Files.exists(filePath)) {
            return null; // Ou gérer autrement si le fichier n'existe pas
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (AccessToken) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            return null;
        }
    }
}
