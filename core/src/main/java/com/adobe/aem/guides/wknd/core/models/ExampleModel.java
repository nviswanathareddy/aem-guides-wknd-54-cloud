package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ExampleModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleModel.class);

    @ValueMapValue
    private String multifieldDescription;

    @ValueMapValue
    private String nestedMultifieldDescription;

    @ChildResource
    private List<PlatformItems> platformItems;

    @ChildResource
    private List<ProductItems> productItems;

    @PostConstruct
    protected void init() {

        LOGGER.info("Author Occupation: {}", multifieldDescription);

    }

    public String getMultifieldDescription() {
        return multifieldDescription;
    }

    public String getNestedMultifieldDescription() {
        return nestedMultifieldDescription;
    }

    public List<PlatformItems> getPlatformItems() {
        return platformItems;
    }

    public List<ProductItems> getProductItems() {
        return productItems;
    }
}