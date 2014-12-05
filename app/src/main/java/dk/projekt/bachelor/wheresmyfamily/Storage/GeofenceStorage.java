package dk.projekt.bachelor.wheresmyfamily.Storage;

/**
 * Created by Tommy on 25-11-2014.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dk.projekt.bachelor.wheresmyfamily.DataModel.WmfGeofence;

/**
 * Storage for geofence values, implemented in SharedPreferences.
 */
public class GeofenceStorage
{
    //region Fields
    // Keys for flattened geofences stored in SharedPreferences
    public static final String KEY_LATITUDE = "dk.projekt.bachelor.wheresmyfamily.KEY_LATITUDE";
    public static final String KEY_LONGITUDE = "dk.projekt.bachelor.wheresmyfamily.KEY_LONGITUDE";
    public static final String KEY_RADIUS = "dk.projekt.bachelor.wheresmyfamily.KEY_RADIUS";
    public static final String KEY_EXPIRATION_DURATION = "dk.projekt.bachelor.wheresmyfamily.KEY_EXPIRATION_DURATION";
    public static final String KEY_TRANSITION_TYPE = "dk.projekt.bachelor.wheresmyfamily.KEY_TRANSITION_TYPE";
    // The prefix for flattened geofence keys
    public static final String KEY_PREFIX = "dk.projekt.bachelor.wheresmyfamily.KEY";

    private static final String PREFIX = "json";
    public static final String GEOFENCE_PREFS_NAME = "GEOFENCE_PREFS";
    public static final String GEOFENCE_FAVORITES = "GEOFENCE_FAVORITES";
    /*
    * Invalid values, used to test geofence storage when
    * retrieving geofences
    */
    public static final long INVALID_LONG_VALUE = -999l;
    public static final float INVALID_FLOAT_VALUE = -999.0f;
    public static final int INVALID_INT_VALUE = -999;
    // The SharedPreferences object in which geofences are stored
    private SharedPreferences geofencePrefs;
    // The name of the SharedPreferences
    private static final String SHARED_PREFERENCES = "GeofencePreferences";
    //endregion

    // Create the SharedPreferences storage with private access only
    public GeofenceStorage(Context context)
    {
        geofencePrefs = context.getSharedPreferences(GEOFENCE_PREFS_NAME, Context.MODE_PRIVATE);
    }

    // These eight methods are used for maintaining favorites.
    public ArrayList<WmfGeofence> getGeofences(Context context) {
        List<WmfGeofence> geofences;
        geofencePrefs = context.getSharedPreferences(GEOFENCE_PREFS_NAME, Context.MODE_PRIVATE);

        if (!geofencePrefs.contains(GEOFENCE_FAVORITES)) {
            return new ArrayList<WmfGeofence>();
        } else {
            String jsonFavorites = geofencePrefs.getString(GEOFENCE_FAVORITES, null);
            Gson gson = new Gson();
            WmfGeofence[] favoriteItems = gson.fromJson(jsonFavorites,
                    WmfGeofence[].class);

            geofences = Arrays.asList(favoriteItems);
            geofences = new ArrayList<WmfGeofence>(geofences);

            return (ArrayList<WmfGeofence>) geofences;
        }
    }

    public void setGeofences(Context context, List<WmfGeofence> geofences)
    {
        geofencePrefs = context.getSharedPreferences(GEOFENCE_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = geofencePrefs.edit();

        Gson gson = new Gson();
        String jsonParentFavorites = gson.toJson(geofences);

        editor.putString(GEOFENCE_FAVORITES, jsonParentFavorites);

        editor.commit();
    }

    /**
     * Returns a stored geofence by its id, or returns null
     * if it's not found.
     *
     * @param id The ID of a stored geofence
     * @return A geofence defined by its center and radius. See
     */
    public WmfGeofence getGeofence(String id)
    {
            /*
            * Get the latitude for the geofence identified by id, or
            * INVALID_FLOAT_VALUE if it doesn't exist
            */
        double lat = geofencePrefs.getFloat(getGeofenceFieldKey(id, KEY_LATITUDE), INVALID_FLOAT_VALUE);
            /*
             * Get the longitude for the geofence identified by id, or
             * INVALID_FLOAT_VALUE if it doesn't exist
             */
        double lng = geofencePrefs.getFloat(getGeofenceFieldKey(id, KEY_LONGITUDE), INVALID_FLOAT_VALUE);
            /*
             * Get the radius for the geofence identified by id, or
             * INVALID_FLOAT_VALUE if it doesn't exist
             */
        float radius = geofencePrefs.getFloat(getGeofenceFieldKey(id, KEY_RADIUS), INVALID_FLOAT_VALUE);
            /*
             * Get the expiration duration for the geofence identified
             * by id, or INVALID_LONG_VALUE if it doesn't exist
             */
        long expirationDuration =
                geofencePrefs.getLong(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION), INVALID_LONG_VALUE);
            /*
             * Get the transition type for the geofence identified by
             * id, or INVALID_INT_VALUE if it doesn't exist
             */
        int transitionType = geofencePrefs.getInt(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE),
                INVALID_INT_VALUE);
        // If none of the values is incorrect, return the object
        if (
                lat != GeofenceStorage.INVALID_FLOAT_VALUE &&
                        lng != GeofenceStorage.INVALID_FLOAT_VALUE &&
                        radius != GeofenceStorage.INVALID_FLOAT_VALUE &&
                        expirationDuration != GeofenceStorage.INVALID_LONG_VALUE &&
                        transitionType != GeofenceStorage.INVALID_INT_VALUE
                )
        {
            // Return a true WmfGeofence object
            return new WmfGeofence(id, lat, lng, radius, expirationDuration, transitionType, false);
            // Otherwise, return null.
        }
        else
            return null;
    }
    /**
     * Save a wmfGeofence.
     * @param wmfGeofence The WmfGeofence containing the
     * values you want to save in SharedPreferences
     */
    public void setGeofence(String id, WmfGeofence wmfGeofence)
    {
            /*
             * Get a SharedPreferences editor instance. Among other
             * things, SharedPreferences ensures that updates are atomic
             * and non-concurrent
             */
        SharedPreferences.Editor editor = geofencePrefs.edit();
        // Write the WmfGeofence values to SharedPreferences
        editor.putFloat(getGeofenceFieldKey(id, KEY_LATITUDE), (float) wmfGeofence.getLatitude());
        editor.putFloat(getGeofenceFieldKey(id, KEY_LONGITUDE), (float) wmfGeofence.getLongitude());
        editor.putFloat(getGeofenceFieldKey(id, KEY_RADIUS), wmfGeofence.getRadius());
        editor.putLong(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION),
                wmfGeofence.getExpirationDuration());
        editor.putInt(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE), wmfGeofence.getTransitionType());
        // Commit the changes
        editor.commit();
    }

    public void clearGeofence(String id) {
            /*
             * Remove a flattened geofence object from storage by
             * removing all of its keys
             */
        SharedPreferences.Editor editor = geofencePrefs.edit();
        editor.remove(getGeofenceFieldKey(id, KEY_LATITUDE));
        editor.remove(getGeofenceFieldKey(id, KEY_LONGITUDE));
        editor.remove(getGeofenceFieldKey(id, KEY_RADIUS));
        editor.remove(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION));
        editor.remove(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE));
        editor.commit();
    }
    /**
     * Given a WmfGeofence object's ID and the name of a field
     * (for example, KEY_LATITUDE), return the key name of the
     * object's values in SharedPreferences.
     *
     * @param id The ID of a WmfGeofence object
     * @param fieldName The field represented by the key
     * @return The full key name of a value in SharedPreferences
     */
    private String getGeofenceFieldKey(String id, String fieldName)
    {
        return KEY_PREFIX + "_" + id + "_" + fieldName;
    }
}