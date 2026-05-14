package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.ExampleModelInterface;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Model(
        adaptables = Resource.class,
        adapters = ExampleModelInterface.class,
        resourceType = ExampleModelInterfaceImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ExampleModelInterfaceImpl
        implements ExampleModelInterface {

    public static final String RESOURCE_TYPE = "wknd/components/content/example";

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleModelInterfaceImpl.class);

    @ValueMapValue
    private String name;

    @ValueMapValue
    private String[] roles;

    @PostConstruct
    protected void init() {

        LOGGER.info("Name of the person: {}", name);
        LOGGER.info("Roles: {}", Arrays.toString(roles));

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getRoles() {
        return roles;
    }
}