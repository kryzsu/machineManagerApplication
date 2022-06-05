package hu.mycompany.machinemanager.repository;

import hu.mycompany.machinemanager.domain.OutOfOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the OutOfOrder entity.
 */
@Repository
public interface OutOfOrderBimRepository extends OutOfOrderRepository {
    @Query(
        "select outOfOrder from OutOfOrder outOfOrder join fetch outOfOrder.machines machine" +
            " where machine.id =:id and outOfOrder.start > :date order by outOfOrder.start"
    )
    List<OutOfOrder> findAllByMachineIdAndStartGreaterThanEqual(@Param("id") Long MachineId, @Param("date") LocalDate date);

}
