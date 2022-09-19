package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.place.PlaceCategory;
import com.heylocal.traveler.domain.travelon.*;
import com.heylocal.traveler.domain.travelon.list.*;
import com.heylocal.traveler.domain.travelon.opinion.EvaluationDegree;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import({OpinionRepository.class})
@DataJpaTest
class OpinionRepositoryTest {
  @PersistenceContext
  private EntityManager em;
  @Autowired
  private OpinionRepository opinionRepository;
  long placeId = 0;

  @Test
  @DisplayName("답변 저장")
  void saveTest() {
    //GIVEN
    User travelOnAuthor = User.builder()
            .accountId("myAccountId")
            .nickname("myNickname")
            .password("myPassword123!")
            .userRole(UserRole.TRAVELER)
            .build();
    em.persist(travelOnAuthor);

    TravelOn travelOn = saveTravelOn(travelOnAuthor, "myState", "myCity");
    Opinion notPersistOpinion = getNotPersistOpinion(travelOn);

    //WHEN
    opinionRepository.save(notPersistOpinion);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 저장이 되었는지
        () -> assertSame(notPersistOpinion, em.find(Opinion.class, notPersistOpinion.getId())),
        //성공 케이스 - 2 - Flush
        () -> assertDoesNotThrow(() -> em.flush())
    );
  }

  @Test
  @DisplayName("ID 로 Opinion 조회")
  void findByIdTest() {
    //GIVEN
    User travelOnAuthor = User.builder().accountId("myAccountId").password("myPassword").nickname("myNickname").userRole(UserRole.TRAVELER).build();

    em.persist(travelOnAuthor);

    TravelOn travelOn = saveTravelOn(travelOnAuthor, "myState", "myCity");
    Opinion opinion = getNotPersistOpinion(travelOn);

    em.persist(opinion);

    long existOpinionId = opinion.getId();
    long notExistOpinionId = existOpinionId + 1L;

    //WHEN
    Optional<Opinion> succeedResult = opinionRepository.findById(existOpinionId);
    Optional<Opinion> failResult = opinionRepository.findById(notExistOpinionId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 존재하는 id 로 조회한 경우
        () -> assertTrue(succeedResult.isPresent()),
        () -> assertSame(opinion, succeedResult.get()),
        //실패 케이스 - 1 - 존재하지 않은 id 로 조회한 경우
        () -> assertFalse(failResult.isPresent())
    );
  }

  @Test
  @DisplayName("id와 travelOn 으로 조회")
  void findByIdAndTravelOnTest() {
    //GIVEN
    User travelOnAuthor = User.builder().accountId("myAccountId").password("myPassword").nickname("myNickname").userRole(UserRole.TRAVELER).build();

    em.persist(travelOnAuthor);

    TravelOn travelOnOfOpinion = saveTravelOn(travelOnAuthor, "myState", "myCity");
    TravelOn travelOnNoOpinion = saveTravelOn(travelOnAuthor, "myState", "myCity");
    Opinion opinion = getNotPersistOpinion(travelOnOfOpinion);

    em.persist(opinion);

    long existOpinionId = opinion.getId();
    long notExistOpinionId = existOpinionId + 1L;
    long travelOnOfOpinionId = travelOnOfOpinion.getId();
    long travelOnNoOpinionId = travelOnNoOpinion.getId();

    //WHEN
    Optional<Opinion> succeedResult = opinionRepository.findByIdAndTravelOn(existOpinionId, travelOnOfOpinionId);
    Optional<Opinion> wrongOpinionIdResult = opinionRepository.findByIdAndTravelOn(notExistOpinionId, travelOnOfOpinionId);
    Optional<Opinion> wrongTravelOnIdResult = opinionRepository.findByIdAndTravelOn(existOpinionId, travelOnNoOpinionId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 존재하는 id 로 조회한 경우
        () -> assertTrue(succeedResult.isPresent()),
        () -> assertSame(opinion, succeedResult.get()),
        //실패 케이스 - 1 - 존재하지 않은 opinion id 로 조회한 경우
        () -> assertFalse(wrongOpinionIdResult.isPresent()),
        //실패 케이스 - 2 - 해당 opinion 을 가지지 않는 조회한 경우
        () -> assertFalse(wrongTravelOnIdResult.isPresent())
    );
  }

  @Test
  @DisplayName("삭제")
  void removeTest() {
    //GIVEN
    User travelOnAuthor = User.builder().accountId("myAccountId").password("myPassword").nickname("myNickname").userRole(UserRole.TRAVELER).build();

    em.persist(travelOnAuthor);

    TravelOn travelOnOfOpinion = saveTravelOn(travelOnAuthor, "myState", "myCity");
    Opinion opinion = getNotPersistOpinion(travelOnOfOpinion);

    em.persist(opinion);

    long opinionId = opinion.getId();

    //WHEN
    opinionRepository.remove(opinion);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 정말 제거되었는지
        () -> assertNull(em.find(Opinion.class, opinionId)),
        //성공 케이스 - 2 - Flush 가 가능한지
        () -> assertDoesNotThrow(() -> em.flush())
    );

  }

  /**
   * 영속화되지 않은 새 Opinion 엔티티를 반환하는 메서드
   * @param travelOn Opinion 이 추가될 TravelOn 엔티티
   * @return
   */
  private Opinion getNotPersistOpinion(TravelOn travelOn) {
    Place place = savePlace(travelOn.getRegion());
    Opinion opinion = Opinion.builder()
        .travelOn(travelOn)
        .author(travelOn.getAuthor()) //테스트용으로 여행On 작성자가 답변을 달았다고 가정
        .region(travelOn.getRegion())
        .place(place)
        .facilityCleanliness(EvaluationDegree.GOOD)
        .canParking(true)
        .waiting(false)
        .costPerformance(EvaluationDegree.GOOD)
        .build();

    opinion.setTravelOn(travelOn);

    return opinion;
  }

  /**
   * 새 TravelOn 엔티티를 저장하는 메서드
   * @param author
   * @param state
   * @param city
   * @return
   */
  private TravelOn saveTravelOn(User author, String state, String city) {
    TravelOn travelOn;
    String title = "testTitle";
    LocalDate travelStartDate = LocalDate.now().plusMonths(1);
    LocalDate travelEndDate = LocalDate.now().plusMonths(1).plusDays(3);
    String description = "test description";
    TransportationType transportationType = TransportationType.OWN_CAR;
    int accommodationMaxCost = 100000;
    int foodMaxCost = 100000;
    int drinkMaxCost = 100000;

    Region region = getPersistRegion(state, city);

    travelOn = TravelOn.builder()
        .title(title)
        .region(region)
        .views(10)
        .author(author)
        .travelStartDate(travelStartDate)
        .travelEndDate(travelEndDate)
        .description(description)
        .transportationType(transportationType)
        .accommodationMaxCost(accommodationMaxCost)
        .foodMaxCost(foodMaxCost)
        .drinkMaxCost(drinkMaxCost)
        .createdDate(LocalDateTime.now())
        .build();
    em.persist(travelOn);

    TravelMember travelMember1 = TravelMember.builder()
        .travelOn(travelOn)
        .memberType(MemberType.CHILD)
        .build();
    TravelMember travelMember2 = TravelMember.builder()
        .travelOn(travelOn)
        .memberType(MemberType.PARENT)
        .build();
    em.persist(travelMember1);
    em.persist(travelMember2);
    travelOn.addTravelMember(travelMember1);
    travelOn.addTravelMember(travelMember2);

    HopeAccommodation accommodation1 = HopeAccommodation.builder()
        .travelOn(travelOn)
        .type(AccommodationType.HOTEL)
        .build();
    HopeAccommodation accommodation2 = HopeAccommodation.builder()
        .travelOn(travelOn)
        .type(AccommodationType.GUEST_HOUSE)
        .build();
    em.persist(accommodation1);
    em.persist(accommodation2);
    travelOn.addHopeAccommodation(accommodation1);
    travelOn.addHopeAccommodation(accommodation2);

    HopeFood food1 = HopeFood.builder()
        .travelOn(travelOn)
        .type(FoodType.GLOBAL)
        .build();
    HopeFood food2 = HopeFood.builder()
        .travelOn(travelOn)
        .type(FoodType.KOREAN)
        .build();
    em.persist(food1);
    em.persist(food2);
    travelOn.addHopeFood(food1);
    travelOn.addHopeFood(food2);

    HopeDrink hopeDrink1 = HopeDrink.builder()
        .travelOn(travelOn)
        .type(DrinkType.BEER)
        .build();
    HopeDrink hopeDrink2 = HopeDrink.builder()
        .travelOn(travelOn)
        .type(DrinkType.BEER)
        .build();
    em.persist(hopeDrink1);
    em.persist(hopeDrink2);
    travelOn.addHopeDrink(hopeDrink1);
    travelOn.addHopeDrink(hopeDrink2);

    TravelTypeGroup travelTypeGroup = TravelTypeGroup.builder()
        .activityTasteType(ActivityTasteType.HARD)
        .placeTasteType(PlaceTasteType.FAMOUS)
        .snsTasteType(SnsTasteType.YES)
        .travelOn(travelOn)
        .build();
    em.persist(travelTypeGroup);
    travelOn.registerTravelTypeGroup(travelTypeGroup);

    em.persist(travelOn);

    return travelOn;
  }

  /**
   * 새 Place 엔티티를 저장하는 메서드
   * @param region 새 Place의 Region
   * @return
   */
  private Place savePlace(Region region) {
    Place place = Place.builder()
        .id(++placeId)
        .category(PlaceCategory.CE7)
        .name("placeName")
        .roadAddress("roadAddress")
        .address("address")
        .lat(0.0)
        .lng(0.0)
        .region(region)
        .link("link")
        .build();
    em.persist(place);

    return place;
  }

  /**
   * 영속화된 Region 엔티티를 얻는 메서드
   * @param state
   * @param city
   * @return
   */
  private Region getPersistRegion(String state, String city) {
    String jpql = "select r from Region r" +
        " where r.state = :state" +
        " and r.city = :city";
    Region region;

    try {
      region = em.createQuery(jpql, Region.class)
          .setParameter("state", state)
          .setParameter("city", city)
          .getSingleResult();
    } catch (NoResultException e) {
      region = Region.builder()
          .city(city)
          .state(state)
          .build();
      em.persist(region);
    }

    return region;
  }

}