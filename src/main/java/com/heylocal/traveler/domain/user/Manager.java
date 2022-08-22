package com.heylocal.traveler.domain.user;

import com.heylocal.traveler.domain.note.Note;
import com.heylocal.traveler.domain.post.Post;
import com.heylocal.traveler.domain.travel.Travel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MANAGER")
@DiscriminatorValue("MANAGER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class Manager extends User {
  @Column(length = 20, nullable = false)
  private String realName;

  //양방향 설정

  @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Note> noteList = new ArrayList<>();

  @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Post> postList = new ArrayList<>();

  @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Travel> travelList = new ArrayList<>();
}
