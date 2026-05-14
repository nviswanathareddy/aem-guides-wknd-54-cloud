package com.adobe.aem.guides.wknd.core.models;

import com.day.cq.wcm.api.Page;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@Model(
        adaptables = SlingHttpServletRequest.class,
        resourceType = PropertyOperationsModel.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public final class PropertyOperationsModel {

    public static final String RESOURCE_TYPE = "wknd/components/content/propertyoperations";

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyOperationsModel.class);

    private static final String FEATURED_CATEGORY = "featuredCategory";
    private static final String PRODUCT_SKU = "productSKU";

    private static final String OTHER_PAGE_PATH = "/content/wknd/us/en/products";
    private static final String OTHER_RESOURCE_PATH = "/content/wknd/us/en/products/jcr:content/root/container/container/productdetails";

    @ScriptVariable
    private Page currentPage;

    @SlingObject
    private Resource currentResource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Getter
    private String currentPageTitle;

    @Getter
    private String currentPagePath;

    @Getter
    private String currentPageCustomProperty;

    @Getter
    private String currentResourceName;

    @Getter
    private String currentResourcePath;

    @Getter
    private String currentResourceCustomProperty;

    @Getter
    private String otherPageTitle;

    @Getter
    private String otherPagePath;

    @Getter
    private String otherPageCustomProperty;

    @Getter
    private String otherResourceName;

    @Getter
    private String otherResourcePath;

    @Getter
    private String otherResourceCustomProperty;

    @PostConstruct
    private void init() {

        if (resourceResolver == null) {
            LOGGER.warn("ResourceResolver is null");
            return;
        }

        populateCurrentPageProperties();
        populateCurrentResourceProperties();
        populateOtherPageProperties();
        populateOtherResourceProperties();
    }

    private void populateCurrentPageProperties() {

        if (currentPage == null) {
            LOGGER.warn("Current page is null");
            return;
        }

        currentPageTitle = currentPage.getTitle();
        currentPagePath = currentPage.getPath();
        currentPageCustomProperty = currentPage.getProperties().get(FEATURED_CATEGORY, String.class);

        try {
            ModifiableValueMap properties = currentPage.getContentResource().adaptTo(ModifiableValueMap.class);

            if (properties != null) {
                properties.put(FEATURED_CATEGORY, "WKND Electronics");
                resourceResolver.commit();
            }

        } catch (PersistenceException e) {
            LOGGER.error("Error while updating current page property", e);
        }

        LOGGER.info("Current Page -> Title: {}, Path: {}, Category: {}", currentPageTitle, currentPagePath, currentPageCustomProperty);
    }

    private void populateCurrentResourceProperties() {

        if (currentResource == null) {
            LOGGER.warn("Current resource is null");
            return;
        }

        currentResourceName = currentResource.getName();
        currentResourcePath = currentResource.getPath();
        currentResourceCustomProperty = currentResource.getValueMap().get(PRODUCT_SKU, String.class);

        try {
            ModifiableValueMap properties = currentResource.adaptTo(ModifiableValueMap.class);

            if (properties != null) {
                properties.put(PRODUCT_SKU, "SKU-WKND-1001");
                resourceResolver.commit();
            }

        } catch (PersistenceException e) {
            LOGGER.error("Error while updating current resource property", e);
        }

        LOGGER.info("Current Resource -> Name: {}, Path: {}, SKU: {}", currentResourceName, currentResourcePath, currentResourceCustomProperty);
    }

    private void populateOtherPageProperties() {

        Resource otherPageResource = resourceResolver.getResource(OTHER_PAGE_PATH);

        if (otherPageResource == null) {
            LOGGER.warn("Other page resource not found: {}", OTHER_PAGE_PATH);
            return;
        }

        Page otherPage = otherPageResource.adaptTo(Page.class);

        if (otherPage == null) {
            LOGGER.warn("Unable to adapt resource to page: {}", OTHER_PAGE_PATH);
            return;
        }

        otherPageTitle = otherPage.getTitle();
        otherPagePath = otherPage.getPath();
        otherPageCustomProperty = otherPage.getProperties().get(FEATURED_CATEGORY, String.class);

        try {
            ModifiableValueMap properties = otherPage.getContentResource().adaptTo(ModifiableValueMap.class);

            if (properties != null) {
                properties.put(FEATURED_CATEGORY, "WKND Fashion");
                resourceResolver.commit();
            }

        } catch (PersistenceException e) {
            LOGGER.error("Error while updating other page property", e);
        }

        LOGGER.info("Other Page -> Title: {}, Path: {}, Category: {}", otherPageTitle, otherPagePath, otherPageCustomProperty);
    }

    private void populateOtherResourceProperties() {

        Resource otherResource = resourceResolver.getResource(OTHER_RESOURCE_PATH);

        if (otherResource == null) {
            LOGGER.warn("Other resource not found: {}", OTHER_RESOURCE_PATH);
            return;
        }

        otherResourceName = otherResource.getName();
        otherResourcePath = otherResource.getPath();
        otherResourceCustomProperty = otherResource.getValueMap().get(PRODUCT_SKU, String.class);

        try {
            ModifiableValueMap properties = otherResource.adaptTo(ModifiableValueMap.class);

            if (properties != null) {
                properties.put(PRODUCT_SKU, "SKU-WKND-5001");
                resourceResolver.commit();
            }

        } catch (PersistenceException e) {
            LOGGER.error("Error while updating other resource property", e);
        }

        LOGGER.info("Other Resource -> Name: {}, Path: {}, SKU: {}", otherResourceName, otherResourcePath, otherResourceCustomProperty);
    }
}