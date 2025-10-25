package com.hackernews.tests;

import com.hackernews.client.HackerNewsAPIClient;
import com.hackernews.model.Comment;
import com.hackernews.model.Story;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for HackerNews Top Stories API
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("HackerNews Top Stories API Tests")
public class TopStoriesTest {

    private static final Logger logger = LoggerFactory.getLogger(TopStoriesTest.class);
    private static HackerNewsAPIClient apiClient;

    @BeforeAll
    static void setUp() {
        apiClient = HackerNewsAPIClient.getInstance();
        logger.info("Starting HackerNews API Tests");
    }

    @Test
    @Order(1)
    @DisplayName("Test 1: Retrieve top stories list")
    void testRetrieveTopStories() {
        logger.info("Test 1: Retrieving top stories list");

        // Act
        List<Long> topStories = apiClient.getTopStories();

        // Assert
        assertThat(topStories)
                .as("Top stories list should not be null")
                .isNotNull();

        assertThat(topStories)
                .as("Top stories list should not be empty")
                .isNotEmpty();

        assertThat(topStories)
                .as("Top stories should contain at least 100 items")
                .hasSizeGreaterThanOrEqualTo(100);

        assertThat(topStories)
                .as("Top stories should contain at most 500 items")
                .hasSizeLessThanOrEqualTo(500);

        assertThat(topStories)
                .as("All story IDs should be positive numbers")
                .allMatch(id -> id > 0);

        assertThat(topStories)
                .as("Story IDs should be unique")
                .doesNotHaveDuplicates();

        logger.info("Successfully retrieved {} top stories", topStories.size());
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Retrieve current top story details")
    void testRetrieveCurrentTopStory() {
        logger.info("Test 2: Retrieving current top story details");

        // Arrange
        List<Long> topStories = apiClient.getTopStories();
        Long topStoryId = topStories.get(0);

        // Act
        Story topStory = apiClient.getStoryById(topStoryId);

        // Assert
        assertThat(topStory)
                .as("Top story should not be null")
                .isNotNull();

        assertThat(topStory.getId())
                .as("Story ID should match the requested ID")
                .isEqualTo(topStoryId);

        assertThat(topStory.getType())
                .as("Top story type should be 'story'")
                .isEqualTo("story");

        assertThat(topStory.getTitle())
                .as("Story title should not be null or empty")
                .isNotBlank();

        assertThat(topStory.getBy())
                .as("Story author should not be null or empty")
                .isNotBlank();

        assertThat(topStory.getTime())
                .as("Story time should be positive")
                .isPositive();

        assertThat(topStory.getScore())
                .as("Story score should not be null")
                .isNotNull();

        assertThat(topStory.getScore())
                .as("Story score should be non-negative")
                .isGreaterThanOrEqualTo(0);

        logger.info("Successfully retrieved top story: {} by {} with score {}",
                topStory.getTitle(), topStory.getBy(), topStory.getScore());
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Retrieve top story with first comment")
    void testRetrieveTopStoryWithFirstComment() {
        logger.info("Test 3: Retrieving top story with first comment");

        // Arrange
        List<Long> topStories = apiClient.getTopStories();
        Story topStory = null;
        Long firstCommentId = null;

        // Find a story with comments
        for (Long storyId : topStories) {
            topStory = apiClient.getStoryById(storyId);
            if (topStory != null && topStory.getKids() != null && !topStory.getKids().isEmpty()) {
                firstCommentId = topStory.getKids().get(0);
                break;
            }
        }

        // Assert story was found with comments
        assertThat(topStory)
                .as("Should find a story with comments")
                .isNotNull();

        assertThat(firstCommentId)
                .as("First comment ID should not be null")
                .isNotNull();

        // Act
        Comment firstComment = apiClient.getCommentById(firstCommentId);

        // Assert comment details
        assertThat(firstComment)
                .as("First comment should not be null")
                .isNotNull();

        assertThat(firstComment.getId())
                .as("Comment ID should match the requested ID")
                .isEqualTo(firstCommentId);

        assertThat(firstComment.getType())
                .as("Comment type should be 'comment'")
                .isEqualTo("comment");

        assertThat(firstComment.getParent())
                .as("Comment parent should be the story ID")
                .isEqualTo(topStory.getId());

        assertThat(firstComment.getBy())
                .as("Comment author should not be null")
                .isNotNull();

        assertThat(firstComment.getTime())
                .as("Comment time should be positive")
                .isPositive();

        logger.info("Successfully retrieved comment {} for story {} by {}",
                firstComment.getId(), topStory.getTitle(), firstComment.getBy());
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Verify top stories are ordered correctly")
    void testTopStoriesOrdering() {
        logger.info("Test 4: Verifying top stories ordering");

        // Arrange
        List<Long> topStories = apiClient.getTopStories();

        // Get first 5 stories for comparison
        Story firstStory = apiClient.getStoryById(topStories.get(0));
        Story secondStory = apiClient.getStoryById(topStories.get(1));

        // Assert
        assertThat(firstStory.getScore())
                .as("First story should have higher or equal score than second story")
                .isGreaterThanOrEqualTo(secondStory.getScore());

        logger.info("Top stories are correctly ordered: #1 score={}, #2 score={}",
                firstStory.getScore(), secondStory.getScore());
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Test pagination - retrieve multiple top stories")
    void testMultipleTopStories() {
        logger.info("Test 5: Testing multiple top stories retrieval");

        // Arrange
        List<Long> topStories = apiClient.getTopStories();
        int numberOfStoriesToTest = Math.min(10, topStories.size());

        // Act & Assert
        for (int i = 0; i < numberOfStoriesToTest; i++) {
            Story story = apiClient.getStoryById(topStories.get(i));

            assertThat(story)
                    .as("Story at position " + i + " should not be null")
                    .isNotNull();

            assertThat(story.getType())
                    .as("Item at position " + i + " should be a story")
                    .isEqualTo("story");
        }

        logger.info("Successfully retrieved and validated {} top stories", numberOfStoriesToTest);
    }

    @AfterAll
    static void tearDown() {
        logger.info("Completed HackerNews API Tests");
    }
}
