package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductItems {

    @ValueMapValue
    private String productName;

    @ValueMapValue
    private String productStatus;

    @ChildResource
    private List<Resource> productInfoItems;

    public String getProductName() {
        return productName;
    }

    public String isProductStatus() {
        return productStatus;
    }

    public List<Resource> getProductInfoItems() {
        return productInfoItems;
    }
}
