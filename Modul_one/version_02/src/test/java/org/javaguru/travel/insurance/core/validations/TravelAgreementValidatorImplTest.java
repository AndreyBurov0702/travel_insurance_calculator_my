package org.javaguru.travel.insurance.core.validations;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TravelAgreementValidatorImplTest {

    @Mock
    private TravelAgreementFieldValidator agreementFieldValidator;
    @Mock
    private TravelPersonFieldValidator personFieldValidator;
    @Mock
    private AgreementDTO agreement;
    @InjectMocks
    private TravelAgreementValidatorImpl validator;



    @Test
    public void shouldNotReturnErrors() {
        when(agreementFieldValidator.validate(agreement)).thenReturn(List.of());
        when(personFieldValidator.validate(any())).thenReturn(List.of());
        List<ValidationErrorDTO> errors = validator.validate(agreement);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void shouldReturnAgreementErrors() {
        when(agreementFieldValidator.validate(agreement)).thenReturn(List.of(new ValidationErrorDTO()));
        when(personFieldValidator.validate(any())).thenReturn(List.of());
        List<ValidationErrorDTO> errors = validator.validate(agreement);
        assertEquals(errors.size(), 1);
    }

    @Test
    public void shouldReturnPersonErrors() {
        when(agreementFieldValidator.validate(agreement)).thenReturn(List.of());
        when(personFieldValidator.validate(any())).thenReturn(List.of(new ValidationErrorDTO()));
        List<ValidationErrorDTO> errors = validator.validate(agreement);
        assertEquals(errors.size(), 1);
    }

    @Test
    public void shouldReturnAgreementAndPersonErrors() {
        when(agreementFieldValidator.validate(agreement)).thenReturn(List.of(new ValidationErrorDTO()));
        when(personFieldValidator.validate(any())).thenReturn(List.of(new ValidationErrorDTO()));
        List<ValidationErrorDTO> errors = validator.validate(agreement);
        assertEquals(errors.size(), 2);
    }
}

