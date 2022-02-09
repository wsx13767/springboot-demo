package com.evolutivelabs.app.counter.batch.job.impl;

import com.evolutivelabs.app.counter.batch.annotation.BeanInject;
import com.evolutivelabs.app.counter.batch.job.CommonJob;
import com.evolutivelabs.app.counter.database.mysql.repository.BoxInfoRepository;

public class MyJob extends CommonJob {

//    @BeanInject
//    private BoxInfoRepository boxInfoRepository;

    @Override
    protected void execute() {
//        System.out.println(boxInfoRepository.findAll());
        System.out.println("Hello this myJob");
    }
}
