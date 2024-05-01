package fr.hoenheimsports.instagramservice.feignClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import feign.Response.Body;
import fr.hoenheimsports.instagramservice.exceptions.IGAPIOAuthException;
import fr.hoenheimsports.instagramservice.exceptions.InstagramAPIException;
import fr.hoenheimsports.instagramservice.feignClient.dto.APIErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
public class InstagramApiErrorDecoderTest {
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private InstagramApiErrorDecoder decoder;

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
    void testDecodeWithNullBody() {
        // Given
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .body((Body) null)
                .request(Request.create( Request.HttpMethod.GET, "http://localhost", Collections.emptyMap(), null, Charset.defaultCharset(), null))
                .build();

        // When
        Exception result = decoder.decode("someMethod", response);

        // Then
        assertThat(result)
                .isInstanceOf(InstagramAPIException.class)
                .hasMessageContaining("Erreur inconnue lors de l'appel à l'API Instagram.");
    }

    @Test
    void testDecodeWithOAuthException() throws Exception {
        // Given
        String json = "{\"error_message\":\"Invalid token\",\"error_type\":\"OAuthException\",\"code\":400}";
        APIErrorResponse errorResponse = new APIErrorResponse("Invalid token", "OAuthException", 400);
        given(objectMapper.readValue(any(Reader.class), eq(APIErrorResponse.class))).willReturn(errorResponse);
        Response response = createResponse(json, 400);

        // When
        Exception result = decoder.decode("someMethod", response);

        // Then
        assertThat(result)
                .isInstanceOf(IGAPIOAuthException.class)
                .hasMessageContaining("Invalid token");
    }

    @Test
    void testDecodeWithUnknownError() throws Exception {
        // Given
        String json = "{\"error_message\":\"Server error\",\"error_type\":\"ServerError\",\"code\":500}";
        APIErrorResponse errorResponse = new APIErrorResponse("Server error", "ServerError", 500);
        given(objectMapper.readValue(any(Reader.class), eq(APIErrorResponse.class))).willReturn(errorResponse);
        Response response = createResponse(json, 500);

        // When
        Exception result = decoder.decode("someMethod", response);

        // Then
        assertThat(result)
                .isInstanceOf(InstagramAPIException.class)
                .hasMessageContaining("Unknown error calling Instagram API.");
    }
}
