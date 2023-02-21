package pojo;

import java.io.Serializable;

/**
 * Created by vineet on 17/5/18.
 */

public class RestaurantPhotosAPI implements Serializable
{
    private String message;

    private RestaurantPhotos[] data;

    private String success;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public RestaurantPhotos[] getData ()
    {
        return data;
    }

    public void setData (RestaurantPhotos[] data)
    {
        this.data = data;
    }

    public String getSuccess ()
    {
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", data = "+data+", success = "+success+"]";
    }
}