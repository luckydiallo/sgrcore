package bf.ibam.sgrcore.repository;

import bf.ibam.sgrcore.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
