package com.example.pfeback.IService;
import com.example.pfeback.Entities.Offer;
import java.util.List;

public interface IOfferService {
    List<Offer> getOffersByType(String type);
    Offer createOffer(Offer offer);
    Offer getOfferById(Long id);
    Offer updateOffer(Long id, Offer offerDetails);
    void deleteOffer(Long id);
}