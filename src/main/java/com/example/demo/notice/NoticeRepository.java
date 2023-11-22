package com.example.demo.notice;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Notice findByNoticehash(String noticehash);
    Page<Notice> findByDeletecheck(Boolean check,Pageable pageable);
    Page<Notice> findAll(Specification<Notice> spec,Pageable pageable);

    Page<Notice> findAll(Pageable pageable);
}