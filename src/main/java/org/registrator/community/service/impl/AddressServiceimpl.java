package org.registrator.community.service.impl;

import java.util.List;

import org.registrator.community.dao.AddressRepository;
import org.registrator.community.entity.Address;
import org.registrator.community.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceimpl implements AddressService{
    
    @Autowired
    private AddressRepository addressRepository;
    
    @Override
    public void delete(List<Address> addressList){
        addressRepository.delete(addressList);
    }

}
