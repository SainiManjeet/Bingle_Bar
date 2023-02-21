package pojo;

/**
 * Created by vineet on 16/5/18.
 */

public class RestaurantPhotos {

    private String ImageId;

    private String ImageName;

    private String RestaurantId;

    private String IsDeleted;

    private String ImageUrl;

    public String getImageId ()
    {
        return ImageId;
    }

    public void setImageId (String ImageId)
    {
        this.ImageId = ImageId;
    }

    public String getImageName ()
    {
        return ImageName;
    }

    public void setImageName (String ImageName)
    {
        this.ImageName = ImageName;
    }

    public String getRestaurantId ()
    {
        return RestaurantId;
    }

    public void setRestaurantId (String RestaurantId)
    {
        this.RestaurantId = RestaurantId;
    }

    public String getIsDeleted ()
    {
        return IsDeleted;
    }

    public void setIsDeleted (String IsDeleted)
    {
        this.IsDeleted = IsDeleted;
    }

    public String getImageUrl ()
    {
        return ImageUrl;
    }

    public void setImageUrl (String ImageUrl)
    {
        this.ImageUrl = ImageUrl;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ImageId = "+ImageId+", ImageName = "+ImageName+", RestaurantId = "+RestaurantId+", IsDeleted = "+IsDeleted+", ImageUrl = "+ImageUrl+"]";
    }
}
