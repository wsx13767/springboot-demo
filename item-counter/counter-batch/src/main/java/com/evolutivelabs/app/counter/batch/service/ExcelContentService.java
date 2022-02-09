package com.evolutivelabs.app.counter.batch.service;

import com.evolutivelabs.app.counter.batch.annotation.BeanInject;
import com.evolutivelabs.app.counter.common.model.excelpaser.ItemBundle;
import com.evolutivelabs.app.counter.common.model.excelpaser.ShippingBox;
import com.evolutivelabs.app.counter.database.mysql.RepositoryFactory;
import com.evolutivelabs.app.counter.database.mysql.entity.BoxOfExcel;
import com.evolutivelabs.app.counter.database.mysql.entity.ExcelFile;
import com.evolutivelabs.app.counter.database.mysql.entity.ItemsOfBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ExcelContentService {
    @Autowired
    private RepositoryFactory repositoryFactory;

    @Transactional
    public void saveExcelContent(ExcelFile excelFile, Optional<List<ShippingBox>> shippingBoxes) {
        shippingBoxes.ifPresent(list -> {
            for (int i = 0; i< list.size();i++) {
                ShippingBox shippingBox = list.get(i);
                BoxOfExcel boxOfExcel = new BoxOfExcel();
                boxOfExcel.setExcelId(excelFile.getId());
                boxOfExcel.setBoxSeries(i);
                boxOfExcel.setBoxCarton(shippingBox.getBoxCarton());
                repositoryFactory.getBoxOfExcelRepository().save(boxOfExcel);

                for (int j = 0;j< shippingBox.getItemBundles().size();j++) {
                    ItemBundle itemBundle = shippingBox.getItemBundles().get(j);
                    ItemsOfBox itemsOfBox = new ItemsOfBox();
                    itemsOfBox.setExcelId(excelFile.getId());
                    itemsOfBox.setBoxSeries(i);
                    itemsOfBox.setSku(itemBundle.getSku());
                    itemsOfBox.setFnSku(itemBundle.getFnsku());
                    itemsOfBox.setNum(itemBundle.getNum().intValue());
                    itemsOfBox.setTitle(itemBundle.getTitle());
                    itemsOfBox.setWeight(itemBundle.getNum().multiply(itemBundle.getItemInfo().getWeight()));
                    itemsOfBox.setVolume(itemBundle.getNum().multiply(itemBundle.getItemInfo().getVolume()));

                    repositoryFactory.getItemsOfBoxRepository().save(itemsOfBox);
                }
            }
        });
    }
}
