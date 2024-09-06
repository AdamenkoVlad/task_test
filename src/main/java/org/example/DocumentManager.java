package org.example;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;

/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
public class DocumentManager {

    private final HashMap<String, Document> documents = new HashMap<>();


    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist, don't change [created] field
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (document.getId() == null || document.getId().isEmpty()) {
            document.setId(UUID.randomUUID().toString());
            document.setCreated(Instant.now());
        }

        documents.put(document.getId(), document);
        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {

        List<Document> result = new ArrayList<>();
        for (Document document : documents.values()) {
            if (matches(request, document)) {
                result.add(document);
            }
        }

        return result;
    }

    /**
     * Method checks if the document's title starts with any of the provided prefixes,
     * if the content contains any of the specified strings,
     * if the author ID matches any of the provided author IDs,
     * and if the document's creation date falls within the specified date range.
     *
     * @param request  - the search request containing various filter criteria
     * @param document - the document to be checked against the search criteria
     * @return true if the document matches all the criteria, false otherwise
     */
    private boolean matches(SearchRequest request, Document document) {
        if (request.getTitlePrefixes() != null) {
            boolean titleMatches = false;
            for (String prefix : request.getTitlePrefixes()) {
                if (document.getTitle().startsWith(prefix)) {
                    titleMatches = true;
                    break;
                }
            }
            if (!titleMatches) {
                return false;
            }

        }
        if (request.getContainsContents() != null) {
            boolean contentMatches = false;
            for (String content : request.getContainsContents()) {
                if (document.getContent().contains(content)) {
                    contentMatches = true;
                    break;
                }
            }
            if (!contentMatches) {
                return false;
            }
        }

        if (request.getAuthorIds() != null) {
            boolean authorMatches = false;
            for (String authorId : request.getAuthorIds()) {
                if (authorId.equals(document.getAuthor().getId())) {
                    authorMatches = true;
                    break;
                }
            }
            if (!authorMatches) {
                return false;
            }
        }
        if (request.getCreatedFrom() != null && document.getCreated()
                .isBefore(request.getCreatedFrom())) {
            return false;
        }
        if (request.getCreatedTo() != null && document.getCreated()
                .isAfter(request.getCreatedTo())) {
            return false;
        }
        return true;
    }


    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {

        return Optional.ofNullable(documents.get(id));
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {

        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}