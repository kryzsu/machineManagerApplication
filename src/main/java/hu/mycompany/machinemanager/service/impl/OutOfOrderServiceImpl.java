package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.domain.OutOfOrder;
import hu.mycompany.machinemanager.repository.OutOfOrderRepository;
import hu.mycompany.machinemanager.service.OutOfOrderService;
import hu.mycompany.machinemanager.service.dto.OutOfOrderDTO;
import hu.mycompany.machinemanager.service.mapper.OutOfOrderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OutOfOrder}.
 */
@Service
@Transactional
public class OutOfOrderServiceImpl implements OutOfOrderService {
    private final Logger log = LoggerFactory.getLogger(OutOfOrderServiceImpl.class);

    private final OutOfOrderRepository outOfOrderRepository;

    private final OutOfOrderMapper outOfOrderMapper;

    public OutOfOrderServiceImpl(OutOfOrderRepository outOfOrderRepository, OutOfOrderMapper outOfOrderMapper) {
        this.outOfOrderRepository = outOfOrderRepository;
        this.outOfOrderMapper = outOfOrderMapper;
    }

    @Override
    public OutOfOrderDTO save(OutOfOrderDTO outOfOrderDTO) {
        log.debug("Request to save OutOfOrder : {}", outOfOrderDTO);
        OutOfOrder outOfOrder = outOfOrderMapper.toEntity(outOfOrderDTO);
        outOfOrder = outOfOrderRepository.save(outOfOrder);
        return outOfOrderMapper.toDto(outOfOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutOfOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OutOfOrders");
        return outOfOrderRepository.findAll(pageable).map(outOfOrderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OutOfOrderDTO> findOne(Long id) {
        log.debug("Request to get OutOfOrder : {}", id);
        return outOfOrderRepository.findById(id).map(outOfOrderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OutOfOrder : {}", id);
        outOfOrderRepository.deleteById(id);
    }
}
