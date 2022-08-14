package com.heylocal.traveler.domain.theme;

import com.heylocal.traveler.domain.BaseTimeEntity;
import com.heylocal.traveler.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 테마
 */

@Entity
@Table(name = "THEME")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Theme extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String imageAddress;

  //양방향 설정

  @OneToMany(mappedBy = "theme") //테마를 제거해도 포스트는 그대로 있어야 하므로, cascade 설정 X
  private List<Post> postList = new ArrayList<>();
}
