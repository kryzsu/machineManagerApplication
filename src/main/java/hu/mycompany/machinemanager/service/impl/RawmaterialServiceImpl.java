package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.domain.Rawmaterial;
import hu.mycompany.machinemanager.repository.RawmaterialRepository;
import hu.mycompany.machinemanager.service.RawmaterialService;
import hu.mycompany.machinemanager.service.dto.RawmaterialDTO;
import hu.mycompany.machinemanager.service.mapper.RawmaterialMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Rawmaterial}.
 */
@Service
@Transactional
public class RawmaterialServiceImpl implements RawmaterialService {

    private final Logger log = LoggerFactory.getLogger(RawmaterialServiceImpl.class);

    private final RawmaterialRepository rawmaterialRepository;

    private final RawmaterialMapper rawmaterialMapper;

    public RawmaterialServiceImpl(RawmaterialRepository rawmaterialRepository, RawmaterialMapper rawmaterialMapper) {
        this.rawmaterialRepository = rawmaterialRepository;
        this.rawmaterialMapper = rawmaterialMapper;
    }

    @Override
    public RawmaterialDTO save(RawmaterialDTO rawmaterialDTO) {
        log.debug("Request to save Rawmaterial : {}", rawmaterialDTO);
        Rawmaterial rawmaterial = rawmaterialMapper.toEntity(rawmaterialDTO);
        rawmaterial = rawmaterialRepository.save(rawmaterial);
        return rawmaterialMapper.toDto(rawmaterial);
    }

    @Override
    public Optional<RawmaterialDTO> partialUpdate(RawmaterialDTO rawmaterialDTO) {
        log.debug("Request to partially update Rawmaterial : {}", rawmaterialDTO);

        return rawmaterialRepository
            .findById(rawmaterialDTO.getId())
            .map(
                existingRawmaterial -> {
                    rawmaterialMapper.partialUpdate(existingRawmaterial, rawmaterialDTO);

                    return existingRawmaterial;
                }
            )
            .map(rawmaterialRepository::save)
            .map(rawmaterialMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RawmaterialDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rawmaterials");
        return rawmaterialRepository.findAll(pageable).map(rawmaterialMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RawmaterialDTO> findOne(Long id) {
        log.debug("Request to get Rawmaterial : {}", id);
        return rawmaterialRepository.findById(id).map(rawmaterialMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rawmaterial : {}", id);
        rawmaterialRepository.deleteById(id);
    }
}
