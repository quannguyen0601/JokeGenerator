package com.quan.service;

import java.util.ArrayList;
import java.util.List;

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
        private List<String> jokes = new ArrayList<>();

        public BaseResponse(List<String> jokes) {
            this.jokes = jokes;
        }

        public BaseResponse() {
        }

        public List<String> getJokes() {
            return jokes;
        }

        public void setJokes(List<String> jokes) {
            this.jokes = jokes;
        }
    }
}
