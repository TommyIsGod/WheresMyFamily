package dk.projekt.bachelor.wheresmyfamily.DataModel;

/**
 * Created by Tommy on 25-11-2014.
 */

import java.io.Serializable;

/**
 * A WmfGeofence object, defined by its center and radius.
 * It also holds a name, an expiration duration
 * and which transition types to monitor
 */
public class WmfGeofence implements Serializable
{

    //region Fields
    private String geofenceId;
    private double latitude;
    private double longitude;
    private float radius;
    private long expirationDuration;
    private int transitionType;
    private Boolean isActive;
    private Boolean isCurrent;
    //endregion

    public WmfGeofence() {}

    public WmfGeofence(String _geofenceId, double _latitude, double _longitude, float _radius,
                       long _expirationDuration, int _transitionType, Boolean _isActive, Boolean _isCurrent)
    {
        setGeofenceId(_geofenceId);
        setLatitude(_latitude);
        setLongitude(_longitude);
        setRadius(_radius);
        setExpirationDuration(_expirationDuration);
        setTransitionType(_transitionType);
        setIsActive(false);
        setIsCurrent(false);
    }

    // region Get and set
    public int getTransitionType() {
        return transitionType;
    }
    public void setTransitionType(int transitionType) {
        this.transitionType = transitionType;
    }
    public long getExpirationDuration() {
        return expirationDuration;
    }
    public void setExpirationDuration(long expirationDuration) {
        this.expirationDuration = expirationDuration;
    }
    public float getRadius() {
        return radius;
    }
    public void setRadius(float radius) {
        this.radius = radius;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public String getGeofenceId() {
        return geofenceId;
    }
    public void setGeofenceId(String geofenceId) {
        this.geofenceId = geofenceId;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    public Boolean getIsCurrent() {
        return isCurrent;
    }
    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }
    //endregion


    public com.google.android.gms.location.Geofence toGeofence()
    {
        // Create a new google Location geofence
        return new com.google.android.gms.location.Geofence.Builder().setRequestId(getGeofenceId()).setTransitionTypes(transitionType)
                .setCircularRegion(getLatitude(), getLongitude(), getRadius())
                .setExpirationDuration(getExpirationDuration()).build();
    }
}