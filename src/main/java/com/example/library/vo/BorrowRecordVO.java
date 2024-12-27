package com.example.library.vo;

import com.example.library.entity.BorrowRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BorrowRecordVO extends BorrowRecord {
    private String userName;
    private String bookName;
} 