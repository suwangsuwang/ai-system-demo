package com.swan.demo.service;

import com.swan.demo.entity.Asset;
import com.swan.demo.mapper.AssetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetService {

    @Autowired
    private AssetMapper assetMapper;

    public Asset findById(Long id) {
        return assetMapper.findById(id);
    }


}
