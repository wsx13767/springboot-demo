package com.evolutivelabs.ordermanager.common.service;

import com.evolutivelabs.ordermanager.common.factory.RepositoryFactory;

public class CommonService {
    protected final RepositoryFactory repositoryFactory;

    public CommonService(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }
}
