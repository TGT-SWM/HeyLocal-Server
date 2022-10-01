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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import({PlaceRepository.class})
@DataJpaTest
class PlaceRepositoryTest {
  @PersistenceContext
  private EntityManager em;
  @Autowired
  private PlaceRepository placeRepository;

  @Test
  @DisplayName("장소 저장")
  void saveTest() {
    //GIVEN
    long placeId = 1L;
    Place notPersistPlace = getNotPersistPlace(placeId);

    //WHEN
    placeRepository.save(notPersistPlace);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 저장한 Place 조회
        () -> assertSame(notPersistPlace, em.find(Place.class, placeId)),
        //성공 케이스 - 2 - Flush
        () -> assertDoesNotThrow(() -> em.flush())
    );
  }

  @Test
  @DisplayName("id 로 장소 조회")
  void findBuyIdTest() {
    //GIVEN
    long placeId = 1L;
    Place place = getPersistPlace(placeId);

    //WHEN
    Optional<Place> result = placeRepository.findById(placeId);

    //THEN
    assertAll(
        //성공 케이스 - 1 - 조회가 되었는지
        () -> assertTrue(result.isPresent()),
        //성공 케이스 - 2 - 조회된 엔티티가 같은 것인지
        () -> assertSame(place, result.get())
    );
  }

  @Test
  @DisplayName("관련 답변 개수 내림차순 정렬하여 조회")
  void findPlaceOrderByOpinionSizeDescTest() {
    //GIVEN
    User author = User.builder().accountId("myAccount").nickname("myNickname").password("myPassword").userRole(UserRole.TRAVELER).build();
    em.persist(author);
    TravelOn travelOn = saveTravelOn(
        author,
        "myState", "myCity"
    );
    Place placeWithTwoOpinion = getPersistPlace(1L);
    Opinion opinion1 = saveOpinion(travelOn, placeWithTwoOpinion);
    Opinion opinion2 = saveOpinion(travelOn, placeWithTwoOpinion);
    placeWithTwoOpinion.addOpinion(opinion1);
    placeWithTwoOpinion.addOpinion(opinion2);

    Place placeWithOneOpinion = getPersistPlace(2L);
    Opinion opinion3 = saveOpinion(travelOn, placeWithOneOpinion);
    placeWithOneOpinion.addOpinion(opinion3);

    //WHEN
    List<Place> result = placeRepository.findPlaceOrderByOpinionSizeDesc(2);

    //THEN
    assertAll(
        //성공 케이스 - 답변 개수 내림차순 정렬
        () -> assertSame(2, result.size()),
        () -> assertSame(placeWithTwoOpinion, result.get(0)),
        () -> assertSame(placeWithOneOpinion, result.get(1))
    );
  }

  /**
   * 영속화되지 않은 새 Opinion 엔티티를 반환하는 메서드
   * @param travelOn Opinion 이 추가될 TravelOn 엔티티
   * @param place 관련 장소
   * @return
   */
  private Opinion saveOpinion(TravelOn travelOn, Place place) {
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
   * 영속화되지 않은 새 Place 엔티티를 반환하는 메서드
   * @param id 새 Place 엔티티의 ID
   * @return
   */
  private Place getNotPersistPlace(long id) {
    PlaceCategory category = PlaceCategory.AD5;
    String name = "myPlace";
    String roadAddress = "myRoadAddress";
    String address = "myAddress";
    double lat = 10;
    double lng = 10;
    String link = "myLink";
    Region region = Region.builder()
        .state("myState")
        .city("myCity")
        .build();

    em.persist(region);

    return Place.builder()
        .id(id)
        .category(category)
        .name(name)
        .roadAddress(roadAddress)
        .address(address)
        .lat(lat)
        .lng(lng)
        .link(link)
        .region(region)
        .build();
  }

  /**
   * 영속화된 새 Place 엔티티를 반환하는 메서드
   * @param id 새 Place 엔티티의 ID
   * @return
   */
  private Place getPersistPlace(long id) {
    Place place = getNotPersistPlace(id);
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