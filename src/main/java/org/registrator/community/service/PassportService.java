package org.registrator.community.service;

import java.util.List;

import org.registrator.community.entity.PassportInfo;

public interface PassportService {

    void delete(List<PassportInfo> passportInfoList);

}
