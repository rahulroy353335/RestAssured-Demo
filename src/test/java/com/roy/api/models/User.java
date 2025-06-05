package com.roy.api.models;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int id;
    private String name;
    private String username;
    private String email;
    private Address address;
    private String phone;
    private String website;
    private Company company;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Geo {
            private String lat;
            private String lng;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Company {
        private String name;
        private String catchPhrase;
        private String bs;
    }

}
