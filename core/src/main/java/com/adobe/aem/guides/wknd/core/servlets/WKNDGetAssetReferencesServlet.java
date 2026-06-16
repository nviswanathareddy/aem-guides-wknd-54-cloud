package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.models.WKNDAssetReferenceResult;
import com.adobe.aem.guides.wknd.core.services.WKNDGetAssetReferencesFromPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/wknd/assetreferences")
@ServiceDescription("Returns valid and broken asset references for a page")
@ServiceVendor("WKND")
public class WKNDGetAssetReferencesServlet extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(WKNDGetAssetReferencesServlet.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Reference
    private WKNDGetAssetReferencesFromPage assetReferenceService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String pagePath = request.getParameter("pagePath");
        if (pagePath == null || pagePath.isBlank()) {
            LOGGER.warn("Missing required parameter: pagePath");
            response.sendError(SlingHttpServletResponse.SC_BAD_REQUEST, "Required parameter 'pagePath' is missing");
            return;
        }
        LOGGER.debug("Fetching asset references for page {}", pagePath);
        WKNDAssetReferenceResult result = assetReferenceService.getAssetReferences(request.getResourceResolver(), pagePath);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), result);
        LOGGER.info("Returned {} valid images and {} broken images for page {}", result.getValidImages().size(), result.getBrokenImages().size(), pagePath);
    }
}