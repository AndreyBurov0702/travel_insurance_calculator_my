package org.javaguru.travel.insurance.rest.v1;

import org.javaguru.travel.insurance.rest.common.JsonFileReader;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class TravelCalculatePremiumControllerV1TestCase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
     private JsonFileReader jsonFileReader;

    private static final String BASE_URL = "/insurance/travel/api/v1/";


    protected abstract String getTestCaseFolderName();

    protected void executeAndCompare() throws Exception {
        executeAndCompare(
                "rest/v1/" + getTestCaseFolderName() + "/request.json",
                "rest/v1/" + getTestCaseFolderName() + "/response.json",
                false
        );
    }
    protected void executeAndCompare(boolean ignoreUUIDValue) throws Exception {
        executeAndCompare(
                "rest/v1/" + getTestCaseFolderName() + "/request.json",
                "rest/v1/" + getTestCaseFolderName() + "/response.json",
                ignoreUUIDValue
        );
    }


    protected void executeAndCompare(String jsonRequestFilePath,
                                     String jsonResponseFilePath,
                                     boolean ignoreUUIDValue) throws Exception {
        String jsonRequest = jsonFileReader.readJsonFromFile(jsonRequestFilePath);

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .content(jsonRequest)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String responseBodyContent = result.getResponse().getContentAsString();

        String jsonResponse = jsonFileReader.readJsonFromFile(jsonResponseFilePath);

        if (ignoreUUIDValue) {
            assertJson(responseBodyContent)
                    .where()
                    .keysInAnyOrder()
                    .arrayInAnyOrder()
                    .at("/uuid").isNotEmpty()
                    .isEqualTo(jsonResponse);
        } else {
            assertJson(responseBodyContent)
                    .where()
                    .keysInAnyOrder()
                    .arrayInAnyOrder()
                    .isEqualTo(jsonResponse);
        }
    }
}
