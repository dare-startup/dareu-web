package com.dareu.web.data.response;

public class FriendshipResponse {

	private String id;
	private String name;
	
	private Long dareCount;
	private Long videoResponsesCount;
	
	public FriendshipResponse(){}
	public FriendshipResponse(final String id, final String name){
		this.id = id;
		this.name = name;
	}
	public FriendshipResponse(final String id, final String name, Long dareCount){
		this.id = id;
		this.name = name;
		this.dareCount = dareCount;
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
	public Long getDareCount() {
		return dareCount;
	}
	public void setDareCount(Long dareCount) {
		this.dareCount = dareCount;
	}
	public Long getVideoResponsesCount() {
		return videoResponsesCount;
	}
	public void setVideoResponsesCount(Long videoResponsesCount) {
		this.videoResponsesCount = videoResponsesCount;
	}
	
	
}
