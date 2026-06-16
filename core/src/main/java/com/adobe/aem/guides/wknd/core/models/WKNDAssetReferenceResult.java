package com.adobe.aem.guides.wknd.core.models;

import java.util.HashSet;
import java.util.Set;

public class WKNDAssetReferenceResult {

    private final Set<String> validImages = new HashSet<>();
    private final Set<String> brokenImages = new HashSet<>();

    public Set<String> getValidImages() {
        return validImages;
    }

    public Set<String> getBrokenImages() {
        return brokenImages;
    }
}