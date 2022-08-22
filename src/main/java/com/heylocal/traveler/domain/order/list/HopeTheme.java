package com.heylocal.traveler.domain.order.list;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.order.OrderSheet;
import com.heylocal.traveler.domain.theme.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 의뢰서 작성 내용 - 희망 테마 목록
 * 중간 연결 엔티티
 */

@Entity
@Table(name = "HOPE_THEME")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class HopeTheme extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private OrderSheet orderSheet;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Theme theme;
}
