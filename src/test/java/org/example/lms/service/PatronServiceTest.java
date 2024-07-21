package org.example.lms.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.lms.model.Patron;
import org.example.lms.repository.PatronRepository;
import org.example.lms.service.PatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronService patronService;

    @BeforeEach
    void setUp() {
        patronService = new PatronService();
    }

    @Test
    void testAddPatron() {
        Patron patron = new Patron(1L, "John Doe", "johndoe@example.com");

        patronService.addPatron(patron);

        verify(patronRepository, times(1)).addPatron(patron);
    }

    @Test
    void testUpdatePatron() {
        Patron patron = new Patron(1L, "John Doe", "johndoe@example.com");

        patronService.updatePatron(patron);

        verify(patronRepository, times(1)).updatePatron(patron);
    }

    @Test
    void testDeletePatron() {
        long patronID = 1L;

        patronService.deletePatron(patronID);

        verify(patronRepository, times(1)).deletePatron(patronID);
    }

    @Test
    void testGetAllPatrons() {
        List<Patron> expectedPatrons = Arrays.asList(
                new Patron(1L, "John Doe", "johndoe@example.com"),
                new Patron(2L, "Jane Smith", "janesmith@example.com")
        );

        when(patronRepository.getAllPatrons()).thenReturn(expectedPatrons);

        ObservableList<Patron> actualPatrons = patronService.getAllPatrons();

        assertEquals(FXCollections.observableArrayList(expectedPatrons), actualPatrons);
    }
}


