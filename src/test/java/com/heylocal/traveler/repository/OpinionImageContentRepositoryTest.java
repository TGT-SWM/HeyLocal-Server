package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.place.Place;
import com.heylocal.traveler.domain.place.PlaceCategory;
import com.heylocal.traveler.domain.travelon.*;
import com.heylocal.traveler.domain.travelon.list.*;
import com.heylocal.traveler.domain.travelon.opinion.EvaluationDegree;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
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

@Import({OpinionImageContentRepository.class})
@DataJpaTest
class OpinionImageContentRepositoryTest {
  @PersistenceContext
  private EntityManager em;
  @Autowired
  private OpinionImageContentRepository opinionImageContentRepository;

  @Test
  @DisplayName("OpinionImageContent 저장")
  void saveTest() {
    //GIVEN
    OpinionImageContent opinionImageContent = getNotPersistOpinionImgContent("objectKeyName");

    //WHEN
    opinionImageContentRepository.save(opinionImageContent);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 저장이 잘 되었는지
        () -> assertSame(opinionImageContent, em.find(OpinionImageContent.class, opinionImageContent.getId())),
        //성공 케이스 - 2 - Flush가 가능한지
        () -> assertDoesNotThrow(() -> em.flush())
    );
  }

  @Test
  @DisplayName("ID 로 OpinionImageContent 조회")
  void findByIdTest() {
    //GIVEN
    String objectKeyName = "my object key";
    OpinionImageContent opinionImgContent = getNotPersistOpinionImgContent(objectKeyName);
    em.persist(opinionImgContent);
    long opinionImgContentId = opinionImgContent.getId();

    //WHEN
    Optional<OpinionImageContent> result = opinionImageContentRepository.findById(opinionImgContentId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 조회되었는지
        () -> assertTrue(result.isPresent()),
        //성공 케이스 - 2 - 조회된 엔티티가 올바른지
        () -> assertSame(opinionImgContent, result.get()),
        //성공 케이스 - 3 - Flush
        () -> assertDoesNotThrow(() -> em.flush())
    );
  }

  @Test
  @DisplayName("Object Key 로 조회")
  void findByObjectKeyNameTest() {
    //GIVEN
    String objectKeyName = "my object key";
    OpinionImageContent opinionImgContent = getNotPersistOpinionImgContent(objectKeyName);
    em.persist(opinionImgContent);

    //WHEN
    Optional<OpinionImageContent> result = opinionImageContentRepository.findByObjectKeyName(objectKeyName);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 조회되었는지
        () -> assertTrue(result.isPresent()),
        //성공 케이스 - 2 - 조회된 엔티티가 올바른지
        () -> assertSame(opinionImgContent, result.get())
    );
  }

  @Test
  @DisplayName("엔티티 제거")
  void removeTest() {
    //GIVEN
    String objectKeyName = "my object key";
    OpinionImageContent opinionImgContent = getNotPersistOpinionImgContent(objectKeyName);
    em.persist(opinionImgContent);

    //WHEN

    //THEN
    assertAll(
        //성공 케이스 - 1 - 예외없이 제거가 되는지
        () -> assertDoesNotThrow(() -> opinionImageContentRepository.remove(opinionImgContent)),
        //성공 케이스 - 2 - Flush
        () -> assertDoesNotThrow(() -> em.flush())
    );
  }

  /**
   * 영속화되지 않은 OpinionImageContent 엔티티를 반환하는 메서드
   * @param objectKeyName
   * @return
   */
  private OpinionImageContent getNotPersistOpinionImgContent(String objectKeyName) {
    OpinionImageContent opinionImageContent = OpinionImageContent.builder()
        .imageContentType(OpinionImageContent.ImageContentType.GENERAL)
        .objectKeyName(objectKeyName)
        .opinion(saveOpinion())
        .build();
    return opinionImageContent;
  }

  /**
   * 영속화된 Opinion 엔티티를 반환하는 메서드
   * @return
   */
  private Opinion saveOpinion() {
    User author = User.builder()
        .accountId("author")
        .password("password")
        .nickname("author")
        .userRole(UserRole.TRAVELER)
        .build();
    em.persist(author);
    TravelOn travelOn = saveTravelOn(author);
    Place place = savePlace(travelOn.getRegion());
    Opinion opinion = Opinion.builder()
        .travelOn(travelOn)
        .author(travelOn.getAuthor()) //테스트용으로 여행On 작성자가 답변을 달았다고 가정
        .region(travelOn.getRegion())
        .place(place)
        .facilityCleanliness(EvaluationDegree.GOOD)
        .canParking(true)
        .waiting(false)
        .countAccept(0)
        .costPerformance(EvaluationDegree.GOOD)
        .build();

    opinion.setTravelOn(travelOn);

    em.persist(opinion);

    return opinion;
  }

  /**
   * 새 Place 엔티티를 저장하는 메서드
   * @param region 새 Place의 Region
   * @return
   */
  private Place savePlace(Region region) {
    Place place = Place.builder()
        .id(1L)
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
   * 새 TravelOn 엔티티를 저장하는 메서드
   * @param author
   * @return
   */
  private TravelOn saveTravelOn(User author) {
    TravelOn travelOn;
    String title = "testTitle";
    LocalDate travelStartDate = LocalDate.now().plusMonths(1);
    LocalDate travelEndDate = LocalDate.now().plusMonths(1).plusDays(3);
    String description = "test description";
    TransportationType transportationType = TransportationType.OWN_CAR;
    int accommodationMaxCost = 100000;
    int foodMaxCost = 100000;
    int drinkMaxCost = 100000;

    Region region = getPersistRegion("stateA", "cityA");

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