
package com.mobilesensing.system.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "convertFromLatLongResponse", namespace = "http://system.mobilesensing.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "convertFromLatLongResponse", namespace = "http://system.mobilesensing.com/")
public class ConvertFromLatLongResponse {

    @XmlElement(name = "return", namespace = "")
    private com.latlong.GoogleResponse _return;

    /**
     * 
     * @return
     *     returns GoogleResponse
     */
    public com.latlong.GoogleResponse getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(com.latlong.GoogleResponse _return) {
        this._return = _return;
    }

}
