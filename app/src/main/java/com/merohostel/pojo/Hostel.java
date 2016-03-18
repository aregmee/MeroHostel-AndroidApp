package com.merohostel.pojo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asimsangram on 2/4/16.
 */
public class Hostel implements Serializable{

    public enum Gender{

        BOYS, GIRLS
    }

    private int hid;
    private String name;
    private String location;
    private Gender gender;
    private List<Photo> photoList;
    private FeeStructure feeStructure;
    private List<Facility> facilities;

    public Hostel(JSONObject hostelJSONObject) throws Exception {

        this.hid = hostelJSONObject.getInt("id");
        this.name = hostelJSONObject.getString("name");
        this.location = hostelJSONObject.getString("address");
        if(hostelJSONObject.getString("gender").equalsIgnoreCase("Boys"))
            this.gender = Gender.BOYS;
        else if(hostelJSONObject.getString("gender").equalsIgnoreCase("Girls"))
            this.gender = Gender.GIRLS;
        else
            throw new Exception("Invalid Gender: " + hostelJSONObject.getString("gender"));

        feeStructure = new FeeStructure(hostelJSONObject.getJSONObject("fee_structure"));
        facilities = new ArrayList<>();

        JSONArray facilityJSONArray = hostelJSONObject.getJSONArray("facilities");

        for(int i = 0; i < facilityJSONArray.length(); i++){

            JSONObject facilityJSONObject = facilityJSONArray.getJSONObject(i);
            facilities.add(new Facility(facilityJSONObject.getInt("id"), facilityJSONObject.getString("facility")));
        }

        JSONArray photoJSONArray = hostelJSONObject.getJSONArray("photos");

        photoList = new ArrayList<>();
        for(int i = 0; i < photoJSONArray.length(); i++){

            JSONObject photoJSONObject = photoJSONArray.getJSONObject(i);
            photoList.add(new Photo(photoJSONObject));
        }
    }

    public Hostel(int id, String name, String location, String gender, List<Photo> photoList) throws Exception {

        this.hid = id;
        this.name = name;
        this.location = location;
        if(gender.equalsIgnoreCase("BOYS"))
            this.gender = Gender.BOYS;
        else if(gender.equalsIgnoreCase("GIRLS"))
            this.gender = Gender.GIRLS;
        else throw new Exception("Invalid Gender: " + gender);
        this.photoList = photoList;
    }

    public Hostel(int hid, String name, String location, String gender, List<Photo> photoList, FeeStructure feeStructure) throws Exception {
        this.hid = hid;
        this.name = name;
        this.location = location;
        if(gender.equalsIgnoreCase("BOYS"))
            this.gender = Gender.BOYS;
        else if(gender.equalsIgnoreCase("GIRLS"))
            this.gender = Gender.GIRLS;
        else throw new Exception("Invalid Gender: " + gender);
        this.photoList = photoList;
        this.feeStructure = feeStructure;
    }

    public Hostel(int hid, String name, String location, String gender, List<Photo> photoList, FeeStructure feeStructure, List<Facility> facilities) throws Exception{
        this.hid = hid;
        this.name = name;
        this.location = location;
        if(gender.equalsIgnoreCase("BOYS"))
            this.gender = Gender.BOYS;
        else if(gender.equalsIgnoreCase("GIRLS"))
            this.gender = Gender.GIRLS;
        else throw new Exception("Invalid Gender: " + gender);
        this.photoList = photoList;
        this.feeStructure = feeStructure;
        this.facilities = facilities;
    }

    public int getHid(){

        return hid;
    }

    public String getName(){

        return name;
    }

    public String getLocation(){

        return location;
    }

    public Gender getGender(){

        return gender;
    }

    public List<Photo> getPhotoList() throws NullPointerException{

        return photoList;
    }

    public FeeStructure getFeeStructure() {
        return feeStructure;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public Photo getMainPhoto() throws NullPointerException{

        for (Photo photo : getPhotoList()) {

            if (photo.isMain())
                return photo;
        }

        throw new NullPointerException("No main photo");
    }

    public boolean hasMainPhoto() throws NullPointerException{

        return getMainPhoto() != null;
    }

    @Override
    public String toString() {
        return "Hostel{" +
                "hid=" + hid +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", gender=" + gender.toString() +
                ", photoList=" + photoList.toString() +
                '}';
    }
}
