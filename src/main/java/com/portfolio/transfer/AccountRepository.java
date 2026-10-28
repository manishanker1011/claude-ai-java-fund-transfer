package com.portfolio.transfer;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
public interface AccountRepository extends JpaRepository<Account,Long>{
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("select a from Account a where a.id = :id")
 Optional<Account> lockById(@Param("id") Long id);
}
