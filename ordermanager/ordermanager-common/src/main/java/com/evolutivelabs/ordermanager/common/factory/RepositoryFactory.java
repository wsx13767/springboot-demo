package com.evolutivelabs.ordermanager.common.factory;

import com.evolutivelabs.ordermanager.database.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryFactory {
    @Autowired
    private MenuRepository menuRepository;

    public MenuRepository getMenuRepository() {
        return menuRepository;
    }

    public void setMenuRepository(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }
}
