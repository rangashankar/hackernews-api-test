package com.hackernews.tests;

import com.hackernews.client.HackerNewsAPIClient;
import com.hackernews.model.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for HackerNews API Edge Cases
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("HackerNews API Edge Cases Tests")
public class EdgeCasesTest {

    private static final Logger logger = LoggerFactory.getLogger(EdgeCasesTest.class);
    private static HackerNewsAPIClient apiClient;

    @BeforeAll
    static void setUp() {
        apiClient = HackerNewsAPIClient.getInstance();
        logger.info("Starting HackerNews API Edge Cases Tests");
    }

    @Test
    @Order(1)
    @DisplayName("Test 1: Handle non-existent story ID")
    void testNonExistentStoryId() {
        logger.info("Test 1: Testing non-existent story ID");

        // Arrange - Use a very large, unlikely ID
        Long nonExistentId = 999999999999L;

        // Act
        Response response = apiClient.getItemResponse(nonExistentId);

        // Assert
        assertThat(response.statusCode())
                .as("Response should be 200 even for non-existent ID")
                .isEqualTo(200);

        assertThat(response.asString())
                .as("Response body should be 'null' for non-existent ID")
                .isEqualTo("null");

        logger.info("Non-existent story ID handled correctly");
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Handle invalid story ID (zero)")
    void testZeroStoryId() {
        logger.info("Test 2: Testing zero story ID");

        // Arrange
        Long zeroId = 0L;

        // Act
        Response response = apiClient.getItemResponse(zeroId);

        // Assert
        assertThat(response.statusCode())
                .as("Response should be 200 for zero ID")
                .isEqualTo(200);

        logger.info("Zero story ID handled correctly");
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Handle deleted or dead story")
    void testDeletedStory() {
        logger.info("Test 3: Testing deleted story handling");

        // Arrange - Find stories with 'dead' or 'deleted' status
        List<Long> topStories = apiClient.getTopStories();
        Story deadStory = null;

        for (Long storyId : topStories) {
            Story story = apiClient.getStoryById(storyId);
            if (story != null && story.getDead() != null && story.getDead()) {
                deadStory = story;
                break;
            }
        }

        if (deadStory != null) {
            assertThat(deadStory.getDead())
                    .as("Dead story should have dead flag set to true")
                    .isTrue();
            logger.info("Found and validated dead story: {}", deadStory.getId());
        } else {
            logger.info("No dead stories found in current top stories");
        }
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Handle story without comments")
    void testStoryWithoutComments() {
        logger.info("Test 4: Testing story without comments");

        // Arrange - Find a story without comments
        List<Long> topStories = apiClient.getTopStories();
        Story storyWithoutComments = null;

        for (Long storyId : topStories) {
            Story story = apiClient.getStoryById(storyId);
            if (story != null && (story.getKids() == null || story.getKids().isEmpty())) {
                storyWithoutComments = story;
                break;
            }
        }

        if (storyWithoutComments != null) {
            assertThat(storyWithoutComments.getKids())
                    .as("Story without comments should have null or empty kids list")
                    .satisfiesAnyOf(
                            kids -> assertThat(kids).isNull(),
                            kids -> assertThat(kids).isEmpty()
                    );
            logger.info("Found and validated story without comments: {}", storyWithoutComments.getId());
        } else {
            logger.info("All top stories have comments");
        }
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Handle story without URL")
    void testStoryWithoutUrl() {
        logger.info("Test 5: Testing story without URL (Ask HN, Show HN)");

        // Arrange - Find a story without URL (typically Ask HN or Show HN)
        List<Long> topStories = apiClient.getTopStories();
        Story storyWithoutUrl = null;

        for (Long storyId : topStories) {
            Story story = apiClient.getStoryById(storyId);
            if (story != null && (story.getUrl() == null || story.getUrl().isEmpty())) {
                storyWithoutUrl = story;
                break;
            }
        }

        if (storyWithoutUrl != null) {
            assertThat(storyWithoutUrl.getUrl())
                    .as("Story without URL should have null or empty URL")
                    .satisfiesAnyOf(
                            url -> assertThat(url).isNull(),
                            url -> assertThat(url).isEmpty()
                    );

            assertThat(storyWithoutUrl.getText())
                    .as("Story without URL should typically have text content")
                    .isNotNull();

            logger.info("Found and validated story without URL: {}", storyWithoutUrl.getTitle());
        } else {
            logger.info("All top stories have URLs");
        }
    }

    @Test
    @Order(6)
    @DisplayName("Test 6: Verify max item ID is valid")
    void testMaxItemId() {
        logger.info("Test 6: Testing max item ID");

        // Act
        Long maxItemId = apiClient.getMaxItemId();

        // Assert
        assertThat(maxItemId)
                .as("Max item ID should not be null")
                .isNotNull();

        assertThat(maxItemId)
                .as("Max item ID should be positive")
                .isPositive();

        assertThat(maxItemId)
                .as("Max item ID should be greater than 1 million")
                .isGreaterThan(1_000_000L);

        logger.info("Max item ID is valid: {}", maxItemId);
    }

    @Test
    @Order(7)
    @DisplayName("Test 7: Compare different story lists")
    void testDifferentStoryLists() {
        logger.info("Test 7: Comparing top, new, and best stories");

        // Act
        List<Long> topStories = apiClient.getTopStories();
        List<Long> newStories = apiClient.getNewStories();
        List<Long> bestStories = apiClient.getBestStories();

        // Assert
        assertThat(topStories)
                .as("Top stories should be different from new stories")
                .isNotEqualTo(newStories);

        assertThat(topStories)
                .as("Top stories should be different from best stories")
                .isNotEqualTo(bestStories);

        assertThat(newStories)
                .as("New stories should be different from best stories")
                .isNotEqualTo(bestStories);

        logger.info("Different story lists validated: Top={}, New={}, Best={}",
                topStories.size(), newStories.size(), bestStories.size());
    }

    @Test
    @Order(8)
    @DisplayName("Test 8: Verify story timestamp is reasonable")
    void testStoryTimestamp() {
        logger.info("Test 8: Testing story timestamp validity");

        // Arrange
        List<Long> topStories = apiClient.getTopStories();
        Story story = apiClient.getStoryById(topStories.get(0));

        // Act
        Long storyTime = story.getTime();
        Long currentTime = System.currentTimeMillis() / 1000; // Convert to seconds
        Long oneYearAgo = currentTime - (365 * 24 * 60 * 60);

        // Assert
        assertThat(storyTime)
                .as("Story timestamp should not be in the future")
                .isLessThanOrEqualTo(currentTime);

        assertThat(storyTime)
                .as("Story timestamp should be within the last year")
                .isGreaterThan(oneYearAgo);

        logger.info("Story timestamp is valid: {}", storyTime);
    }

    @Test
    @Order(9)
    @DisplayName("Test 9: Verify story score is non-negative")
    void testStoryScoreRange() {
        logger.info("Test 9: Testing story score range");

        // Arrange
        List<Long> topStories = apiClient.getTopStories();
        int numberOfStoriesToCheck = Math.min(20, topStories.size());

        // Act & Assert
        for (int i = 0; i < numberOfStoriesToCheck; i++) {
            Story story = apiClient.getStoryById(topStories.get(i));

            assertThat(story.getScore())
                    .as("Story score should be non-negative")
                    .isGreaterThanOrEqualTo(0);

            assertThat(story.getScore())
                    .as("Story score should be reasonable (less than 10000)")
                    .isLessThan(10000);
        }

        logger.info("Story scores validated for {} stories", numberOfStoriesToCheck);
    }

    @Test
    @Order(10)
    @DisplayName("Test 10: Verify descendants count matches kids")
    void testDescendantsCount() {
        logger.info("Test 10: Testing descendants count");

        // Arrange
        List<Long> topStories = apiClient.getTopStories();
        Story story = null;

        // Find a story with comments
        for (Long storyId : topStories) {
            story = apiClient.getStoryById(storyId);
            if (story != null && story.getKids() != null && !story.getKids().isEmpty()) {
                break;
            }
        }

        // Assert
        if (story != null && story.getDescendants() != null) {
            assertThat(story.getDescendants())
                    .as("Descendants count should be greater than or equal to direct kids count")
                    .isGreaterThanOrEqualTo(story.getKids().size());

            logger.info("Descendants validation: descendants={}, direct kids={}",
                    story.getDescendants(), story.getKids().size());
        } else {
            logger.info("No suitable story found for descendants validation");
        }
    }

    @AfterAll
    static void tearDown() {
        logger.info("Completed HackerNews API Edge Cases Tests");
    }
}
