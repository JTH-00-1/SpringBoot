package com.example.demo.notice;


import com.example.demo.DataNotFoundException;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    
    private Specification<Notice> search(String kw) {
        return new Specification<Notice>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Notice> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Notice, User> u1 = q.join("uid", JoinType.LEFT);
                return cb.and(
                        cb.or(cb.like(q.get("title"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용 
                        cb.like(u1.get("nickname"), "%" + kw + "%"))    // 질문 작성자
                        ,cb.equal(q.get("deletecheck"),false));             //삭제 여부 체크
            }
        };
    }
    private NoticeDTO of(Notice notice) {
        return modelMapper.map(notice, NoticeDTO.class);
    }

    public Page<NoticeDTO> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("Date"));
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));
        Page<Notice> noticeList = this.noticeRepository.findByDeletecheck(false,pageable);
        Page<NoticeDTO> noticeDtoList = noticeList.map(q -> of(q));
        return noticeDtoList;
    }
    public Page<NoticeDTO> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("Date"));
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));
        Specification<Notice> spec = search(kw);
        Page<Notice> noticeList = this.noticeRepository.findAll(spec, pageable);
        Page<NoticeDTO> noticeDtoList = noticeList.map(q -> of(q));

        return noticeDtoList;
    }
    
    public NoticeDTO getNotice(String hash) {
        Notice notice = this.noticeRepository.findByNoticehash(hash);
        if (notice!= null) {
            return of(notice);
        } else {
            throw new DataNotFoundException("notice not found");
        }
    }

    @Transactional//트랜잭션 사용
    public void create(String title, String content, String userid) {
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setUid(this.userRepository.findById(userid).get());
        notice.setNoticehash(UUID.randomUUID().toString());

        this.noticeRepository.save(notice);
    }

    @Transactional//더티체킹을 위한 트랜잭션
    public void modify(String hashcode, String title, String content) {
        Notice notice= this.noticeRepository.findByNoticehash(hashcode);
        notice.setTitle(title);
        notice.setContent(content);
        notice.setFixdate(LocalDateTime.now());
    }

    @Transactional
    public void delete(String hashcode) {
        Notice notice = this.noticeRepository.findByNoticehash(hashcode);
        notice.setDeletecheck(true);
    }
}