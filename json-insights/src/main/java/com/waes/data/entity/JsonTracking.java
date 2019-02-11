package com.waes.data.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class JsonTracking mapped to database table JSON_TRACKING
 * 
 * @author Alejandro Aguirre
 * @version 1.0
 * @since 2019/02/10
 */
@Entity
@Table(name = "JSON_TRACKING")
public class JsonTracking {

	@Id
	@Column(name = "JSON_TRACKING_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "JSON_ID")
	private String jsonId;
	@Column(name = "BASE64_ENCODED_JSON")
	private String base64EncodedJson;
	@Column(name = "SIDE")
	private String side;
	@Column(name = "CREATED_DATE")
	private LocalDate createdDate;
	@Column(name = "UPDATED_DATE")
	private LocalDate updatedDate;

	public JsonTracking(Long id, String jsonId, String base64EncodedJson, String side, LocalDate createdDate,
			LocalDate updatedDate) {
		super();
		this.id = id;
		this.jsonId = jsonId;
		this.base64EncodedJson = base64EncodedJson;
		this.side = side;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	public JsonTracking() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the base64EncodedJson
	 */
	public String getBase64EncodedJson() {
		return base64EncodedJson;
	}

	/**
	 * @param base64EncodedJson the base64EncodedJson to set
	 */
	public void setBase64EncodedJson(String base64EncodedJson) {
		this.base64EncodedJson = base64EncodedJson;
	}

	/**
	 * @return the side
	 */
	public String getSide() {
		return side;
	}

	/**
	 * @param side the side to set
	 */
	public void setSide(String side) {
		this.side = side;
	}

	/**
	 * @return the createdDate
	 */
	public LocalDate getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the updatedDate
	 */
	public LocalDate getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(LocalDate updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the jsonId
	 */
	public String getJsonId() {
		return jsonId;
	}

	/**
	 * @param jsonId the jsonId to set
	 */
	public void setJsonId(String jsonId) {
		this.jsonId = jsonId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JsonTracking [id=" + id + ", jsonId=" + jsonId + ", base64EncodedJson=" + base64EncodedJson + ", side="
				+ side + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + "]";
	}

}
