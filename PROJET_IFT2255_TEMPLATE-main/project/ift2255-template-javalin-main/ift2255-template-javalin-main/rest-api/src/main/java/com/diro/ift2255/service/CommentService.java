package com.diro.ift2255.service;

import java.util.*;

public class CommentService {

    private Map<String, List<Map<String, String>>> comments = new HashMap<>();

    public List<Map<String, String>> getByCourse(String courseId) {
        return comments.getOrDefault(courseId.toUpperCase(), new ArrayList<>());
    }

    public Map<String, String> create(Map<String, String> comment) {
        String courseId = comment.get("courseId").toUpperCase();
        comments.putIfAbsent(courseId, new ArrayList<>());
        comments.get(courseId).add(comment);
        return comment;
    }
}