package com.training.springbootbuyitem.service;

import com.training.springbootbuyitem.entity.model.Buyer;
import com.training.springbootbuyitem.entity.model.Item;
import com.training.springbootbuyitem.enums.EnumEntity;
import com.training.springbootbuyitem.error.EntityNotFoundException;
import com.training.springbootbuyitem.repository.BuyerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class BuyerService implements  IBuyerService{

    @Autowired
    BuyerRepository buyerRepository;

    @Override
    public List<Buyer> list() {
        return buyerRepository.findAll();
    }

    @Override
    public Buyer get(Long id) {
        return buyerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EnumEntity.BUYER.name(), id)) ;
    }

    @Override
    public List<Buyer> get(List<Long> id) {
        return null;
    }

    @Override
    public void delete(Long id) {
        buyerRepository.delete(get(id));
    }

    @Override
    public Buyer update(Buyer entity) {
        Buyer persistedBuyer = get(entity.getUserUid());
        if (StringUtils.hasText(entity.getName())) {
            persistedBuyer.setName(entity.getName());
        }
        buyerRepository.save(persistedBuyer);
        return persistedBuyer;
    }

    @Override
    public Buyer save(Buyer entity) {
        return buyerRepository.save(entity);
    }
}
