package com.example.demo.event;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    Event findEventByEventhash(String eventhash);

    Page<Event> findAll(Specification<Event> spec, Pageable pageable);
    Page<Event> findByDeletecheckAndStatement(Boolean deletecheck,String statement, Pageable pageable);

}