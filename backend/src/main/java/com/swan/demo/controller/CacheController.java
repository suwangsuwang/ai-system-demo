package com.swan.demo.controller;


import com.swan.demo.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cache")
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @GetMapping("/set")
    public Map<String, Object> set(@RequestParam String key, @RequestParam String value) {
            cacheService.set(key, value);

            return Map.of(
                    "success", true,
                    "key", key,
                    "vlaue", value
            );
    }

    @GetMapping("/get")
    public Map<String, Object> get(@RequestParam String key) {

        return  Map.of(
                "key", key,
                "value", cacheService.get(key)
        );
    }
}
