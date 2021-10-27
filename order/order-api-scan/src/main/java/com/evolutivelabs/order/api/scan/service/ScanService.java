package com.evolutivelabs.order.api.scan.service;

import com.evolutivelabs.order.common.service.CommonService;
import com.evolutivelabs.order.database.entity.Menu;
import com.evolutivelabs.order.database.facotry.RepositoryFacotry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScanService extends CommonService {
    @Autowired
    public ScanService(RepositoryFacotry repositoryFacotry) {
        super(repositoryFacotry);
    }

    public Menu getMenuBySno(String sno) {
        return new Menu();//repositoryFacotry.getMenuRespository().findById(sno).get();
    }

}
