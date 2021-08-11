package hu.mycompany.machinemanager.service;

import hu.mycompany.machinemanager.domain.*; // for static metamodels
import hu.mycompany.machinemanager.domain.OutOfOrder;
import hu.mycompany.machinemanager.repository.OutOfOrderRepository;
import hu.mycompany.machinemanager.service.dto.OutOfOrderCriteria;
import hu.mycompany.machinemanager.service.dto.OutOfOrderDTO;
import hu.mycompany.machinemanager.service.mapper.OutOfOrderMapper;
import io.github.jhipster.service.QueryService;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link OutOfOrder} entities in the database.
 * The main input is a {@link OutOfOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OutOfOrderDTO} or a {@link Page} of {@link OutOfOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OutOfOrderQueryService extends QueryService<OutOfOrder> {
    private final Logger log = LoggerFactory.getLogger(OutOfOrderQueryService.class);

    private final OutOfOrderRepository outOfOrderRepository;

    private final OutOfOrderMapper outOfOrderMapper;

    public OutOfOrderQueryService(OutOfOrderRepository outOfOrderRepository, OutOfOrderMapper outOfOrderMapper) {
        this.outOfOrderRepository = outOfOrderRepository;
        this.outOfOrderMapper = outOfOrderMapper;
    }

    /**
     * Return a {@link List} of {@link OutOfOrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OutOfOrderDTO> findByCriteria(OutOfOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OutOfOrder> specification = createSpecification(criteria);
        return outOfOrderMapper.toDto(outOfOrderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OutOfOrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OutOfOrderDTO> findByCriteria(OutOfOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OutOfOrder> specification = createSpecification(criteria);
        return outOfOrderRepository.findAll(specification, page).map(outOfOrderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OutOfOrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OutOfOrder> specification = createSpecification(criteria);
        return outOfOrderRepository.count(specification);
    }

    /**
     * Function to convert {@link OutOfOrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OutOfOrder> createSpecification(OutOfOrderCriteria criteria) {
        Specification<OutOfOrder> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OutOfOrder_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), OutOfOrder_.date));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), OutOfOrder_.description));
            }
        }
        return specification;
    }
}
