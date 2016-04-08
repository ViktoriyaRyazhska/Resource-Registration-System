package org.registrator.community.service.impl;

import java.util.List;

import org.registrator.community.dao.PassportRepository;
import org.registrator.community.entity.PassportInfo;
import org.registrator.community.service.PassportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassportServiceimpl implements PassportService{
    
    @Autowired
    private PassportRepository passportRepository;
    
    @Override
    public void delete(List<PassportInfo> passportInfoList){
        passportRepository.delete(passportInfoList);
    }

}
