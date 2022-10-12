/**
 * packageName    : com.heylocal.traveler.mapper.context
 * fileName       : S3UrlOpinionContext
 * author         : 우태균
 * date           : 2022/10/04
 * description    : 여행On의 답변에 관련 이미지의 S3 접근 URL을 바인딩하는 Mapping Context
 */

package com.heylocal.traveler.mapper.context;

import com.amazonaws.HttpMethod;
import com.heylocal.traveler.domain.travelon.opinion.Opinion;
import com.heylocal.traveler.domain.travelon.opinion.OpinionImageContent;
import com.heylocal.traveler.domain.user.User;
import com.heylocal.traveler.util.aws.S3ObjectNameFormatter;
import com.heylocal.traveler.util.aws.S3PresignUrlProvider;
import lombok.RequiredArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import static com.heylocal.traveler.dto.OpinionDto.OpinionResponse;
import static com.heylocal.traveler.dto.OpinionDto.OpinionWithPlaceResponse;
import static com.heylocal.traveler.dto.UserDto.UserProfileResponse;

@Component
@RequiredArgsConstructor
public class S3UrlOpinionContext {
  private final S3UrlUserContext s3UrlUserContext;
  private final S3ObjectNameFormatter s3ObjectNameFormatter;
  private final S3PresignUrlProvider s3PresignUrlProvider;

  @AfterMapping
  public void bindS3DownloadUrl(@MappingTarget OpinionWithPlaceResponse responseDto, Opinion opinion) {
    UserProfileResponse authorDto = responseDto.getAuthor();
    User authorEntity = opinion.getAuthor();

    //답변 작성자의 프로필 이미지 Download URL 바인딩
    s3UrlUserContext.bindS3DownloadUrl(authorDto, authorEntity);

    //답변의 이미지 Download URL 바인딩
    List<OpinionImageContent> sortedImgEntityList = sortImgEntityByKeyIndex(opinion.getOpinionImageContentList());
    sortedImgEntityList.stream().forEach( (imgEntity) -> bindDownloadUrls(responseDto, imgEntity) );
  }

  /**
   * <pre>
   * OpinionImageContent 리스트를 오브젝트 키의 OBJECT_INDEX 오름차순으로 정렬하는 메서드
   * </pre>
   * @param imgEntityList 정렬할 OpinionImageContent 엔티티 리스트
   * @return
   */
  private List<OpinionImageContent> sortImgEntityByKeyIndex(List<OpinionImageContent> imgEntityList) {
    //OpinionImageContent.objectKeyName 값에서 index에 해당하는 값의 오름차순으로 정렬하는 Priority-Set
    Queue<OpinionImageContent> queue = new PriorityQueue<>(Comparator.comparing((opinionImageContent) -> {
      String objectKey = opinionImageContent.getObjectKeyName();
      String index = s3ObjectNameFormatter.parseObjectNameOfOpinionImg(objectKey)
          .get(S3ObjectNameFormatter.ObjectNameProperty.OBJECT_INDEX);
      return index;
    }));

    imgEntityList.stream().forEach(queue::offer);

    return queue.stream().collect(Collectors.toList());
  }

  /**
   * OpinionResponse DTO 객체에 다운로드 Presigned URL 을 바인딩하는 메서드
   * @param targetDto 바인딩 대상
   * @param opinionImageContent 바인딩할 데이터를 가지고 있는 엔티티
   */
  private void bindDownloadUrls(OpinionResponse targetDto, OpinionImageContent opinionImageContent) {
    List<String> generalImgDownloadImgUrl = targetDto.getGeneralImgDownloadImgUrl();
    List<String> foodImgDownloadImgUrl = targetDto.getFoodImgDownloadImgUrl();
    List<String> drinkAndDessertImgDownloadImgUrl = targetDto.getDrinkAndDessertImgDownloadImgUrl();
    List<String> photoSpotImgDownloadImgUrl = targetDto.getPhotoSpotImgDownloadImgUrl();

    String objectKeyName = opinionImageContent.getObjectKeyName();
    String presignedUrl = s3PresignUrlProvider.getPresignedUrl(objectKeyName, HttpMethod.GET);
    OpinionImageContent.ImageContentType imageContentType = opinionImageContent.getImageContentType();

    switch (imageContentType) {
      case GENERAL:
        generalImgDownloadImgUrl.add(presignedUrl);
        break;
      case RECOMMEND_FOOD:
        foodImgDownloadImgUrl.add(presignedUrl);
        break;
      case RECOMMEND_DRINK_DESSERT:
        drinkAndDessertImgDownloadImgUrl.add(presignedUrl);
        break;
      case PHOTO_SPOT:
        photoSpotImgDownloadImgUrl.add(presignedUrl);
        break;
    }
  }
}
