package hu.mycompany.machinemanager.repository;

import hu.mycompany.machinemanager.domain.OutOfOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the OutOfOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OutOfOrderRepository extends JpaRepository<OutOfOrder, Long>, JpaSpecificationExecutor<OutOfOrder> {}
