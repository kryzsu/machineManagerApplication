package hu.mycompany.machinemanager.repository;

import hu.mycompany.machinemanager.domain.Rawmaterial;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Rawmaterial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RawmaterialRepository extends JpaRepository<Rawmaterial, Long> {}
