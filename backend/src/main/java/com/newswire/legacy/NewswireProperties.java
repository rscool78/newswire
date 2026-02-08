package com.newswire.legacy;
//package com.newswire.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
// NO longer used keeping for historical purposes
@Deprecated
@ConfigurationProperties(prefix = "newswireold")
public class NewswireProperties {
    private List<String> feeds = new ArrayList<>();
    private int maxItems = 40;

    public List<String> getFeeds() { return feeds; }
    public void setFeeds(List<String> feeds) { this.feeds = feeds; }

    public int getMaxItems() { return maxItems; }
    public void setMaxItems(int maxItems) { this.maxItems = maxItems; }
}
