package ru.taipov.webperson.projectboot.dto;

import java.util.Date;


public class PersonDTO {



        private String name;

        private int age;

        private String email;



        public PersonDTO(String name, int age, String email, Date created_at) {

            this.name = name;
            this.age = age;
            this.email = email;

        }

        public PersonDTO()
        {

        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getEmail() {
            return email;
        }





    }


