package model;

import java.util.Date;
import java.util.UUID;

public class Contact {

    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Date date;
    private String category;
    private Boolean favorite;

    public Contact(String firstName, String lastName, String phone, String email, String category, Boolean favorite) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.date = new Date();
        this.category = category;
        this.favorite = favorite;
    }

    public Contact(String id, String firstName, String lastName, String phone, String email, String category, Boolean favorite) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.date = new Date();
        this.category = category;
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Detalles del registro: [ Nombre: " + firstName + ", Apellido: " + lastName + ", Teléfono: " + phone + ", Email: " + email + ", Categoría: " + category + ", ¿Es favorito?" + (favorite ? "Sí" : "No") + ", Fecha de registro: " + date + "]";
    }

}
