package com.evolutivelabs.ordermanager.api.scan.service;

import com.evolutivelabs.ordermanager.common.service.CommonService;
import com.evolutivelabs.ordermanager.database.entity.Menu;
import com.evolutivelabs.ordermanager.common.factory.RepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScanService extends CommonService {
    @Autowired
    public ScanService(RepositoryFactory repositoryFactory) {
        super(repositoryFactory);
    }

    public Menu searchBySno(String sno) {
        return repositoryFactory.getMenuRepository().findById(sno).get();
    }
}
