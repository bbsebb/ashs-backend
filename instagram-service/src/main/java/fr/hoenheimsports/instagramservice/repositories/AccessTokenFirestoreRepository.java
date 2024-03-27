package fr.hoenheimsports.instagramservice.repositories;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import fr.hoenheimsports.instagramservice.models.AccessToken;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Repository
@Primary
public class AccessTokenFirestoreRepository implements AccessTokenRepository{

    private final Firestore db ;

    public AccessTokenFirestoreRepository(Firestore db) {
        this.db = db;
    }

    @Override
    public void save(AccessToken accessToken) {
        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", accessToken.getAccessToken());
        data.put("createdAt", toTimestamp(accessToken.getCreatedAt()));
        data.put("expiresAt", toTimestamp(accessToken.getExpiresAt()));
        data.put("tokenType", accessToken.getTokenType());

        db.collection("accessTokens").document("singletonToken").set(data);
    }
    @Override
    public AccessToken get() {
        try {
            DocumentSnapshot document = db.collection("accessTokens").document("singletonToken").get().get();
            if (document.exists()) {
                return AccessToken.builder()
                        .accessToken(document.getString("accessToken"))
                        .createdAt(fromTimestamp(Objects.requireNonNull(document.getTimestamp("createdAt"))))
                        .expiresAt(fromTimestamp(Objects.requireNonNull(document.getTimestamp("expiresAt"))))
                        .tokenType(document.getString("tokenType"))
                        .build();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return null; // Handle case where document does not exist
    }

    public static Timestamp toTimestamp(LocalDateTime localDateTime) {
        return Timestamp.ofTimeSecondsAndNanos(
                localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond(),
                localDateTime.getNano()
        );
    }

    public static LocalDateTime fromTimestamp(Timestamp timestamp) {
        return LocalDateTime.ofInstant(timestamp.toDate().toInstant(), ZoneId.systemDefault());
    }
}
