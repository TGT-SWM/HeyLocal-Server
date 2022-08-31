package com.heylocal.traveler.repository;

import com.heylocal.traveler.domain.Region;
import com.heylocal.traveler.domain.travelon.*;
import com.heylocal.traveler.domain.travelon.list.*;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.domain.user.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Import(TravelOnRepository.class)
@DataJpaTest
class TravelOnRepositoryTest {
  @Autowired
  private EntityManager em;
  @Autowired
  private TravelOnRepository travelOnRepository;

  @Test
  @DisplayName("여행 On 저장 성공")
  void addTravelOnSucceedTest() {
    //GIVEN
    User author = User.builder()
        .accountId("testAccount")
        .nickname("testNickname")
        .password("encodedPassword")
        .userRole(UserRole.TRAVELER)
        .build();
    em.persist(author);
    TravelOn travelOn = getNotPersistedTravelOn(author);

    //WHEN
    travelOnRepository.saveTravelOn(travelOn);

    //THEN
    TravelOn result = em.find(TravelOn.class, travelOn.getId());

    assertAll(
        //성공 케이스 - 1 - SQL Flush 성공
        () -> assertDoesNotThrow(() -> em.flush()),
        //성공 케이스 - 2 - 결과 확인
        () -> assertEquals(travelOn, result)
    );
  }

  @Test
  @DisplayName("여행 On 저장 실패 - 존재하지 않는 유저가 작성한 경우")
  void addTravelOnInvalidAuthorTest() {
    //GIVEN
    User author = null;
    TravelOn travelOn = getNotPersistedTravelOn(author);

    //WHEN
    travelOnRepository.saveTravelOn(travelOn);

    //THEN
    TravelOn result = em.find(TravelOn.class, travelOn.getId());

    //실패 케이스 - 1 - SQL Flush 실패
    assertThrows(Exception.class, () -> em.flush());
  }

  private TravelOn getNotPersistedTravelOn(User author) {
    TravelOn travelOn;
    String title = "testTitle";
    LocalDate travelStartDate = LocalDate.now().plusMonths(1);
    LocalDate travelEndDate = LocalDate.now().plusMonths(1).plusDays(3);
    String description = "test description";
    TransportationType transportationType = TransportationType.OWN_CAR;
    int accommodationMaxCost = 100000;
    int foodMaxCost = 100000;
    int drinkMaxCost = 100000;

    Region region = Region.builder()
        .city("성남시")
        .state("경기도")
        .build();
    em.persist(region);

    travelOn = TravelOn.builder()
        .title(title)
        .region(region)
        .views(1)
        .author(author)
        .travelStartDate(travelStartDate)
        .travelEndDate(travelEndDate)
        .description(description)
        .transportationType(transportationType)
        .accommodationMaxCost(accommodationMaxCost)
        .foodMaxCost(foodMaxCost)
        .drinkMaxCost(drinkMaxCost)
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

    //Ignore
    //em.persist(travelOn);

    return travelOn;
  }
}