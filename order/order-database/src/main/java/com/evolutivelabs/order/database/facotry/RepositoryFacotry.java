package com.evolutivelabs.order.database.facotry;

import com.evolutivelabs.order.database.repository.BoxRepository;
import com.evolutivelabs.order.database.repository.MenuRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryFacotry {
    @Autowired
    private MenuRespository menuRespository;
    @Autowired
    private BoxRepository boxRepository;

    public MenuRespository getMenuRespository() {
        return menuRespository;
    }

    public void setMenuRespository(MenuRespository menuRespository) {
        this.menuRespository = menuRespository;
    }

    public BoxRepository getBoxRepository() {
        return boxRepository;
    }

    public void setBoxRepository(BoxRepository boxRepository) {
        this.boxRepository = boxRepository;
    }
}
