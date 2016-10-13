package com.dareu.web.data.response;

public class FriendshipResponse {

	private String id;
	private String name;
	
	private Integer dareCount;
	private Integer videoResponsesCount;
	
	public FriendshipResponse(){}
	public FriendshipResponse(final String id, final String name){
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getDareCount() {
		return dareCount;
	}
	public void setDareCount(Integer dareCount) {
		this.dareCount = dareCount;
	}
	public Integer getVideoResponsesCount() {
		return videoResponsesCount;
	}
	public void setVideoResponsesCount(Integer videoResponsesCount) {
		this.videoResponsesCount = videoResponsesCount;
	}
	
	
}
