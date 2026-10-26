package com.rodrigo.tests.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;

/**
 * Runs every test case defined in the JSON files of the fixtures folder.
 *
 * Structure of each fixture:
 * {
 *   "testName": "Users",                  // optional, default name for the cases in the file
 *   "endpoint": "/path",                  // default for every case in the file
 *   "method": "GET",                      // optional, default GET
 *   "headers": { ... },                   // optional, default for every case
 *   "caseName": {
 *     "testName": "...",                  // optional, result name (default: "file.json -> caseName")
 *     "description": "...",               // optional
 *     "endpoint": "/other-path",          // optional, overrides the file-level value
 *     "method": "POST",                   // optional, overrides the file-level value
 *     "pathParams":  { ... },             // optional
 *     "queryParams": { ... },             // optional
 *     "headers":     { ... },             // optional, merged with the file-level headers
 *     "body":        { ... },             // optional, request body
 *     "expectedStatus": 200,              // optional, default 200
 *     "ignoreFields": ["requestId"],      // optional, dot-notation paths that are not compared
 *     "expectedResults": { ... }          // expected JSON response
 *   }
 * }
 *
 * The comparison checks that every field declared in expectedResults exists and matches in the
 * actual response; arrays must have the same size and are compared element by element.
 */
@Epic("REST API Module")
@Feature("Dynamic Execution from JSON Fixtures")
public class TemplateApiTest extends BaseApiTest implements ITest {

    private static final String FIXTURES_PATH = "src/test/java/com/rodrigo/tests/api/fixtures";
    private static final Set<String> FILE_LEVEL_KEYS = Set.of("testName", "endpoint", "method", "headers");
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final ThreadLocal<String> testName = new ThreadLocal<>();

    // Name shown by TestNG / the IDE in the result: the testName of the current case
    @BeforeMethod(alwaysRun = true)
    public void setTestName(Object[] params) {
        testName.set(params.length > 0 ? String.valueOf(params[0]) : null);
    }

    @Override
    public String getTestName() {
        return testName.get();
    }

    // Each case in each .json file in fixtures is an independent test execution
    @DataProvider(name = "fixtureCasesProvider")
    public Iterator<Object[]> provideFixtureCases() throws IOException {
        List<Object[]> testCases = new ArrayList<>();

        File[] files = new File(FIXTURES_PATH).listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
        Assert.assertNotNull(files, "Fixtures folder not found: " + new File(FIXTURES_PATH).getAbsolutePath());
        Arrays.sort(files, Comparator.comparing(File::getName));

        for (File file : files) {
            JsonNode fixture = MAPPER.readTree(file);
            for (Iterator<Map.Entry<String, JsonNode>> it = fixture.fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> entry = it.next();
                if (FILE_LEVEL_KEYS.contains(entry.getKey()) || !entry.getValue().has("expectedResults")) {
                    continue;
                }
                String testName = textOrDefault(entry.getValue(), "testName",
                        textOrDefault(fixture, "testName", file.getName() + " -> " + entry.getKey()));
                testCases.add(new Object[]{testName, fixture, entry.getValue()});
            }
        }

        return testCases.iterator();
    }

    @Test(dataProvider = "fixtureCasesProvider")
    @Story("Automated tests based on fixture files")
    @Description("Sends dynamic HTTP requests by reading each case from the JSON files in the fixtures folder and compares the response against expectedResults.")
    public void testDynamicApiFromFixture(String testId, JsonNode fixture, JsonNode testCase) {
        Allure.getLifecycle().updateTestCase(result -> result.setName(testId));
        System.out.println("Running test case: " + testId);

        // 1. Build the request from the fixture (the case overrides the file-level values)
        String endpoint = textOrDefault(testCase, "endpoint", textOrDefault(fixture, "endpoint", null));
        Assert.assertNotNull(endpoint, "Test case " + testId + " does not define 'endpoint'");
        String method = textOrDefault(testCase, "method", textOrDefault(fixture, "method", "GET")).toUpperCase();
        int expectedStatus = testCase.path("expectedStatus").asInt(200);

        RequestSpecification request = given().spec(requestSpec);
        addHeaders(request, fixture.get("headers"));
        addHeaders(request, testCase.get("headers"));
        if (testCase.has("pathParams")) {
            testCase.get("pathParams").fields().forEachRemaining(e -> request.pathParam(e.getKey(), e.getValue().asText()));
        }
        if (testCase.has("queryParams")) {
            testCase.get("queryParams").fields().forEachRemaining(e -> request.queryParam(e.getKey(), e.getValue().asText()));
        }
        if (testCase.has("body")) {
            request.body(testCase.get("body").toString());
        }

        // 2. Send the HTTP request
        Response response = request.when().request(method, endpoint);

        // 3. Validate status code
        Assert.assertEquals(response.getStatusCode(), expectedStatus,
                "Failure in " + testId + ": unexpected HTTP status code");

        // 4. Compare the response against expectedResults
        JsonNode actual;
        try {
            actual = MAPPER.readTree(response.getBody().asString());
        } catch (IOException e) {
            Assert.fail("Error parsing JSON response in " + testId + ": " + e.getMessage());
            return;
        }

        JsonNode expected = testCase.get("expectedResults").deepCopy();
        Set<String> ignoreFields = new HashSet<>();
        testCase.path("ignoreFields").forEach(f -> ignoreFields.add(f.asText()));

        List<String> differences = new ArrayList<>();
        compare("$", expected, actual, ignoreFields, differences);

        if (!differences.isEmpty()) {
            String report = String.join("\n", differences);
            System.out.println("--- DIFFERENCES IN " + testId + " ---\n" + report);
            Allure.addAttachment("Differences", report);
            Allure.addAttachment("Expected", "application/json", expected.toPrettyString());
            Allure.addAttachment("Actual", "application/json", actual.toPrettyString());
        }

        Assert.assertTrue(differences.isEmpty(),
                "Response does not match expectedResults in " + testId + ":\n" + String.join("\n", differences));
    }

    /**
     * Recursive comparison: every field in 'expected' must exist and match in 'actual'.
     * Paths are reported as $.data[0].name; ignoreFields uses the same path without "$." or indexes (data.name).
     */
    private void compare(String path, JsonNode expected, JsonNode actual, Set<String> ignoreFields, List<String> differences) {
        if (ignoreFields.contains(normalize(path))) {
            return;
        }
        if (actual == null || actual.isMissingNode()) {
            differences.add(path + ": missing in response (expected " + expected + ")");
            return;
        }
        if (expected.isObject()) {
            if (!actual.isObject()) {
                differences.add(path + ": expected an object, received " + actual);
                return;
            }
            expected.fields().forEachRemaining(e ->
                    compare(path + "." + e.getKey(), e.getValue(), actual.get(e.getKey()), ignoreFields, differences));
        } else if (expected.isArray()) {
            if (!actual.isArray()) {
                differences.add(path + ": expected an array, received " + actual);
                return;
            }
            if (expected.size() != actual.size()) {
                differences.add(path + ": expected size " + expected.size() + ", received " + actual.size());
            }
            for (int i = 0; i < Math.min(expected.size(), actual.size()); i++) {
                compare(path + "[" + i + "]", expected.get(i), actual.get(i), ignoreFields, differences);
            }
        } else if (expected.isNumber() && actual.isNumber()) {
            if (expected.decimalValue().compareTo(actual.decimalValue()) != 0) {
                differences.add(path + ": expected " + expected + ", received " + actual);
            }
        } else if (!expected.equals(actual)) {
            differences.add(path + ": expected " + expected + ", received " + actual);
        }
    }

    private static String normalize(String path) {
        return path.replaceAll("\\[\\d+]", "").replaceFirst("^\\$\\.?", "");
    }

    private static String textOrDefault(JsonNode node, String field, String defaultValue) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? defaultValue : value.asText();
    }

    private static void addHeaders(RequestSpecification request, JsonNode headers) {
        if (headers instanceof ObjectNode) {
            headers.fields().forEachRemaining(e -> request.header(e.getKey(), e.getValue().asText()));
        }
    }
}