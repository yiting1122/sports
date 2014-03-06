package com.yhealthy.model;

import java.io.Serializable;

public class SportsContents implements Serializable {

	/**
	 * 
	 */
	
	private Long id;
	private String contentName;
	private String content;
	private String startTime;
	private String endTime;
	private Integer score;
	private String picPath;
	private Double latitude;
	private Double longitude;
	private int state;
	private String address;
	
	private Long userId;
	private Long sportsClassId;
	private String cityId;
	private String cityName;
	private String userName;
	private String sportsClassName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getSportsClassId() {
		return sportsClassId;
	}
	public void setSportsClassId(Long sportsClassId) {
		this.sportsClassId = sportsClassId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSportsClassName() {
		return sportsClassName;
	}
	public void setSportsClassName(String sportsClassName) {
		this.sportsClassName = sportsClassName;
	}
	
	
	
}
