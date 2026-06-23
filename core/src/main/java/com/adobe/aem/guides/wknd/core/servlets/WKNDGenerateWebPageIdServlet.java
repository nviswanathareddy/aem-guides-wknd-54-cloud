package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.services.WKNDWebPageIdGeneratorService;
import com.google.gson.JsonObject;
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
@SlingServletPaths("/bin/wknd/generate-webpageid")
@ServiceDescription("WKND Servlet for generating Web Page ID")
@ServiceVendor("WKND")
public class WKNDGenerateWebPageIdServlet extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(WKNDGenerateWebPageIdServlet.class);

    @Reference
    private transient WKNDWebPageIdGeneratorService webPageIdGeneratorService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        String webPage = request.getParameter("item");

        String webPageId = webPageIdGeneratorService.generateWebPageId();
        LOGGER.info("Web Page ID: {}", webPageId);
        LOGGER.info("Web Page: {}", webPage);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("webPageId", webPageId);
        jsonObject.addProperty("webPage", webPage);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonObject.toString());
    }
}