package com.swan.demo.mapper;

import com.swan.demo.entity.Asset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AssetMapper {

    @Select("""
            select *
            from asset
            where id = #{id}
            """)
    Asset findById(Long id);

    @Select("select 1")
    Integer test();
}
