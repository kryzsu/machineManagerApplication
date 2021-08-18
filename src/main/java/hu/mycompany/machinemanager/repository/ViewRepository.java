package hu.mycompany.machinemanager.repository;

import hu.mycompany.machinemanager.domain.View;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the View entity.
 */
@Repository
public interface ViewRepository extends JpaRepository<View, Long>, JpaSpecificationExecutor<View> {
    @Query(
        value = "select distinct view from View view left join fetch view.machines",
        countQuery = "select count(distinct view) from View view"
    )
    Page<View> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct view from View view left join fetch view.machines")
    List<View> findAllWithEagerRelationships();

    @Query("select view from View view left join fetch view.machines where view.id =:id")
    Optional<View> findOneWithEagerRelationships(@Param("id") Long id);
}
