package com.example.albawabacp.Moduls;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Ad {
    public ArrayList<String> imagesUrls;
    public String mainImageUrl;
    public String content;
    public String name;
    public String contact;
    public String address;
    public String detailedAddress;
    public long price;
    public ArrayList<Boolean> features;
    public String type;

    public Ad(ArrayList<String> imagesUrls, String mainImageUrl, String content, String name, String contact, String address, String detailedAddress, long price, ArrayList<Boolean> features, String type) {
        this.imagesUrls = imagesUrls;
        this.mainImageUrl = mainImageUrl;
        this.content = content;
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.price = price;
        this.features = features;
        this.type = type;
    }

    Ad() {

    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(ArrayList<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public ArrayList<Boolean> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<Boolean> features) {
        this.features = features;
    }
}