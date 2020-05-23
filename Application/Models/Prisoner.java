package Application.Models;

import java.sql.Date;
import java.util.List;

public class Prisoner {
    private int id;
    private String first_name;
    private String last_name;
    private int punishment;
    private int room;
    private int state;
    private int city;
    private int gender; // MALE: 0, FEMALE: 1
    private Date DOA; // date of arrest
    private Date DOB; // date of birth
    private String address;
    private List<Integer> crimes;
    private List<String> photos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getPunishment() {
        return punishment;
    }

    public void setPunishment(int punishment) {
        this.punishment = punishment;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getDOA() {
        return DOA;
    }

    public void setDOA(Date DOA) {
        this.DOA = DOA;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Integer> getCrimes() {
        return crimes;
    }

    public void setCrimes(List<Integer> crimes) {
        this.crimes = crimes;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}
