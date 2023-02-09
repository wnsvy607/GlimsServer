package com.glimps.glimpsserver.review.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.glimps.glimpsserver.perfume.application.PerfumeService;
import com.glimps.glimpsserver.perfume.domain.Perfume;
import com.glimps.glimpsserver.review.domain.Review;
import com.glimps.glimpsserver.review.dto.ReviewCreateRequest;
import com.glimps.glimpsserver.review.infra.ReviewRepository;
import com.glimps.glimpsserver.user.application.UserService;
import com.glimps.glimpsserver.user.domain.RoleType;
import com.glimps.glimpsserver.user.domain.User;

@SpringBootTest
class ReviewServiceTest {
	private static final String TITLE = "제목입니다.";
	private static final String BODY = "본문입니다.";
	private static final String EXISTS_EMAIL = "exists@email.com";
	private static final Long EXISTS_PERFUME_ID = 3L;
	private static final Long NEW_REVIEW_ID = 1L;
	private static final User EXISTS_USER = User.builder()
		.id(1L)
		.email(EXISTS_EMAIL)
		.nickname("test_nickname")
		.role(RoleType.USER)
		.build();

	private static final Perfume EXISTS_PERFUME = Perfume.builder()
		.id(EXISTS_PERFUME_ID)
		.perfumeName("향수 이름")
		.brand("향수 브랜드")
		.build();

	private final ReviewRepository reviewRepository = mock(ReviewRepository.class);
	private final UserService userService = mock(UserService.class);
	private ReviewService reviewService;
	@MockBean
	private ReviewPhotoService reviewPhotoService;
	@MockBean
	private PerfumeService perfumeService;

	@BeforeEach
	void setUp() {
		reviewService = new ReviewService(reviewRepository, userService, reviewPhotoService, perfumeService);
	}

	@Nested
	@DisplayName("createReview 메서드는")
	class Describe_createReview {
		@Nested
		@DisplayName("사용자가 존재하고, 향수가 존재할 때")
		class Context_when_user_exists_and_perfume_exists {
			@BeforeEach
			void setUp() {

				given(userService.getUser(EXISTS_EMAIL)).willReturn(EXISTS_USER);
				given(perfumeService.getPerfume(EXISTS_PERFUME_ID)).willReturn(EXISTS_PERFUME);
				given(reviewRepository.save(any(Review.class))).will(invocation -> {
					Review source = invocation.getArgument(0);
					return Review.builder()
						.id(NEW_REVIEW_ID)
						.title(source.getTitle())
						.body(source.getBody())
						.overallRating(source.getOverallRating())
						.longevityRating(source.getLongevityRating())
						.sillageRating(source.getSillageRating())
						.user(source.getUser())
						.perfume(source.getPerfume())
						.build();
				});
			}

			@Test
			@DisplayName("리뷰를 생성한다.")
			void It_creates_review() {
				ReviewCreateRequest reviewCreateRequest =
					ReviewCreateRequest.builder()
						.title(TITLE)
						.body(BODY)
						.overallRatings(5.0)
						.longevityRatings(4.5)
						.sillageRatings(4.0)
						.build();

				Review review = reviewService.createReview(reviewCreateRequest, EXISTS_EMAIL);

				assertThat(review.getTitle()).isEqualTo(TITLE);
				assertThat(review.getBody()).isEqualTo(BODY);
				assertThat(review.getOverallRating()).isEqualTo(5.0);
				assertThat(review.getLongevityRating()).isEqualTo(4.5);
				assertThat(review.getSillageRating()).isEqualTo(4.0);
				assertThat(review.getUser()).isEqualTo(EXISTS_USER);
			}
		}
	}
}
