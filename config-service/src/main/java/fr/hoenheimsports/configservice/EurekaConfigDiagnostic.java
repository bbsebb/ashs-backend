package fr.hoenheimsports.configservice;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
public class EurekaConfigDiagnostic {

    private static final Logger log = LoggerFactory.getLogger(EurekaConfigDiagnostic.class);

    @Value("${DISCOVERY_SERVICE_URL:Pas défini}")
    private String discoveryServiceUrl;

    @PostConstruct
    public void postConstruct() {
        log.info("DISCOVERY_SERVICE_URL = {}", discoveryServiceUrl);
    }
}
