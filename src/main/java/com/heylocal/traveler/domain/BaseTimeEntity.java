/**
 * packageName    : com.heylocal.traveler.domain
 * fileName       : BaseTimeEntity
 * author         : 우태균
 * date           : 2022/08/14
 * description    : 모든 엔티티가 상속받는 생성일·수정일 엔티티
 */

package com.heylocal.traveler.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class BaseTimeEntity {
  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime modifiedDate;
}
