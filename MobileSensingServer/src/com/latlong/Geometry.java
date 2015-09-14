package com.latlong;

import org.codehaus.jackson.annotate.JsonIgnore;

// TODO: Auto-generated Javadoc
/**
 * The Class Geometry.
 *
 * @author Swetha
 */
public class Geometry {

	/** The location. */
	private Location location;

	/** The location_type. */
	private String location_type;

	/** The bounds. */
	@JsonIgnore
	private Object bounds;

	/** The viewport. */
	@JsonIgnore
	private Object viewport;

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * Gets the location_type.
	 *
	 * @return the location_type
	 */
	public String getLocation_type() {
		return location_type;
	}

	/**
	 * Sets the location_type.
	 *
	 * @param location_type the new location_type
	 */
	public void setLocation_type(String location_type) {
		this.location_type = location_type;
	}

	/**
	 * Gets the bounds.
	 *
	 * @return the bounds
	 */
	public Object getBounds() {
		return bounds;
	}

	/**
	 * Sets the bounds.
	 *
	 * @param bounds the new bounds
	 */
	public void setBounds(Object bounds) {
		this.bounds = bounds;
	}

	/**
	 * Gets the viewport.
	 *
	 * @return the viewport
	 */
	public Object getViewport() {
		return viewport;
	}

	/**
	 * Sets the viewport.
	 *
	 * @param viewport the new viewport
	 */
	public void setViewport(Object viewport) {
		this.viewport = viewport;
	}

}
