package com.adobe.aem.guides.wknd.core.servlets;

import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
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
import java.util.Iterator;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/wknd/page-api")
@ServiceDescription("WKND Page API Servlet for CRUD Operations: Path based for WKND Project")
@ServiceVendor("WKND")
public class WKNDPageAPIServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(WKNDPageAPIServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        LOGGER.info("WKND Page API Servlet triggered with doGet() method.");

        String pagePath = request.getParameter("pagePath");

        ResourceResolver resourceResolver = request.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

        if (pageManager != null) {
            Page page = pageManager.getPage(pagePath);
            if (page != null) {
                LOGGER.info("Title: {}, Page Title: {}, Page Path: {}", page.getTitle(), page.getPageTitle(), page.getPath());
                response.getWriter().write("Page Found: " + page.getPath() + "\n");
                response.getWriter().write("Page Properties: Title: " + page.getTitle() + ", Page Title: " + page.getPageTitle() + "\n");
                Tag[] tags = page.getTags();
                if (tags != null) {
                    for (Tag tag : tags) {
                        LOGGER.info("Page Tags: {}", tag.getTitle());
                        response.getWriter().write("Page Tags: " + tag.getTitle() + "\n");
                    }
                }

                Iterator<Page> childPages = page.listChildren();
                if (childPages.hasNext()) {
                    while (childPages.hasNext()) {
                        Page childPage = childPages.next();
                        LOGGER.info("Child Page - Title: {}, Page Title: {}, Page Path: {}", childPage.getTitle(), childPage.getPageTitle(), childPage.getPath());
                        response.getWriter().write("Child Page Found: " + childPage.getPath() + "\n");
                        response.getWriter().write("Child Page Properties:  Title: " + childPage.getTitle() + ", Page Title: " + childPage.getPageTitle() + "\n");
                    }
                } else {
                    LOGGER.info("No child pages found.");
                    response.getWriter().write("No child pages found.");
                }
            } else {
                LOGGER.info("Page not found at path: {}", pagePath);
                response.getWriter().write("Page not found at path: " + pagePath);
            }
        } else {
            LOGGER.info("PageManager adaptation failed.");
            response.getWriter().write("PageManager adaptation failed.");
        }
    }
}
