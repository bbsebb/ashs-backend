package fr.hoenheimsports.instagramservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import fr.hoenheimsports.instagramservice.feignClient.InstagramApiErrorDecoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

class FeignGraphConfigTest {

    @MockBean
    private ObjectMapper mockObjectMapper;


    @Test
    public void testErrorDecoderBean() {
        FeignApiConfig config = new FeignApiConfig(mockObjectMapper);
        ErrorDecoder errorDecoder = config.errorDecoder();
        assertInstanceOf(InstagramApiErrorDecoder.class, errorDecoder);
    }

}