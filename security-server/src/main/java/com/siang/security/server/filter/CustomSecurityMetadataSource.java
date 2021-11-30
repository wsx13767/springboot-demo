package com.siang.security.server.filter;

import com.google.common.collect.Lists;
import com.siang.security.server.database.entity.Menu;
import com.siang.security.server.database.entity.MenuRole;
import com.siang.security.server.database.entity.Role;
import com.siang.security.server.database.repository.MenuRepository;
import com.siang.security.server.database.repository.MenuRoleRepository;
import com.siang.security.server.database.repository.RoleRepository;
import com.siang.security.server.service.RoleService;
import org.apache.el.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private RoleService roleService;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestURI = ((FilterInvocation) object).getRequest().getRequestURI();
        List<Menu> allMenu = Lists.newArrayList(roleService.allMenu());
        for (Menu menu : allMenu) {
            if (antPathMatcher.match(menu.getPattern(), requestURI)) {
                List<Integer> rids = roleService.getRids(menu.getId())
                        .stream()
                        .map(MenuRole::getRid)
                        .toList();
                String[] roles =
                        StreamSupport
                                .stream(roleService.findAllRolesByIds(rids).spliterator(), false)
                                .map(role -> "ROLE_".concat(role.getName()))
                                .toArray(String[]::new);
                return SecurityConfig.createList(roles);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
