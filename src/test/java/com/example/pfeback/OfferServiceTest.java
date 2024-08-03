package com.example.pfeback;

import com.example.pfeback.Entities.Offer;
import com.example.pfeback.Repositories.OfferRepository;
import com.example.pfeback.Service.OfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private OfferService offerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOffer() {
        Offer offer = new Offer();
        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        Offer result = offerService.createOffer(offer);
        assertEquals(offer, result);
    }

    @Test
    void testGetOfferById_Success() {
        Offer offer = new Offer();
        when(offerRepository.findById(anyLong())).thenReturn(Optional.of(offer));

        Offer result = offerService.getOfferById(1L);
        assertEquals(offer, result);
    }

    @Test
    void testGetOfferById_NotFound() {
        when(offerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Offer result = offerService.getOfferById(1L);
        assertNull(result);
    }

    @Test
    void testUpdateOffer_Success() {
        Offer existingOffer = new Offer();
        Offer offerDetails = new Offer();
        when(offerRepository.findById(anyLong())).thenReturn(Optional.of(existingOffer));
        when(offerRepository.save(any(Offer.class))).thenReturn(offerDetails);

        Offer result = offerService.updateOffer(1L, offerDetails);
        assertEquals(offerDetails, result);
    }

    @Test
    void testUpdateOffer_NotFound() {
        Offer offerDetails = new Offer();
        when(offerRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            offerService.updateOffer(1L, offerDetails);
        });
        assertEquals("Offer with id 1 does not exist", thrown.getMessage());
    }

    @Test
    void testDeleteOffer_Success() {
        when(offerRepository.existsById(anyLong())).thenReturn(true);

        offerService.deleteOffer(1L);
        verify(offerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteOffer_NotFound() {
        when(offerRepository.existsById(anyLong())).thenReturn(false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            offerService.deleteOffer(1L);
        });
        assertEquals("Offer with id 1 does not exist", thrown.getMessage());
    }
}
