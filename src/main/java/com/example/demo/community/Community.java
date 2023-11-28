package com.example.demo.community;

import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "community")
@EntityListeners(AuditingEntityListener.class)
public class Community{
    @Id
    @Column(name="ComhashID")
    private String comhash;

    @ManyToOne
    @JoinColumn(name="u_id")
    private User uid;
    @Column(name="RegisterNum")
    private Long registernum;
    /*@ManyToOne 사용자(업주) 병합 후 연결해줘야함 임시로 String 사용
    @JoinColumn(name="RegisterNum")
    private Register register;*/
}