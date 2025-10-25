# HackerNews API Test Automation

A comprehensive Java-based test automation framework for the HackerNews Public API using REST Assured, JUnit 5, and AssertJ.

## Table of Contents
- [Overview](#overview)
- [Design Patterns](#design-patterns)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Running Tests](#running-tests)
- [Test Coverage](#test-coverage)
- [Project Structure](#project-structure)
- [Test Results](#test-results)

## Overview

This project provides automated acceptance tests for the [HackerNews API](https://github.com/HackerNews/API), covering:
- Top, New, and Best Stories retrieval
- Story details and metadata validation
- Comment retrieval and validation
- Edge cases and error handling
- Data integrity checks

## Design Patterns

The project implements several design patterns for maintainability and extensibility:

### 1. Singleton Pattern
- **HackerNewsAPIClient**: Single instance for all API interactions
- Ensures consistent configuration across tests
- Reduces resource overhead

### 2. Model-Based Testing
- **Story** and **Comment** models: Type-safe representations of API responses
- Enables compile-time validation
- Improves code readability

### 3. Page Object Pattern (API Context)
- **Client Layer**: Encapsulates all API interactions
- Separates test logic from API implementation
- Makes tests more maintainable

### 4. Fluent Assertions
- **AssertJ**: Provides readable, chainable assertions
- Improves test readability
- Better error messages

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Internet connection (for API access)

## Setup

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd hackernews-api-test
   ```

2. **Verify Java installation**:
   ```bash
   java -version
   ```
   Should show Java 11 or higher

3. **Verify Maven installation**:
   ```bash
   mvn -version
   ```

4. **Install dependencies**:
   ```bash
   mvn clean install
   ```

## Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Class
```bash
# Run only Top Stories tests
mvn test -Dtest=TopStoriesTest

# Run only Edge Cases tests
mvn test -Dtest=EdgeCasesTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=TopStoriesTest#testRetrieveTopStories
```

### Run with Detailed Logging
```bash
mvn test -X
```

### Generate Test Report
```bash
mvn surefire-report:report
```
Report will be available at: `target/site/surefire-report.html`

## Test Coverage

### Core Test Cases

#### 1. Top Stories Tests (`TopStoriesTest.java`)
- ✅ **Test 1**: Retrieve top stories list
  - Validates list is not null or empty
  - Verifies list contains 100-500 items
  - Ensures all IDs are positive
  - Checks for duplicates

- ✅ **Test 2**: Retrieve current top story details
  - Validates story structure
  - Checks required fields (ID, title, author, score)
  - Verifies data types

- ✅ **Test 3**: Retrieve top story with first comment
  - Finds a story with comments
  - Retrieves the first comment
  - Validates comment structure
  - Verifies parent-child relationship

- ✅ **Test 4**: Verify top stories are ordered correctly
  - Compares scores of consecutive stories
  - Validates ranking algorithm

- ✅ **Test 5**: Test pagination - retrieve multiple top stories
  - Validates first 10 stories
  - Ensures consistency across retrievals

#### 2. Edge Cases Tests (`EdgeCasesTest.java`)
- ✅ **Test 1**: Handle non-existent story ID
  - Tests API response for invalid IDs
  - Validates error handling

- ✅ **Test 2**: Handle invalid story ID (zero)
  - Tests boundary conditions
  - Validates API behavior

- ✅ **Test 3**: Handle deleted or dead story
  - Checks 'dead' flag handling
  - Validates deleted content

- ✅ **Test 4**: Handle story without comments
  - Tests stories with no comments
  - Validates null/empty kids list

- ✅ **Test 5**: Handle story without URL
  - Tests Ask HN/Show HN stories
  - Validates text-only content

- ✅ **Test 6**: Verify max item ID is valid
  - Tests max item endpoint
  - Validates ID range

- ✅ **Test 7**: Compare different story lists
  - Compares top, new, and best stories
  - Validates list uniqueness

- ✅ **Test 8**: Verify story timestamp is reasonable
  - Validates timestamp range
  - Checks for future dates

- ✅ **Test 9**: Verify story score is non-negative
  - Validates score range
  - Checks for reasonable values

- ✅ **Test 10**: Verify descendants count matches kids
  - Validates comment hierarchy
  - Checks descendant counting

## Project Structure

```
hackernews-api-test/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── hackernews/
│   │               ├── client/
│   │               │   └── HackerNewsAPIClient.java
│   │               └── model/
│   │                   ├── Story.java
│   │                   └── Comment.java
│   └── test/
│       └── java/
│           └── com/
│               └── hackernews/
│                   └── tests/
│                       ├── TopStoriesTest.java
│                       └── EdgeCasesTest.java
├── pom.xml
├── README.md
└── .gitignore
```

### Key Components

- **HackerNewsAPIClient**: Singleton client for API interactions
- **Story**: Model representing a HackerNews story
- **Comment**: Model representing a comment
- **TopStoriesTest**: Core functional tests
- **EdgeCasesTest**: Boundary and error condition tests

## Test Results

### Expected Outcomes

All tests should pass with the following characteristics:

1. **API Availability**: Tests verify the API is responsive
2. **Data Integrity**: All returned data matches expected schemas
3. **Edge Case Handling**: API gracefully handles invalid inputs
4. **Performance**: API responses are within acceptable timeouts

### Known Issues/Findings

#### Potential Bug Found
**Issue**: Story ordering may not always be strictly by score
- **Test**: `testTopStoriesOrdering()`
- **Finding**: First story score >= second story score (allows equality)
- **Expected**: Strictly decreasing scores
- **Impact**: Minor - doesn't affect core functionality

### Sample Test Output

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.hackernews.tests.TopStoriesTest
[INFO] Test 1: Retrieving top stories list
[INFO] Retrieved 500 top stories
[INFO] Test 2: Retrieving current top story details
[INFO] Successfully retrieved top story: <title> by <author> with score <score>
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] Running com.hackernews.tests.EdgeCasesTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] Results:
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
```

## Technologies Used

- **Java 11**: Programming language
- **Maven**: Build and dependency management
- **JUnit 5**: Testing framework
- **REST Assured**: REST API testing library
- **AssertJ**: Fluent assertion library
- **Jackson**: JSON processing
- **SLF4J + Logback**: Logging framework

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is for educational and testing purposes.

## API Documentation

For more information about the HackerNews API, visit:
- [Official API Documentation](https://github.com/HackerNews/API)
- [API Endpoint](https://hacker-news.firebaseio.com/v0/)

## Contact

For questions or issues, please open an issue in the repository.
