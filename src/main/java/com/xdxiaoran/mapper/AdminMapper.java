package com.xdxiaoran.mapper;

import com.xdxiaoran.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface AdminMapper {
    List<Admin>getAllAdmin();
}
