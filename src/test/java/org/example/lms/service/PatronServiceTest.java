package org.example.lms.service;
import javafx.collections.ObservableList;
import org.example.lms.model.Patron;
import org.example.lms.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatronServiceTest {

    private PatronRepository patronRepository;
    private PatronService patronService;

    @BeforeEach
    public void setUp() {
        patronRepository = Mockito.mock(PatronRepository.class);
        patronService = new PatronService(patronRepository);
    }

    @Test
    public void testAddPatron() {
        Patron patron = new Patron(12345, "Patrick Mugabe","pmugabe@gmail.com");

        patronService.addPatron(patron);

        verify(patronRepository, times(1)).addPatron(patron);
    }

    @Test
    public void testUpdatePatron() {
        Patron patron = new Patron(12345, "Patrick Mugabe","pmugabe@gmail.com");

        patronService.updatePatron(patron);

        verify(patronRepository, times(1)).updatePatron(patron);
    }

    @Test
    public void testDeletePatron() {
        long patronID = 1;

        patronService.deletePatron(patronID);

        verify(patronRepository, times(1)).deletePatron(patronID);
    }

    @Test
    public void testGetAllPatrons() {
        Patron patron1 = new Patron(12345, "Patrick Mugabe","pmugabe@gmail.com");
        Patron patron2 = new Patron(67891, "Rose Kituyi","rosekituyi@gmail.com");
        List<Patron> patrons = Arrays.asList(patron1, patron2);

        when(patronRepository.getAllPatrons()).thenReturn(patrons);

        ObservableList<Patron> result = patronService.getAllPatrons();

        assertEquals(2, result.size());
        assertTrue(result.contains(patron1));
        assertTrue(result.contains(patron2));
    }
}
