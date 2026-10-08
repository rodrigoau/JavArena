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
 * Ejecuta todos los casos definidos en los JSON de la carpeta fixtures.
 *
 * Estructura de cada fixture:
 * {
 *   "endpoint": "/ruta",                  // default para todos los casos del archivo
 *   "method": "GET",                      // opcional, default GET
 *   "headers": { ... },                   // opcional, default para todos los casos
 *   "nombreDelCaso": {
 *     "description": "...",               // opcional
 *     "endpoint": "/otra-ruta",           // opcional, sobreescribe el del archivo
 *     "method": "POST",                   // opcional, sobreescribe el del archivo
 *     "pathParams":  { ... },             // opcional
 *     "queryParams": { ... },             // opcional
 *     "headers":     { ... },             // opcional, se mezcla con los del archivo
 *     "body":        { ... },             // opcional, request body
 *     "expectedStatus": 200,              // opcional, default 200
 *     "ignoreFields": ["requestId"],      // opcional, rutas (con puntos) que no se comparan
 *     "expectedResults": { ... }          // JSON esperado del response
 *   }
 * }
 *
 * La comparación valida que cada campo declarado en expectedResults exista y coincida en el
 * response real; los arrays deben tener el mismo tamaño y se comparan elemento por elemento.
 */
@Epic("Módulo API REST")
@Feature("Ejecución Dinámica desde Fixtures JSON")
public class TemplateApiTest extends BaseApiTest {

    private static final String FIXTURES_PATH = "src/test/java/com/rodrigo/tests/api/fixtures";
    private static final Set<String> FILE_LEVEL_KEYS = Set.of("endpoint", "method", "headers");
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Cada caso de cada archivo .json en fixtures es una ejecución independiente del test
    @DataProvider(name = "fixtureCasesProvider")
    public Iterator<Object[]> provideFixtureCases() throws IOException {
        List<Object[]> testCases = new ArrayList<>();

        File[] files = new File(FIXTURES_PATH).listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
        Assert.assertNotNull(files, "No se encontró la carpeta de fixtures: " + new File(FIXTURES_PATH).getAbsolutePath());
        Arrays.sort(files, Comparator.comparing(File::getName));

        for (File file : files) {
            JsonNode fixture = MAPPER.readTree(file);
            for (Iterator<Map.Entry<String, JsonNode>> it = fixture.fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> entry = it.next();
                if (FILE_LEVEL_KEYS.contains(entry.getKey()) || !entry.getValue().has("expectedResults")) {
                    continue;
                }
                testCases.add(new Object[]{file.getName(), entry.getKey(), fixture, entry.getValue()});
            }
        }

        return testCases.iterator();
    }

    @Test(dataProvider = "fixtureCasesProvider")
    @Story("Pruebas automatizadas basadas en archivos de fixtures")
    @Description("Ejecuta peticiones HTTP dinámicas leyendo cada caso de los archivos JSON en la carpeta fixtures y compara el response con expectedResults.")
    public void testDynamicApiFromFixture(String fileName, String caseName, JsonNode fixture, JsonNode testCase) {
        String testId = fileName + " -> " + caseName;
        Allure.getLifecycle().updateTestCase(result -> result.setName(testId));
        System.out.println("Ejecutando caso: " + testId);

        // 1. Construir el request a partir del fixture (el caso sobreescribe los valores del archivo)
        String endpoint = textOrDefault(testCase, "endpoint", textOrDefault(fixture, "endpoint", null));
        Assert.assertNotNull(endpoint, "El caso " + testId + " no define 'endpoint'");
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

        // 2. Ejecutar la petición HTTP
        Response response = request.when().request(method, endpoint);

        // 3. Validar status code
        Assert.assertEquals(response.getStatusCode(), expectedStatus,
                "Fallo en " + testId + ": código de estado HTTP inesperado");

        // 4. Comparar el response contra expectedResults
        JsonNode actual;
        try {
            actual = MAPPER.readTree(response.getBody().asString());
        } catch (IOException e) {
            Assert.fail("Error al parsear la respuesta JSON en " + testId + ": " + e.getMessage());
            return;
        }

        JsonNode expected = testCase.get("expectedResults").deepCopy();
        Set<String> ignoreFields = new HashSet<>();
        testCase.path("ignoreFields").forEach(f -> ignoreFields.add(f.asText()));

        List<String> differences = new ArrayList<>();
        compare("$", expected, actual, ignoreFields, differences);

        if (!differences.isEmpty()) {
            String report = String.join("\n", differences);
            System.out.println("--- DIFERENCIAS EN " + testId + " ---\n" + report);
            Allure.addAttachment("Diferencias", report);
            Allure.addAttachment("Esperado", "application/json", expected.toPrettyString());
            Allure.addAttachment("Recibido", "application/json", actual.toPrettyString());
        }

        Assert.assertTrue(differences.isEmpty(),
                "El response no coincide con expectedResults en " + testId + ":\n" + String.join("\n", differences));
    }

    /**
     * Compara recursivamente: cada campo de 'expected' debe existir y coincidir en 'actual'.
     * Las rutas se reportan como $.data[0].name; ignoreFields usa la misma ruta sin "$." ni índices (data.name).
     */
    private void compare(String path, JsonNode expected, JsonNode actual, Set<String> ignoreFields, List<String> differences) {
        if (ignoreFields.contains(normalize(path))) {
            return;
        }
        if (actual == null || actual.isMissingNode()) {
            differences.add(path + ": falta en el response (esperado " + expected + ")");
            return;
        }
        if (expected.isObject()) {
            if (!actual.isObject()) {
                differences.add(path + ": se esperaba un objeto, se recibió " + actual);
                return;
            }
            expected.fields().forEachRemaining(e ->
                    compare(path + "." + e.getKey(), e.getValue(), actual.get(e.getKey()), ignoreFields, differences));
        } else if (expected.isArray()) {
            if (!actual.isArray()) {
                differences.add(path + ": se esperaba un array, se recibió " + actual);
                return;
            }
            if (expected.size() != actual.size()) {
                differences.add(path + ": tamaño esperado " + expected.size() + ", recibido " + actual.size());
            }
            for (int i = 0; i < Math.min(expected.size(), actual.size()); i++) {
                compare(path + "[" + i + "]", expected.get(i), actual.get(i), ignoreFields, differences);
            }
        } else if (expected.isNumber() && actual.isNumber()) {
            if (expected.decimalValue().compareTo(actual.decimalValue()) != 0) {
                differences.add(path + ": esperado " + expected + ", recibido " + actual);
            }
        } else if (!expected.equals(actual)) {
            differences.add(path + ": esperado " + expected + ", recibido " + actual);
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
