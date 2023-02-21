package pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class RestaurantDetailsByLatLong implements Serializable {

    private String message;

    private String count;

    public ArrayList <ResturantDetailFields> getData() {
        return data;
    }

    public void setData(ArrayList <ResturantDetailFields> data) {
        this.data = data;
    }

    private ArrayList <ResturantDetailFields> data = new ArrayList <>();

    private String success;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getCount ()
    {
        return count;
    }

    public void setCount (String count)
    {
        this.count = count;
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
        return "ClassPojo [message = "+message+", count = "+count+", data = "+data+", success = "+success+"]";
    }


}
