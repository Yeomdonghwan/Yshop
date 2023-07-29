package yeom.yshop.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import yeom.yshop.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByAccountEmail(String email);

    void deleteRefreshTokenByAccountEmail(String email);
}
