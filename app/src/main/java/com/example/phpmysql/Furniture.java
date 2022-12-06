package com.example.phpmysql;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Furniture {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("detail")
@Expose
private String detail;
@SerializedName("price")
@Expose
private Integer price;
@SerializedName("image")
@Expose
private String image;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getDetail() {
return detail;
}

public void setDetail(String detail) {
this.detail = detail;
}

public Integer getPrice() {
return price;
}

public void setPrice(Integer price) {
this.price = price;
}

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}



}