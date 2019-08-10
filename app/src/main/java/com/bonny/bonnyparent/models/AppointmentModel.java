package com.bonny.bonnyparent.models;

/**
 * @author Aditya Kulkarni
 */

public class AppointmentModel {
    private int id,baby;
    private String administered_on, status;
    private Administered_at administered_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBaby() {
        return baby;
    }

    public void setBaby(int baby) {
        this.baby = baby;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Administered_at getAdministered_at() {
        return administered_at;
    }

    public void setAdministered_at(Administered_at administered_at) {
        this.administered_at = administered_at;
    }

    public String getAdministered_on() {
        return administered_on;
    }

    public void setAdministered_on(String administered_on) {
        this.administered_on = administered_on;
    }

    public class Administered_at{
        private int id;
        private String name, address, email, contact;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }
    }
}
