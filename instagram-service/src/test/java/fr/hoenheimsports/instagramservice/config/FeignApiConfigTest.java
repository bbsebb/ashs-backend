package fr.hoenheimsports.instagramservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import fr.hoenheimsports.instagramservice.feignClient.InstagramApiErrorDecoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;


@SpringBootTest(classes = FeignApiConfig.class)
public class FeignApiConfigTest {

    @MockBean
    private ObjectMapper mockObjectMapper;
    @MockBean
    private ObjectFactory<HttpMessageConverters> mockConvertersFactory;

    @Test
    public void testErrorDecoderBean() {
        FeignApiConfig config = new FeignApiConfig(mockObjectMapper);
        ErrorDecoder errorDecoder = config.errorDecoder();
        assertInstanceOf(InstagramApiErrorDecoder.class, errorDecoder);
    }

    @Test
    public void testEncoderBean() {
        FeignApiConfig config = new FeignApiConfig(mockObjectMapper);
        Encoder encoder = config.encoder(mockConvertersFactory);
        assertInstanceOf(SpringFormEncoder.class, encoder);
    }
}