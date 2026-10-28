package com.portfolio.transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface TransferRepository extends JpaRepository<TransferRecord,Long>{
 Optional<TransferRecord> findByIdempotencyKey(String key);
}
