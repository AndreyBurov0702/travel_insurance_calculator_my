package org.javaguru.travel.insurance.core.underwriting.calculators.medical;

import org.javaguru.travel.insurance.core.domain.AgeCoefficient;
import org.javaguru.travel.insurance.core.repositories.AgeCoefficientRepository;
import org.javaguru.travel.insurance.core.util.DateTimeUtil;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumRequestV1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgeCoefficientCalculatorTest {
    @Mock
    private DateTimeUtil dateTimeUtil;
    @Mock
    private AgeCoefficientRepository ageCoefficientRepository;
    @Mock
    private AgeCoefficient ageCoefficient;

    @InjectMocks
    private AgeCoefficientCalculator ageCoefficientCalculator;
    private TravelCalculatePremiumRequestV1 request;

    @BeforeEach
    void setUp(){
        request = new TravelCalculatePremiumRequestV1();
        request.setPersonBirthDate(Date.from(LocalDate.of(1990, 1, 1)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
    @Test
    void shouldReturnOneWhenDisabled() {
        var calculator = new AgeCoefficientCalculator(false, dateTimeUtil, ageCoefficientRepository);
        BigDecimal result = calculator.calculate(request);
        assertEquals(BigDecimal.ONE, result);
    }
    @Test
    void shouldFindCoefficientWhenAgeCoefficientExists() {
        var calculator = new AgeCoefficientCalculator(true, dateTimeUtil, ageCoefficientRepository);
        LocalDate currentDate = LocalDate.of(2023, 3, 27);
        int age = 33;
        BigDecimal expectedCoefficient = BigDecimal.valueOf(1.2);
        when(dateTimeUtil.getCurrentDateTime()).thenReturn(Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(ageCoefficient.getCoefficient()).thenReturn(expectedCoefficient);
        when(ageCoefficientRepository.findCoefficient(age)).thenReturn(Optional.of(ageCoefficient));
        BigDecimal result = calculator.calculate(request);
        assertEquals(expectedCoefficient, result);
    }

    @Test
    void shouldThrowExceptionWhenAgeCoefficientNotFound() {
        var calculator = new AgeCoefficientCalculator(true, dateTimeUtil, ageCoefficientRepository);
        LocalDate currentDate = LocalDate.of(2023, 3, 27);
        int age = 33;
        when(dateTimeUtil.getCurrentDateTime()).thenReturn(Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(ageCoefficientRepository.findCoefficient(age)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> calculator.calculate(request));
        assertEquals("Age coefficient not found for age = " + age, exception.getMessage());
    }
}
