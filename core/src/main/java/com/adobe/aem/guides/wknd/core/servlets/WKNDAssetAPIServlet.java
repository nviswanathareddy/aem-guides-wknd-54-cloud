package com.adobe.aem.guides.wknd.core.servlets;

import com.day.cq.dam.api.Asset;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/wknd/asset-api")
@ServiceDescription("WKND Asset API Servlet for CRUD Operations: Path based for WKND Project")
@ServiceVendor("WKND")
public class WKNDAssetAPIServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(WKNDAssetAPIServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        LOGGER.info("WKND Asset API Servlet triggered with doGet() method.");

        String assetPath = request.getParameter("assetPath");

        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource resource = resourceResolver.getResource(assetPath);

        if (resource != null) {
            Asset asset = resource.adaptTo(Asset.class);
            if (asset != null) {
                String title = asset.getMetadataValue("dc:title");
                LOGGER.info("Title: {}, Asset Path: {}", title, asset.getPath());
                response.getWriter().write("Asset Path: " + asset.getPath() + "\n");
                response.getWriter().write("Title: " + title + "\n");

                String tags = asset.getMetadataValueFromJcr("cq:tags");
                if (tags != null) {
                    response.getWriter().write("Tags: " + tags + "\n");
                }
            } else {
                LOGGER.info("Asset adaptation failed.");
                response.getWriter().write("Asset adaptation failed.");
            }
        } else {
            LOGGER.info("Asset not found at path: {}", assetPath);
            response.getWriter().write("Asset not found at path: " + assetPath);
        }
    }
}
