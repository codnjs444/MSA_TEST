package com.example.userService.global.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
	
	@Comment("등록일시")
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Comment("등록자")
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @Comment("수정일시")
    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    @Comment("수정자")
    @LastModifiedBy
    @Column
    private String updatedBy;
}
