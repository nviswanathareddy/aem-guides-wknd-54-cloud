package com.adobe.aem.guides.wknd.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/wknd/resource-api")
@ServiceDescription("WKND Resource API Servlet for CRUD Operations: Path based for WKND Project")
@ServiceVendor("WKND")
public class WKNDResourceAPIServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(WKNDResourceAPIServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        LOGGER.info("WKND Resource API Servlet triggered with doGet() method.");

        String resourcePath = request.getParameter("resourcePath");
        String user = request.getParameter("user");

        String userResource = resourcePath + "/" + user;

        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource resource = resourceResolver.getResource(userResource);

        if (resource != null) {
            ValueMap valueMap = resource.getValueMap();
            String name = valueMap.get("name", String.class);
            response.getWriter().write("Name Property: " + name);
            LOGGER.info("Name property fetched for resource: {}", resource.getPath());

            response.getWriter().write("Properties for resource: " + resource.getPath() + "\n");
            for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
                response.getWriter().write(entry.getKey() + " : " + entry.getValue() + "\n");
            }
            LOGGER.info("Properties fetched for resource: {}", resource.getPath());
        } else {
            LOGGER.info("Resource not found at path to get property: {}", userResource);
            response.getWriter().write("Resource not found at path: " + userResource);
        }
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        LOGGER.info("WKND Resource API Servlet triggered with doPost() method.");

        String resourcePath = request.getParameter("resourcePath");
        String user = request.getParameter("user");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource resource = resourceResolver.getResource(resourcePath);

        Map<String, Object> properties = new HashMap<>();
        properties.put("name", name);
        properties.put("email", email);
        properties.put("phone", phone);

        try {
            if (resource != null) {
                Resource node = resourceResolver.getResource(resourcePath + "/" + user);
                if (node == null) {
                    node = resourceResolver.create(resource, user, properties);
                    ModifiableValueMap modifiableValueMap = node.adaptTo(ModifiableValueMap.class);
                    if (modifiableValueMap != null) {
                        modifiableValueMap.put("status", "created");
                    }
                    resourceResolver.commit();
                    LOGGER.info("Resource created at path: {}", node.getPath());
                    response.getWriter().write("Resource created at Path: " + node.getPath());
                } else {
                    LOGGER.info("Node already exists at path: {}", node.getPath());
                    response.getWriter().write("Node already exists at Path: " + node.getPath());
                }
            } else {
                LOGGER.info("Parent resource not found for path: {}", resourcePath);
                response.getWriter().write("Parent resource not found");
            }
        } catch (PersistenceException e) {
            LOGGER.error("Error while creating a resource", e);
            response.getWriter().write("Error while creating a resource");
        }
    }

    @Override
    protected void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        LOGGER.info("WKND Resource API Servlet triggered with doPut() method.");

        String resourcePath = request.getParameter("resourcePath");
        String user = request.getParameter("user");
        String phone = request.getParameter("phone");

        String userResource = resourcePath + "/" + user;

        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource resource = resourceResolver.getResource(userResource);

        try {
            if (resource != null) {
                ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
                if (modifiableValueMap != null) {
                    modifiableValueMap.put("phone", phone);
                    modifiableValueMap.put("status", "updated");
                    resourceResolver.commit();
                    LOGGER.info("Properties updated for path: {}", resource.getPath());
                    response.getWriter().write("Properties updated for path: " + resource.getPath());
                }
            } else {
                LOGGER.info("Resource not found at path: {}", userResource);
                response.getWriter().write("Resource not found at path: " + userResource);
            }
        } catch (PersistenceException e) {
            LOGGER.error("Error while updating resource", e);
            response.getWriter().write("Error while updating resource");
        }
    }

    @Override
    protected void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        LOGGER.info("WKND Resource API Servlet triggered with doDelete() method.");

        String resourcePath = request.getParameter("resourcePath");
        String user = request.getParameter("user");

        String userResource = resourcePath + "/" + user;

        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource resource = resourceResolver.getResource(userResource);

        try {
            if (resource != null) {
                ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
                if (modifiableValueMap != null) {
                    modifiableValueMap.remove("status");
                    resourceResolver.commit();
                    LOGGER.info("Property removed from resource: {}", resource.getPath());
                    response.getWriter().write("Property removed from resource: " + resource.getPath());
                }
                resourceResolver.delete(resource);
                resourceResolver.commit();
                LOGGER.info("Resource deleted at path: {}", resource.getPath());
                response.getWriter().write("Resource deleted at path: " + resource.getPath());
            } else {
                LOGGER.info("Resource not found at path to delete: {}", userResource);
                response.getWriter().write("Resource not found at path: " + userResource);
            }
        } catch (PersistenceException e) {
            LOGGER.error("Error while deleting resource", e);
            response.getWriter().write("Error while deleting resource");
        }
    }
}