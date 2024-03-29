package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.domain.View;
import hu.mycompany.machinemanager.repository.ViewRepository;
import hu.mycompany.machinemanager.service.ViewService;
import hu.mycompany.machinemanager.service.dto.ViewDTO;
import hu.mycompany.machinemanager.service.mapper.ViewMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link View}.
 */
@Service
@Transactional
public class ViewServiceImpl implements ViewService {

    private final Logger log = LoggerFactory.getLogger(ViewServiceImpl.class);

    private final ViewRepository viewRepository;

    private final ViewMapper viewMapper;

    public ViewServiceImpl(ViewRepository viewRepository, ViewMapper viewMapper) {
        this.viewRepository = viewRepository;
        this.viewMapper = viewMapper;
    }

    @Override
    public ViewDTO save(ViewDTO viewDTO) {
        log.debug("Request to save View : {}", viewDTO);
        View view = viewMapper.toEntity(viewDTO);
        view = viewRepository.save(view);
        return viewMapper.toDto(view);
    }

    @Override
    public Optional<ViewDTO> partialUpdate(ViewDTO viewDTO) {
        log.debug("Request to partially update View : {}", viewDTO);

        return viewRepository
            .findById(viewDTO.getId())
            .map(
                existingView -> {
                    viewMapper.partialUpdate(existingView, viewDTO);

                    return existingView;
                }
            )
            .map(viewRepository::save)
            .map(viewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ViewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Views");
        return viewRepository.findAll(pageable).map(viewMapper::toDto);
    }

    public Page<ViewDTO> findAllWithEagerRelationships(Pageable pageable) {
        return viewRepository.findAllWithEagerRelationships(pageable).map(viewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ViewDTO> findOne(Long id) {
        log.debug("Request to get View : {}", id);
        return viewRepository.findOneWithEagerRelationships(id).map(viewMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete View : {}", id);
        viewRepository.deleteById(id);
    }
}
