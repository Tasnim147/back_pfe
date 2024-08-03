package com.example.pfeback.Service;

import com.example.pfeback.Entities.Offer;
import com.example.pfeback.IService.IOfferService;
import com.example.pfeback.Repositories.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OfferService implements IOfferService {
    @Autowired
    private OfferRepository offerRepository;

    @Override
    public List<Offer> getOffersByType(String type) {
        return offerRepository.findByType(type);
    }

    @Override
    public Offer createOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    @Override
    public Offer getOfferById(Long id) {
        return offerRepository.findById(id).orElse(null);
    }

    @Override
    public Offer updateOffer(Long id, Offer offerDetails) {
        Offer offer = getOfferById(id);
        if (offer != null) {
            offer.setType(offerDetails.getType());
            offer.setName(offerDetails.getName());
            offer.setDescription(offerDetails.getDescription());
            offer.setPrice(offerDetails.getPrice());
            return offerRepository.save(offer);
        } else {
            throw new IllegalArgumentException("Offer with id " + id + " does not exist");
        }
    }

    @Override
    public void deleteOffer(Long id) {
        if (offerRepository.existsById(id)) {
            offerRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Offer with id " + id + " does not exist");
        }
    }
}
