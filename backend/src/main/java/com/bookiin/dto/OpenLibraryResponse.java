package com.bookiin.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenLibraryResponse {

    private Map<String, BookData> books = new HashMap<>();

    @JsonAnySetter
    public void setDynamicProperty(String name, BookData value) {
        books.put(name, value);
    }

    public Map<String, BookData> getBooks() {
        return books;
    }

    public static class BookData {
        private String title;
        private List<Author> authors;
        private List<Publisher> publishers;
        private Cover cover;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public List<Author> getAuthors() { return authors; }
        public void setAuthors(List<Author> authors) { this.authors = authors; }
        public List<Publisher> getPublishers() { return publishers; }
        public void setPublishers(List<Publisher> publishers) { this.publishers = publishers; }
        public Cover getCover() { return cover; }
        public void setCover(Cover cover) { this.cover = cover; }
    }

    public static class Author {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class Publisher {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class Cover {
        private String medium;
        private String large;
        public String getMedium() { return medium; }
        public void setMedium(String medium) { this.medium = medium; }
        public String getLarge() { return large; }
        public void setLarge(String large) { this.large = large; }
    }
}
