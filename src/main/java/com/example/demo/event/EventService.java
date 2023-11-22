package com.example.demo.event;


import com.example.demo.DataNotFoundException;
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
import java.util.*;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    
    private Specification<Event> search(String kw,String statement) {
        return new Specification<Event>() {
                private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Event> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Event, User> u1 = q.join("uid", JoinType.LEFT);
                return cb.and(
                        cb.or(cb.like(q.get("title"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용 
                        cb.like(u1.get("nickname"), "%" + kw + "%"))    // 질문 작성자
                        ,cb.equal(q.get("deletecheck"),false),            //삭제 여부 체크
                        cb.equal(q.get("statement"),statement)); //진행중인 혹은 종료된 이벤트 구분
            }
        };
    }
    private EventDTO of(Event event) {
        return modelMapper.map(event, EventDTO.class);
    }
    
    private Event of(EventDTO eventDto) {
        return modelMapper.map(eventDto, Event.class);
    }

    public void checkStatement(List<EventDTO> listDTO){
        for (EventDTO dto: listDTO){
            if(dto.getEnddate().compareTo(LocalDateTime.now())<0){
                dto.setStatement("종료된이벤트");
                Event event = of(dto);
                this.eventRepository.save(event);
            }
        }
    }
    public Page<EventDTO> getListbyStatement(int page, String statement) {//Statement 구분을 하여 출력
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("Date"));
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        //종료시간을 체크하기 위해 진행중인 이벤트로 조건을 먼저 걸음
        Page<Event> eventList = this.eventRepository.findByDeletecheckAndStatement(false,statement, pageable);
        Page<EventDTO> eventDtoList = eventList.map(q -> of(q));

        List<EventDTO> listDTO = eventDtoList.getContent();

        //리스트 조회할때 현재시간을 기준으로 종료시간이 지나면 종료된 페이지로 이동
        checkStatement(listDTO);

        eventList = this.eventRepository.findByDeletecheckAndStatement(false,statement, pageable);
        eventDtoList = eventList.map(q -> of(q));
        System.out.println(eventDtoList.getContent());
        return eventDtoList;
    }
    public Page<EventDTO> getListbyStatement(int page, String kw, String statement) {//Statement 구분을 하여 출력
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("Date"));
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        //종료시간을 체크하기 위해 진행중인 이벤트로 조건을 먼저 걸음
        Specification<Event> spec = search(kw,statement);
        Page<Event> eventList = this.eventRepository.findAll(spec, pageable);
        Page<EventDTO> eventDtoList = eventList.map(q -> of(q));

        List<EventDTO> listDTO = eventDtoList.getContent();
        //리스트 조회할때 현재시간을 기준으로 종료시간이 지나면 종료된 페이지로 이동
        checkStatement(listDTO);

        spec = search(kw,statement);
        eventList = this.eventRepository.findAll(spec, pageable);
        eventDtoList = eventList.map(q -> of(q));
        return eventDtoList;
    }
    public EventDTO getEvent(String hash) {
        Event event = this.eventRepository.findEventByEventhash(hash);
        if (event != null) {
            return of(event);
        } else {
            throw new DataNotFoundException("event not found");
        }
    }
    @Transactional
    public void create(String title, String content, String userid, LocalDateTime localDateTime) {
        Event event = new Event();
        event.setTitle(title);
        event.setContent(content);
        event.setUid(this.userRepository.findById(userid).get());
        event.setEventhash(UUID.randomUUID().toString());
        event.setEnddate(localDateTime);

        this.eventRepository.save(event);
    }

    @Transactional
    public void modify(String hashcode, String title, String content) {
        Event event = this.eventRepository.findEventByEventhash(hashcode);
        event.setTitle(title);
        event.setContent(content);
        event.setFixdate(LocalDateTime.now());
    }
    @Transactional
    public void delete(String hashcode) {
        Event event = this.eventRepository.findEventByEventhash(hashcode);
        event.setDeletecheck(true);
    }
}