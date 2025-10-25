package com.hackernews.client;

import com.hackernews.model.Comment;
import com.hackernews.model.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * Client class for interacting with HackerNews API
 * Implements Singleton and Builder patterns
 */
public class HackerNewsAPIClient {

    private static final Logger logger = LoggerFactory.getLogger(HackerNewsAPIClient.class);
    private static final String BASE_URL = "https://hacker-news.firebaseio.com/v0";

    private static HackerNewsAPIClient instance;

    // Private constructor for Singleton pattern
    private HackerNewsAPIClient() {
        RestAssured.baseURI = BASE_URL;
    }

    /**
     * Get singleton instance of the API client
     * @return HackerNewsAPIClient instance
     */
    public static synchronized HackerNewsAPIClient getInstance() {
        if (instance == null) {
            instance = new HackerNewsAPIClient();
        }
        return instance;
    }

    /**
     * Get top stories IDs
     * @return List of story IDs
     */
    public List<Long> getTopStories() {
        logger.info("Fetching top stories");

        Response response = given()
                .contentType("application/json")
                .when()
                .get("/topstories.json")
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Long> storyIds = response.jsonPath().getList("$", Long.class);
        logger.info("Retrieved {} top stories", storyIds.size());

        return storyIds;
    }

    /**
     * Get new stories IDs
     * @return List of story IDs
     */
    public List<Long> getNewStories() {
        logger.info("Fetching new stories");

        Response response = given()
                .contentType("application/json")
                .when()
                .get("/newstories.json")
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Long> storyIds = response.jsonPath().getList("$", Long.class);
        logger.info("Retrieved {} new stories", storyIds.size());

        return storyIds;
    }

    /**
     * Get best stories IDs
     * @return List of story IDs
     */
    public List<Long> getBestStories() {
        logger.info("Fetching best stories");

        Response response = given()
                .contentType("application/json")
                .when()
                .get("/beststories.json")
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Long> storyIds = response.jsonPath().getList("$", Long.class);
        logger.info("Retrieved {} best stories", storyIds.size());

        return storyIds;
    }

    /**
     * Get story by ID
     * @param storyId Story ID
     * @return Story object
     */
    public Story getStoryById(Long storyId) {
        logger.info("Fetching story with ID: {}", storyId);

        Response response = given()
                .contentType("application/json")
                .when()
                .get("/item/" + storyId + ".json")
                .then()
                .extract()
                .response();

        if (response.statusCode() == 200) {
            Story story = response.as(Story.class);
            logger.info("Retrieved story: {}", story);
            return story;
        } else {
            logger.warn("Failed to retrieve story {}. Status: {}", storyId, response.statusCode());
            return null;
        }
    }

    /**
     * Get comment by ID
     * @param commentId Comment ID
     * @return Comment object
     */
    public Comment getCommentById(Long commentId) {
        logger.info("Fetching comment with ID: {}", commentId);

        Response response = given()
                .contentType("application/json")
                .when()
                .get("/item/" + commentId + ".json")
                .then()
                .extract()
                .response();

        if (response.statusCode() == 200) {
            Comment comment = response.as(Comment.class);
            logger.info("Retrieved comment: {}", comment);
            return comment;
        } else {
            logger.warn("Failed to retrieve comment {}. Status: {}", commentId, response.statusCode());
            return null;
        }
    }

    /**
     * Get raw response for an item
     * @param itemId Item ID
     * @return Response object
     */
    public Response getItemResponse(Long itemId) {
        logger.info("Fetching item with ID: {}", itemId);

        return given()
                .contentType("application/json")
                .when()
                .get("/item/" + itemId + ".json")
                .then()
                .extract()
                .response();
    }

    /**
     * Get max item ID
     * @return Maximum item ID
     */
    public Long getMaxItemId() {
        logger.info("Fetching max item ID");

        Response response = given()
                .contentType("application/json")
                .when()
                .get("/maxitem.json")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Long maxId = response.as(Long.class);
        logger.info("Max item ID: {}", maxId);

        return maxId;
    }
}
