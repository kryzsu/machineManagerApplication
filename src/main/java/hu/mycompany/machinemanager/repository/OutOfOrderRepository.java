package hu.mycompany.machinemanager.repository;

import hu.mycompany.machinemanager.domain.OutOfOrder;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OutOfOrder entity.
 */
@Repository
public interface OutOfOrderRepository extends JpaRepository<OutOfOrder, Long>, JpaSpecificationExecutor<OutOfOrder> {
    @Query(
        value = "select distinct outOfOrder from OutOfOrder outOfOrder left join fetch outOfOrder.machines",
        countQuery = "select count(distinct outOfOrder) from OutOfOrder outOfOrder"
    )
    Page<OutOfOrder> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct outOfOrder from OutOfOrder outOfOrder left join fetch outOfOrder.machines")
    List<OutOfOrder> findAllWithEagerRelationships();

    @Query("select outOfOrder from OutOfOrder outOfOrder left join fetch outOfOrder.machines where outOfOrder.id =:id")
    Optional<OutOfOrder> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(
        "select outOfOrder from OutOfOrder outOfOrder join fetch outOfOrder.machines machine" +
        " where machine.id =:id and outOfOrder.start > :date"
    )
    List<OutOfOrder> findAllByMachineIdAndStartGreaterThanEqual(@Param("id") Long MachineId, @Param("date") LocalDate date);
}
