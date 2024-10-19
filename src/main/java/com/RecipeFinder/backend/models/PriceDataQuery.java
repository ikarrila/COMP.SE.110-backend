// PriceDataQuery.java
package com.RecipeFinder.backend.models;

import java.util.List;

public class PriceDataQuery {
    private List<QueryItem> query;
    private ResponseFormat response;

    // Getters and Setters

    public List<QueryItem> getQuery() {
        return query;
    }

    public void setQuery(List<QueryItem> query) {
        this.query = query;
    }

    public ResponseFormat getResponse() {
        return response;
    }

    public void setResponse(ResponseFormat response) {
        this.response = response;
    }

    public static class QueryItem {
        private String code;
        private Selection selection;

        // Getters and Setters

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Selection getSelection() {
            return selection;
        }

        public void setSelection(Selection selection) {
            this.selection = selection;
        }
    }

    public static class Selection {
        private String filter;
        private List<String> values;

        // Getters and Setters

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }
    }

    public static class ResponseFormat {
        private String format;

        // Getters and Setters

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }
    }
}