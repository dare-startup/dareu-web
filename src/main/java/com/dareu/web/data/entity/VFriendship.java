package com.dareu.web.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "VFriendship")
@Table(name = "v_friendship")
public class VFriendship {

	@Id
	@Column(name = "user_id")
	private String id;

	@Column(name = "friend_id")
	private String friendId;

	@Column(name = "friend_name")
	private String friendName;

	@Column(name = "dare_count")
	private Long dareCount;

	@Column(name = "video_count")
	private Long videoCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public Long getDareCount() {
		return dareCount;
	}

	public void setDareCount(Long dareCount) {
		this.dareCount = dareCount;
	}

	public Long getVideoCount() {
		return videoCount;
	}

	public void setVideoCount(Long videoCount) {
		this.videoCount = videoCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dareCount == null) ? 0 : dareCount.hashCode());
		result = prime * result + ((friendId == null) ? 0 : friendId.hashCode());
		result = prime * result + ((friendName == null) ? 0 : friendName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((videoCount == null) ? 0 : videoCount.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VFriendship other = (VFriendship) obj;
		if (dareCount == null) {
			if (other.dareCount != null)
				return false;
		} else if (!dareCount.equals(other.dareCount))
			return false;
		if (friendId == null) {
			if (other.friendId != null)
				return false;
		} else if (!friendId.equals(other.friendId))
			return false;
		if (friendName == null) {
			if (other.friendName != null)
				return false;
		} else if (!friendName.equals(other.friendName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (videoCount == null) {
			if (other.videoCount != null)
				return false;
		} else if (!videoCount.equals(other.videoCount))
			return false;
		return true;
	}

}
