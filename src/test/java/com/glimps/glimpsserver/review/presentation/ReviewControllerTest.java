package com.glimps.glimpsserver.review.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.uuid.Generators;
import com.glimps.glimpsserver.common.config.SecurityConfig;
import com.glimps.glimpsserver.review.application.ReviewService;
import com.glimps.glimpsserver.review.domain.Review;
import com.glimps.glimpsserver.review.domain.ReviewPhoto;
import com.glimps.glimpsserver.review.dto.ReviewCreateRequest;
import com.glimps.glimpsserver.session.application.AuthenticationService;

@WebMvcTest(ReviewController.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ReviewController.class, SecurityConfig.class})
class ReviewControllerTest {
	private static final String TITLE = "제목입니다.";
	private static final String BODY = "본문입니다.";
	private static final String EXISTS_EMAIL = "exists@email.com";
	private static final String NOT_EXISTS_EMAIL = "notexists@email.com";
	private static final Long EXISTS_PERFUME_ID = 3L;
	private static final Long NOT_EXISTS_PERFUME_ID = 200L;
	private static final Long NEW_REVIEW_ID = 1L;
	private static final UUID NEW_REVIEW_UUID = UUID.randomUUID();
	private static final Long EXISTS_REVIEW_ID = 3L;
	private static final UUID EXISTS_REVIEW_UUID = UUID.randomUUID();
	private static final UUID NOT_EXISTS_REVIEW_UUID = UUID.randomUUID();
	@MockBean
	private ReviewService reviewService;

	@MockBean
	private AuthenticationService authenticationService;

	@Autowired
	private MockMvc mvc;

	@Nested
	@DisplayName("POST /reviews")
	class Describe_create {

		@Nested
		@DisplayName("valid `ReviewCreateRequest`라면")
		class Context_with_valid_request {
			private final ReviewCreateRequest reviewCreateRequest = ReviewCreateRequest.builder()
				.title("testTitle")
				.body("testBody")
				.overallRatings(5.0)
				.longevityRatings(4.5)
				.sillageRatings(4.0)
				.photoUrls(List.of("url1", "url2"))
				.perfumeUuid(Generators.timeBasedGenerator().generate())
				.build();
			private final ReviewPhoto REVIEW_PHOTO1 = ReviewPhoto.builder()
				.id(1L)
				.url("url1")
				.build();

			private final ReviewPhoto REVIEW_PHOTO2 = ReviewPhoto.builder()
				.id(1L)
				.url("url2")
				.build();

			@BeforeEach
			void setUp() {
				Review createdReview = Review.builder()
					.id(1L)
					.title("testTitle")
					.body("testBody")
					.overallRatings(5.0)
					.longevityRatings(4.5)
					.sillageRatings(4.0)
					.reviewPhotos(List.of(REVIEW_PHOTO1, REVIEW_PHOTO2))
					.build();

				given(reviewService.createReview(reviewCreateRequest, EXISTS_EMAIL)).willReturn(createdReview);

			}

			@Test
			@WithMockUser(username = "user1", password = "1111", roles = "USER")
			@DisplayName("생성된 리뷰와 201을 반환한다.")
			void it_returns_created_review_and_201() throws Exception {
				mvc.perform(post("/reviews")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(
							"{\"title\":\"testTitle\",\"body\":\"testBody\",\"overallRatings\":5.0,\"longevityRatings\":4.5,"
								+ "\"sillageRatings\":\"4.0\",\"photosUrls\":[\"url1\",\"url2\"], \"hashTagsIds\": [1,2], \"perfumeId\": 1}"))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.photos[0].url").value("url1"));
			}
		}
	}

	@Nested
	@DisplayName("POST /reviews/{id}/heart")
	class Describe_heart {
		private final UUID REVIEW_ID = UUID.randomUUID();

		@Nested
		@DisplayName("존재하는 리뷰라면")
		class Context_with_valid_request {
			private final Review HEART_UPDATED_REVIEW = Review.builder()
				.id(1L)
				.uuid(UUID.randomUUID())
				.reviewPhotos(List.of())
				.title(TITLE)
				.body(BODY)
				.heartsCnt(1)
				.build();

			@BeforeEach
			void setUp() {
				given(reviewService.createHeart(REVIEW_ID, EXISTS_EMAIL)).willReturn(HEART_UPDATED_REVIEW);
			}

			@Test
			@WithMockUser(username = "user1", password = "1111", roles = "USER")
			@DisplayName("true를 반환한다.")
			void it_returns_true() throws Exception {
				mvc.perform(post("/reviews/{id}/heart", REVIEW_ID))
					.andExpect(status().isOk())
					.andExpect(content().string("true"));
			}
		}
	}

}
