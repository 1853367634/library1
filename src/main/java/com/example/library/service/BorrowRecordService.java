package com.example.library.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.library.entity.BorrowRecord;
import com.example.library.vo.BorrowRecordVO;

import java.util.List;

public interface BorrowRecordService extends IService<BorrowRecord> {
    
    Page<BorrowRecordVO> pageAll(Integer pageNum, Integer pageSize);
    
    Page<BorrowRecordVO> pageByUserId(Long userId, Integer pageNum, Integer pageSize);
    
    void borrow(Long userId, Long bookId);
    
    void returnBook(Long recordId);

    List<BorrowRecordVO> listByUserId(Long userId);
} 