package com.latlong;

import org.codehaus.jackson.annotate.JsonIgnore;

// TODO: Auto-generated Javadoc
/**
 * The Class Result.
 */
public class Result {

	/** The formatted_address. */
	private String formatted_address;
	
	/** The partial_match. */
	private boolean partial_match;
	
	/** The address count. */
	public int addressCount;
	
	/** The geometry. */
	private Geometry geometry;

	/** The address_components. */
	@JsonIgnore
	private Object address_components;

	/** The types. */
	@JsonIgnore
	private Object types;

	/**
	 * Gets the formatted_address.
	 *
	 * @return the formatted_address
	 */
	public String getFormatted_address() {
		return formatted_address;
	}

	/**
	 * Sets the formatted_address.
	 *
	 * @param formatted_address the new formatted_address
	 */
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	/**
	 * Checks if is partial_match.
	 *
	 * @return true, if is partial_match
	 */
	public boolean isPartial_match() {
		return partial_match;
	}

	/**
	 * Sets the partial_match.
	 *
	 * @param partial_match the new partial_match
	 */
	public void setPartial_match(boolean partial_match) {
		this.partial_match = partial_match;
	}

	/**
	 * Gets the geometry.
	 *
	 * @return the geometry
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Sets the geometry.
	 *
	 * @param geometry the new geometry
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * Gets the address_components.
	 *
	 * @return the address_components
	 */
	public Object getAddress_components() {
		return address_components;
	}

	/**
	 * Sets the address_components.
	 *
	 * @param address_components the new address_components
	 */
	public void setAddress_components(Object address_components) {
		this.address_components = address_components;
	}

	/**
	 * Gets the types.
	 *
	 * @return the types
	 */
	public Object getTypes() {
		return types;
	}

	/**
	 * Sets the types.
	 *
	 * @param types the new types
	 */
	public void setTypes(Object types) {
		this.types = types;
	}
}