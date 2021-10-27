package com.evolutivelabs.order.common.service;

import com.evolutivelabs.order.database.facotry.RepositoryFacotry;

public class CommonService {
    protected RepositoryFacotry repositoryFacotry;

    public CommonService(RepositoryFacotry repositoryFacotry) {
        this.repositoryFacotry = repositoryFacotry;
    }
}
