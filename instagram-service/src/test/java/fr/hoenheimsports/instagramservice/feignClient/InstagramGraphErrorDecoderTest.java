package fr.hoenheimsports.instagramservice.feignClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import fr.hoenheimsports.instagramservice.exceptions.*;
import fr.hoenheimsports.instagramservice.feignClient.dto.GraphErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class InstagramGraphErrorDecoderTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private InstagramGraphErrorDecoder decoder;

    private Response response;
    private Request request;

    @BeforeEach
    void setUp() {
        request = Request.create(Request.HttpMethod.GET, "http://localhost",
                Collections.emptyMap(), null, StandardCharsets.UTF_8, null);
    }

    private Response createResponse(String body, int status) {
        return Response.builder()
                .status(status)
                .reason(status == 400 ? "Bad Request" : "Server Error")
                .body(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)), body.length())
                .request(request)
                .build();
    }
    @Test
    void testDecodeWithOAuthException() throws Exception {
        // Given
        String json = "{\"error\":{\"message\":\"Invalid token\",\"type\":\"IGApiBadRequestException\",\"code\":400,\"subcode\":0,\"fbtrace_id\":\"XYZ123\"}}";
        GraphErrorResponse.Error error = new GraphErrorResponse.Error("Invalid token", "IGApiBadRequestException", 400, 0, "XYZ123");
        GraphErrorResponse errorResponse = new GraphErrorResponse(error);
        given(objectMapper.readValue(any(Reader.class), eq(GraphErrorResponse.class))).willReturn(errorResponse);
        response = createResponse(json, 400);

        // When
        Exception result = decoder.decode("someMethod", response);

        // Then
        assertThat(result)
                .isInstanceOf(IGAPIBadRequestException.class)
                .hasMessageContaining("Invalid token");
    }

    @Test
    void testDecodeWithBadRequestException() throws Exception {
        String json = "{\"error\":{\"message\":\"Invalid request\",\"type\":\"IGApiBadRequestException\",\"code\":400,\"subcode\":0,\"fbtrace_id\":\"XYZ123\"}}";
        GraphErrorResponse.Error error = new GraphErrorResponse.Error("Invalid request", "IGApiBadRequestException", 400, 0, "XYZ123");
        GraphErrorResponse errorResponse = new GraphErrorResponse(error);
        given(objectMapper.readValue(any(Reader.class), eq(GraphErrorResponse.class))).willReturn(errorResponse);
        response = createResponse(json, 400);

        Exception result = decoder.decode("someMethod", response);

        assertThat(result)
                .isInstanceOf(IGAPIBadRequestException.class)
                .hasMessageContaining("Invalid request");
    }

    @Test
    void testDecodeWithUnauthorizedException() throws Exception {
        String json = "{\"error\":{\"message\":\"Unauthorized access\",\"type\":\"IGApiUnauthorizedException\",\"code\":401,\"subcode\":0,\"fbtrace_id\":\"XYZ456\"}}";
        GraphErrorResponse.Error error = new GraphErrorResponse.Error("Unauthorized access", "IGApiUnauthorizedException", 401, 0, "XYZ456");
        GraphErrorResponse errorResponse = new GraphErrorResponse(error);
        given(objectMapper.readValue(any(Reader.class), eq(GraphErrorResponse.class))).willReturn(errorResponse);
        response = createResponse(json, 401);

        Exception result = decoder.decode("someMethod", response);

        assertThat(result)
                .isInstanceOf(IGAPIUnauthorizedException.class)
                .hasMessageContaining("Unauthorized access");
    }

    @Test
    void testDecodeWithForbiddenException() throws Exception {
        String json = "{\"error\":{\"message\":\"Access forbidden\",\"type\":\"IGApiForbiddenException\",\"code\":403,\"subcode\":0,\"fbtrace_id\":\"XYZ789\"}}";
        GraphErrorResponse.Error error = new GraphErrorResponse.Error("Access forbidden", "IGApiForbiddenException", 403, 0, "XYZ789");
        GraphErrorResponse errorResponse = new GraphErrorResponse(error);
        given(objectMapper.readValue(any(Reader.class), eq(GraphErrorResponse.class))).willReturn(errorResponse);
        response = createResponse(json, 403);

        Exception result = decoder.decode("someMethod", response);

        assertThat(result)
                .isInstanceOf(IGAPIForbiddenException.class)
                .hasMessageContaining("Access forbidden");
    }

    @Test
    void testDecodeWithGenericException() throws Exception {
        String json = "{\"error\":{\"message\":\"Generic error\",\"type\":\"IGApiException\",\"code\":500,\"subcode\":0,\"fbtrace_id\":\"XYZ000\"}}";
        GraphErrorResponse.Error error = new GraphErrorResponse.Error("Generic error", "IGApiException", 500, 0, "XYZ000");
        GraphErrorResponse errorResponse = new GraphErrorResponse(error);
        given(objectMapper.readValue(any(Reader.class), eq(GraphErrorResponse.class))).willReturn(errorResponse);
        response = createResponse(json, 500);

        Exception result = decoder.decode("someMethod", response);

        assertThat(result)
                .isInstanceOf(IGAPIException.class)
                .hasMessageContaining("Generic error");
    }

    @Test
    void testDecodeWithCodedException() throws Exception {
        String json = "{\"error\":{\"message\":\"Specific coded error\",\"type\":\"CodedException\",\"code\":418,\"subcode\":0,\"fbtrace_id\":\"XYZ123\"}}";
        GraphErrorResponse.Error error = new GraphErrorResponse.Error("Specific coded error", "CodedException", 418, 0, "XYZ123");
        GraphErrorResponse errorResponse = new GraphErrorResponse(error);
        given(objectMapper.readValue(any(Reader.class), eq(GraphErrorResponse.class))).willReturn(errorResponse);
        response = createResponse(json, 418);

        Exception result = decoder.decode("someMethod", response);

        assertThat(result)
                .isInstanceOf(CodedException.class)
                .hasMessageContaining("Specific coded error");
    }

    @Test
    void testDecodeWithUnknownErrorType() throws Exception {
        String json = "{\"error\":{\"message\":\"Unknown type error\",\"type\":\"UnknownErrorType\",\"code\":400,\"subcode\":0,\"fbtrace_id\":\"XYZ321\"}}";
        GraphErrorResponse.Error error = new GraphErrorResponse.Error("Unknown type error", "UnknownErrorType", 400, 0, "XYZ321");
        GraphErrorResponse errorResponse = new GraphErrorResponse(error);
        given(objectMapper.readValue(any(Reader.class), eq(GraphErrorResponse.class))).willReturn(errorResponse);
        response = createResponse(json, 400);

        Exception result = decoder.decode("someMethod", response);

        assertThat(result)
                .isInstanceOf(InstagramAPIException.class)
                .hasMessageContaining("Unknown error calling Instagram API.");
    }


}