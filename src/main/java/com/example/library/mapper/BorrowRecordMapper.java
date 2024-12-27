package com.example.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.library.entity.BorrowRecord;
import com.example.library.vo.BorrowRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {
    
    /**
     * 分页查询用户的借阅记录
     */
    IPage<BorrowRecordVO> selectBorrowRecordVOPage(Page<?> page, @Param("userId") Long userId);
} 