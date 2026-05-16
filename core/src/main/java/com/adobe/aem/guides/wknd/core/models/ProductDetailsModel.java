package com.adobe.aem.guides.wknd.core.models;

import com.adobe.aem.guides.wknd.core.services.GreetingService;
import com.day.cq.wcm.api.Page;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = ProductDetailsModel.RESOURCE_TYPE
)
public class ProductDetailsModel {

    public static final String RESOURCE_TYPE = "wknd/components/content/productdetails";

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ProductDetailsModel.class);

    @Getter
    @ValueMapValue
    private String productTitle;

    @Getter
    @ValueMapValue
    @Named("sling:resourceType")
    private String componentResourceType;

    @Getter
    @ValueMapValue
    @Default(values = "Electronics")
    private String productCategory;

    @Getter
    @ValueMapValue
    @Required
    @Default(values = "SKU-1001")
    private String productSKU;

    @Getter
    @ValueMapValue
    @Optional
    private String productTagline;

    @Getter
    @OSGiService
    private GreetingService greetingService;

    @Getter
    @ChildResource
    private List<Resource> productFeatures;

    @Getter
    @ScriptVariable
    private Page currentPage;

    @Getter
    @SlingObject
    private ResourceResolver resourceResolver;

    @Getter
    @RequestAttribute
    private String campaignId;

    @Getter
    @ResourcePath(path = "/content/wknd/us/en")
    private Page enPage;

    @Getter
    @Inject
    @Via("resource")
    private String manufacturerName;

    @Getter
    private String productSummary;

    @Getter
    private String productPageUrl;

    @Getter
    private String currentPagePath;

    @PostConstruct
    protected void init() {

        buildProductSummary();
        buildProductPageUrl();

        LOGGER.info("Product Title: {}", productTitle);
        LOGGER.info("Component Resource Type: {}", componentResourceType);
        LOGGER.info("Product Category: {}", productCategory);
        LOGGER.info("Product SKU: {}", productSKU);
        LOGGER.info("Product Tagline: {}", productTagline);
        LOGGER.info("Manufacturer Name: {}", manufacturerName);

        if (greetingService != null) {
            LOGGER.info("Greeting Message: {}", greetingService.getGreetingMessage());
        }

        if (currentPage != null) {
            LOGGER.info("Current Page Title: {}", currentPage.getTitle());
            LOGGER.info("Page Template: {}", currentPage.getTemplate().getPath());
            LOGGER.info("Current Page Featured Category: {}", currentPage.getProperties().get("featuredCategory"));
        }

        if (resourceResolver != null) {
            LOGGER.info("Logged In User: {}", resourceResolver.getUserID());
        }

        LOGGER.info("Campaign ID: {}", campaignId);

        if (enPage != null) {
            LOGGER.info("English Page Title: {}", enPage.getNavigationTitle());
            LOGGER.info("English Page Path: {}", enPage.getPath());
            LOGGER.info("English Page Site Region: {}", enPage.getProperties().get("siteRegion"));
        }

        if (productFeatures != null) {
            for (Resource feature : productFeatures) {
                String featureTitle = feature.getValueMap().get("featureTitle", String.class);
                String featureDescription = feature.getValueMap().get("featureDescription", String.class);

                LOGGER.info("Feature Node Name: {}", feature.getName());
                LOGGER.info("Feature Title: {}", featureTitle);
                LOGGER.info("Feature Description: {}", featureDescription);
            }
        }
    }

    private void buildProductSummary() {
        productSummary = productTitle + " | " + productCategory + " | By " + manufacturerName;
        LOGGER.info("Product Summary: {}", productSummary);
    }

    private void buildProductPageUrl() {
        currentPagePath = currentPage.getPath();
        productPageUrl = currentPagePath + "/" + productSKU.toLowerCase() + ".html";
        LOGGER.info("Product Page URL: {}", productPageUrl);
    }
}