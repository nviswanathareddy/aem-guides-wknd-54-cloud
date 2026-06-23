package com.adobe.aem.guides.wknd.core.services;

import com.adobe.aem.guides.wknd.core.utils.WKNDSafeResourceResolver;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component(service = WKNDWebPageIdGeneratorService.class)
@ServiceDescription("WKND Service for generating Web Page ID")
@ServiceVendor("WKND")
public class WKNDWebPageIdGeneratorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WKNDWebPageIdGeneratorService.class);
    private static final String COUNTER_PATH = "/var/wknd/pageid-counter";
    private static final String COUNTER_PROPERTY = "lastSequence";

    @Reference
    private WKNDSafeResourceResolver wkndSafeResourceResolver;

    public synchronized String generateWebPageId() {

        try (ResourceResolver resourceResolver = wkndSafeResourceResolver.getResourceResolver()) {

            if (resourceResolver == null) {
                LOGGER.error("Unable to obtain service resource resolver");
                throw new IllegalStateException("Unable to obtain service resource resolver");
            }

            Resource counterResource = resourceResolver.getResource(COUNTER_PATH);

            if (counterResource == null) {
                LOGGER.error("Counter resource not found at {}", COUNTER_PATH);
                throw new IllegalStateException("Counter resource not found");
            }

            ModifiableValueMap properties = counterResource.adaptTo(ModifiableValueMap.class);

            if (properties == null) {
                LOGGER.error("Unable to adapt resource {} to ModifiableValueMap", COUNTER_PATH);
                throw new IllegalStateException("Unable to update counter resource");
            }

            long nextSequence = properties.get(COUNTER_PROPERTY, 0L) + 1;

            properties.put(COUNTER_PROPERTY, nextSequence);
            resourceResolver.commit();

            String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            String webPageId = String.format("WKND-%s-%03d", date, nextSequence);
            LOGGER.info("Generated Web Page ID: {}", webPageId);

            return webPageId;

        } catch (PersistenceException e) {
            LOGGER.error("Error while generating Web Page ID", e);
            throw new IllegalStateException("Unable to generate Web Page ID", e);
        }
    }
}