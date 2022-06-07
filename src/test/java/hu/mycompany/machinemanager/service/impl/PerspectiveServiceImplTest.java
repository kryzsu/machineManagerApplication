package hu.mycompany.machinemanager.service.impl;

import hu.mycompany.machinemanager.domain.Job;
import hu.mycompany.machinemanager.domain.OutOfOrder;
import hu.mycompany.machinemanager.domain.Product;
import hu.mycompany.machinemanager.repository.JobRepository;
import hu.mycompany.machinemanager.repository.OutOfOrderRepository;
import hu.mycompany.machinemanager.service.PerspectiveService;
import hu.mycompany.machinemanager.service.dto.MachineDayDTO;
import hu.mycompany.machinemanager.service.mapper.MachineMapper;
import hu.mycompany.machinemanager.service.mapper.OutOfOrderMapper;
import hu.mycompany.machinemanager.service.mapper.OutOfOrderMapperImpl;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerspectiveServiceImplTest {

    @Mock
    OutOfOrderRepository outOfOrderRepository;

    JobRepository jobRepository;

    PerspectiveServiceImpl perspectiveService;

    @BeforeEach
    void setUp() {
        jobRepository = mock(JobRepository.class);
//        perspectiveService = new PerspectiveServiceImpl(null, null, new OutOfOrderMapperImpl(),
//            jobRepository, outOfOrderRepository);
    }

    @Test
    @Ignore
    void getJobNextDays_noJob_noOutOfService() {
//        // GIVEN
//        long days = 4;
//        long machineId = 1;
//        //when(jobRepository.findFirstByMachineIdAndEndDateIsNullAndStartDateIsNotNullOrderByPriorityDesc(any()))
//          //  .thenReturn(Optional.empty());
//        // WHEN
//        List<MachineDayDTO> jobNextDays = perspectiveService.getJobNextDays(machineId, days);
//
//        // THEN
//        assertThat(jobNextDays, hasSize(4));
//        assertThat(jobNextDays, everyItem(Matchers.<MachineDayDTO>hasProperty("comment", equalTo("free"))));
    }

    @Test
    @Ignore
    void getJobNextDays_oneJobSmaller_noOutOfService() {
        // GIVEN
//        long days = 4;
//        long machineId = 1;
//        //when(jobRepository.findFirstByMachineIdAndEndDateIsNullAndStartDateIsNotNullOrderByPriorityDesc(any()))
//        //  .thenReturn(Optional.empty());
//
//
////        when(jobRepository.findByMachineIdAndStartDateIsNullOrderByPriorityDescCreateDateTimeDesc(any()))
////          .thenReturn(Stream.of(new Job()
////              .estimation(2)
////              .productCount(100).id(123L)
////              .worknumber("job in")
////              .addProduct(new Product().name("productName"))).collect(Collectors.toList()));
//
//        // WHEN
//        List<MachineDayDTO> jobNextDays = perspectiveService.getJobNextDays(machineId, days);
//
//        // THEN
//        assertThat(jobNextDays, hasSize(4));
//        assertThat(jobNextDays, contains(
//            Matchers.<MachineDayDTO>hasProperty("comment", equalTo("job in")),
//            Matchers.<MachineDayDTO>hasProperty("comment", equalTo("job in")),
//            Matchers.<MachineDayDTO>hasProperty("comment", equalTo("free")),
//            Matchers.<MachineDayDTO>hasProperty("comment", equalTo("free")))
//        );
    }

}
