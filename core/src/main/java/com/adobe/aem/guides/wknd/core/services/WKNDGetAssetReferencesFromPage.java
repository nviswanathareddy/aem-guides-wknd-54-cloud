package com.adobe.aem.guides.wknd.core.services;

import com.adobe.aem.guides.wknd.core.models.WKNDAssetReferenceResult;
import com.day.cq.dam.api.Asset;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = WKNDGetAssetReferencesFromPage.class)
@ServiceDescription("Returns valid and broken DAM asset references from a page")
@ServiceVendor("WKND")
public class WKNDGetAssetReferencesFromPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(WKNDGetAssetReferencesFromPage.class);

    public WKNDAssetReferenceResult getAssetReferences(ResourceResolver resourceResolver, String pagePath) {
        WKNDAssetReferenceResult result = new WKNDAssetReferenceResult();
        if (pagePath == null || pagePath.isBlank()) {
            LOGGER.warn("Page path is null or empty");
            return result;
        }
        Resource pageContentResource = resourceResolver.getResource(pagePath + "/jcr:content");
        if (pageContentResource == null) {
            LOGGER.warn("jcr:content not found for page {}", pagePath);
            return result;
        }
        LOGGER.debug("Collecting asset references from {}", pagePath);
        collectAssetReferences(pageContentResource, resourceResolver, result);
        LOGGER.info("Page [{}] contains {} valid images and {} broken images", pagePath, result.getValidImages().size(), result.getBrokenImages().size());
        return result;
    }

    private void collectAssetReferences(Resource resource, ResourceResolver resolver, WKNDAssetReferenceResult result) {
        ValueMap properties = resource.getValueMap();
        properties.forEach((key, value) -> {
            if (value instanceof String) {
                validateAssetReference((String) value, resolver, result);
            }
            if (value instanceof String[]) {
                for (String item : (String[]) value) {
                    validateAssetReference(item, resolver, result);
                }
            }
        });
        for (Resource child : resource.getChildren()) {
            collectAssetReferences(child, resolver, result);
        }
    }

    private void validateAssetReference(String value, ResourceResolver resolver, WKNDAssetReferenceResult result) {
        if (value == null || !value.startsWith("/content/dam/")) {
            return;
        }
        Resource assetResource = resolver.getResource(value);
        if (assetResource != null && assetResource.adaptTo(Asset.class) != null) {
            if (result.getValidImages().add(value)) {
                LOGGER.debug("Valid asset reference found: {}", value);
            }
        } else {
            if (result.getBrokenImages().add(value)) {
                LOGGER.warn("Broken asset reference found: {}", value);
            }
        }
    }
}