package com.siang.security.server.service;

import com.google.common.collect.Lists;
import com.siang.security.server.database.entity.Menu;
import com.siang.security.server.database.entity.MenuRole;
import com.siang.security.server.database.entity.Role;
import com.siang.security.server.database.repository.MenuRepository;
import com.siang.security.server.database.repository.MenuRoleRepository;
import com.siang.security.server.database.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "User_Role_Menu_CacheConfig")
public class RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuRoleRepository menuRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Cacheable(key = "'Menu.all'")
    public List<Menu> allMenu() {
        return Lists.newArrayList(menuRepository.findAll());
    }

    @Cacheable(key = "'MenuRole.' + #mid")
    public List<MenuRole> getRids(Integer mid) {
        return menuRoleRepository.findByMid(mid);
    }

    @Cacheable(key = "'Role.list.' + #ids")
    public Iterable<Role> findAllRolesByIds(Iterable<Integer> ids) {
        return roleRepository.findAllById(ids);
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 500)
    public void clearRoleList() {
        redisTemplate.keys("*").forEach(
                key -> {
                    logger.info("{}", key);
                    if (key.startsWith("User_Role_Menu_CacheConfig::Role.list")) {
                        redisTemplate.delete(key);
                    }
                }
        );

        logger.info("clear role list");
    }

}
