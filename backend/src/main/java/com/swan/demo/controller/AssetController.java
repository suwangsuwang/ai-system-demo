package com.swan.demo.controller;

import com.swan.demo.annotation.Require;
import com.swan.demo.common.Result;
import com.swan.demo.entity.Asset;
import com.swan.demo.mapper.AssetMapper;
import com.swan.demo.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetMapper assetMapper;

    @GetMapping("/{id}")
    @Require("admin || user")
    public Result<Asset> getAsset(
            @PathVariable Long id
    ) {

        System.out.println("🔥 controller hit asset id = " + id);

        Asset asset = assetService.findById(id);

        System.out.println("🔥 service result = " + asset);

        return Result.ok(asset);
    }

    @GetMapping("/test")
    public Result<Integer> getTest() {

        return Result.ok(assetMapper.test());
    }




}
