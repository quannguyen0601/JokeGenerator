package com.quan.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public interface IJokeService {
    BaseResponse getJokes(BaseSearch query);

    class BaseSearch {
        private String keyword;

        public BaseSearch(String keyword) {
            this.keyword = keyword;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }
    }

    class BaseResponse {
        public BaseResponse(List<String> jokes) {
            this.jokes = jokes;
        }

        public BaseResponse() {
        }

        private List<String> jokes =  new ArrayList<>();

        public List<String> getJokes() {
            return jokes;
        }

        public void setJokes(List<String> jokes) {
            this.jokes = jokes;
        }
    }
}
