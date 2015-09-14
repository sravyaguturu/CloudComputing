package com.latlong;

// TODO: Auto-generated Javadoc
/**
 * The Class GoogleResponse.
 */
public class GoogleResponse {

	/** The results. */
	private Result[] results;
	
	/** The status. */
	private String status;

	/**
	 * Gets the results.
	 *
	 * @return the results
	 */
	public Result[] getResults() {
		return results;
	}

	/**
	 * Sets the results.
	 *
	 * @param results the new results
	 */
	public void setResults(Result[] results) {
		this.results = results;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
