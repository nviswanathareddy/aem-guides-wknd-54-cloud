package com.adobe.aem.guides.wknd.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletResourceTypes(resourceTypes = "wknd/components/content/wknd-resource-based-servlet")
@ServiceDescription("WKND Resource Based Servlet for WKND Project")
@ServiceVendor("WKND")

public class WKNDResourceBasedServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(WKNDResourceBasedServlet.class);

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {
        LOGGER.info("WKND Resource Based Servlet triggered with doGet Method: {}", request.getRequestURI());
        response.setContentType("text/plain");
        response.getWriter().write("WKND Resource Based Servlet triggered with doGet Method:");
    }

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {
        LOGGER.info("WKND Resource Based Servlet triggered with doPost Method: {}", request.getRequestURI());
        response.setContentType("text/plain");
        response.getWriter().write("WKND Resource Based Servlet triggered with doPost Method:");
    }

    @Override
    protected void doPut(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {
        LOGGER.info("WKND Resource Based Servlet triggered with doPut Method: {}", request.getRequestURI());
        response.setContentType("text/plain");
        response.getWriter().write("WKND Resource Based Servlet triggered with doPut Method:");
    }

    @Override
    protected void doDelete(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException {
        LOGGER.info("WKND Resource Based Servlet triggered with doDelete Method: {}", request.getRequestURI());
        response.setContentType("text/plain");
        response.getWriter().write("WKND Resource Based Servlet triggered with doDelete Method:");
    }
}
