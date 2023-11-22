package com.example.demo.event;

import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Table(name = "eventboard")
public class Event implements Persistable {
    @Id
    private String eventhash;

    @Column(columnDefinition = "TEXT")
    private String content;
    private String statement; //기본으로"진행중인이벤트"들어감    
    private boolean deletecheck; //기본으로 "0"으로 들어감

    private String title;
    private String imgurl;

    @CreatedDate //생성일자 생성
    private LocalDateTime date;
    private LocalDateTime enddate;
    //@LastModifiedDate //데이터 수정 시 시간을 자동으로 업데이트해줌 <--사용하지 않는 이유는 삭제할때도 update하면서 수정일자를 변경하기때문
    private LocalDateTime fixdate;

    @ManyToOne
    @JoinColumn(name="user")
    private User uid;

    @Override
    public String getId() {
        return null;
    }
    @Override
    public boolean isNew() { //기존의 Repagitory.save()의 방식으로 하게되면 id를 임의로 만들면서 Entity가 있는것으로 판단되어 merge가 실행됨.
        // 그래서 조회 쿼리를 한번 더 부른 뒤 수정하기 때문에 id를 기준으로 하는것이 아닌 생성일자를 기준으로 판단하게 설정
        return date == null;
    }
}