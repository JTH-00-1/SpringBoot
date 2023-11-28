package com.example.demo.community;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community,Long> {
    Community findByRegisternum(Long registernum);
}
