package com.example.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.library.entity.Book;
import com.example.library.entity.BorrowRecord;
import com.example.library.entity.User;
import com.example.library.mapper.BorrowRecordMapper;
import com.example.library.service.BookService;
import com.example.library.service.BorrowRecordService;
import com.example.library.service.UserService;
import com.example.library.vo.BorrowRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements BorrowRecordService {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Override
    public Page<BorrowRecordVO> pageAll(Integer pageNum, Integer pageSize) {
        Page<BorrowRecord> page = page(new Page<>(pageNum, pageSize));
        return convertToVOPage(page);
    }

    @Override
    public Page<BorrowRecordVO> pageByUserId(Long userId, Integer pageNum, Integer pageSize) {
        Page<BorrowRecord> page = page(
            new Page<>(pageNum, pageSize),
            new LambdaQueryWrapper<BorrowRecord>()
                .eq(BorrowRecord::getUserId, userId)
                .orderByDesc(BorrowRecord::getCreateTime)
        );
        return convertToVOPage(page);
    }

    @Override
    @Transactional
    public void borrow(Long userId, Long bookId) {
        // 检查库存
        Book book = bookService.getById(bookId);
        if (book == null || book.getStock() <= 0) {
            throw new RuntimeException("图书库存不足");
        }

        // 检查是否已借阅
        long count = count(new LambdaQueryWrapper<BorrowRecord>()
            .eq(BorrowRecord::getUserId, userId)
            .eq(BorrowRecord::getBookId, bookId)
            .eq(BorrowRecord::getStatus, 0));
        if (count > 0) {
            throw new RuntimeException("您已借阅此书");
        }

        // 创建借阅记录
        BorrowRecord record = new BorrowRecord();
        record.setUserId(userId);
        record.setBookId(bookId);
        record.setBorrowTime(LocalDateTime.now());
        record.setStatus(0);
        save(record);

        // 更新库存
        book.setStock(book.getStock() - 1);
        bookService.updateById(book);
    }

    @Override
    @Transactional
    public void returnBook(Long recordId) {
        BorrowRecord record = getById(recordId);
        if (record == null || record.getStatus() == 1) {
            throw new RuntimeException("借阅记录不存在或图书已归还");
        }

        // 更新借阅记录
        record.setReturnTime(LocalDateTime.now());
        record.setStatus(1);
        updateById(record);

        // 更新库存
        Book book = bookService.getById(record.getBookId());
        book.setStock(book.getStock() + 1);
        bookService.updateById(book);
    }

    @Override
    public List<BorrowRecordVO> listByUserId(Long userId) {
        // 查询用户的所有借阅记录
        List<BorrowRecord> records = list(
            new LambdaQueryWrapper<BorrowRecord>()
                .eq(BorrowRecord::getUserId, userId)
                .orderByDesc(BorrowRecord::getCreateTime)
        );

        // 转换为VO
        List<BorrowRecordVO> voList = new ArrayList<>();
        for (BorrowRecord record : records) {
            BorrowRecordVO vo = new BorrowRecordVO();
            BeanUtils.copyProperties(record, vo);
            
            // 设置图书名
            Book book = bookService.getById(record.getBookId());
            if (book != null) {
                vo.setBookName(book.getName());
            }
            
            voList.add(vo);
        }
        
        return voList;
    }

    private Page<BorrowRecordVO> convertToVOPage(Page<BorrowRecord> page) {
        List<BorrowRecordVO> voList = new ArrayList<>();
        for (BorrowRecord record : page.getRecords()) {
            BorrowRecordVO vo = new BorrowRecordVO();
            BeanUtils.copyProperties(record, vo);
            
            // 设置用户名
            User user = userService.getById(record.getUserId());
            if (user != null) {
                vo.setUserName(user.getName());
            }
            
            // 设置图书名
            Book book = bookService.getById(record.getBookId());
            if (book != null) {
                vo.setBookName(book.getName());
            }
            
            voList.add(vo);
        }
        
        Page<BorrowRecordVO> voPage = new Page<>();
        BeanUtils.copyProperties(page, voPage, "records");
        voPage.setRecords(voList);
        return voPage;
    }
} 