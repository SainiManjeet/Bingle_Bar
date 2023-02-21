package pojo;

import java.io.Serializable;

/**
 * Created by ErAshwini on 13/5/18.
 */

public class ResturantDetailFields implements Serializable {
    private String CompleteAddress;

    private String WifiPassword;

    private String DisplayAddress;

    private String TimingClose;

    private String TimingsOpen;

    private String LicenseNumber;

    private String RestaurantCode;

    private String Landmark;

    private String RestaurantId;

    private String CreditCardValidTill;

    private String LogoUrl;

    private AllRating[] AllRating;

    private String TagLine;

    private String MacID;

    private String Description;

    private String ContactNumber;

    private String CreditCardNumber;

    private String ContactPerson;

    private String Website;

    private String GeoLatitude;

    private String isDeleted;

    private String MailingAddress;

    private String Country;

    private String ZipCode;

    private String City;

    private String Name;

    private String Status;

    private String State;

    private String Rating;

    private String WifiName;

    private Images[] images;

    private String GeoLongitude;

    private String CVV;

    private String AddressLine1;

    private String AddressLine2;

    private String CoverUrl;

    private String BarStatus;

    private String DistanceInKMToShow;

    public String getDistanceInKMToShow() {
        return DistanceInKMToShow;
    }

    public void setDistanceInKMToShow(String distanceInKMToShow) {
        DistanceInKMToShow = distanceInKMToShow;
    }



    public Boolean getIsActiveStatus() {
        return IsActiveStatus;
    }

    public void setIsActiveStatus(Boolean isActiveStatus) {
        IsActiveStatus = isActiveStatus;
    }

    private Boolean IsActiveStatus;


    public String getAvailableStatus() {
        return AvailableStatus;
    }

    public void setAvailableStatus(String availableStatus) {
        AvailableStatus = availableStatus;
    }

    private String AvailableStatus;


    public String getCoverUrl() {
        return CoverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        CoverUrl = coverUrl;
    }

    public String getDisplayOpeningClosingTime() {
        return DisplayOpeningClosingTime;
    }

    public void setDisplayOpeningClosingTime(String displayOpeningClosingTime) {
        DisplayOpeningClosingTime = displayOpeningClosingTime;
    }

    private String AddressLine3;

    private String DisplayOpeningClosingTime;



    public String getCompleteAddress() {
        return CompleteAddress;
    }

    public void setCompleteAddress(String CompleteAddress) {
        this.CompleteAddress = CompleteAddress;
    }

    public String getWifiPassword() {
        return WifiPassword;
    }

    public void setWifiPassword(String WifiPassword) {
        this.WifiPassword = WifiPassword;
    }

    public String getDisplayAddress() {
        return DisplayAddress;
    }

    public void setDisplayAddress(String DisplayAddress) {
        this.DisplayAddress = DisplayAddress;
    }

    public String getTimingClose() {
        return TimingClose;
    }

    public void setTimingClose(String TimingClose) {
        this.TimingClose = TimingClose;
    }

    public String getTimingsOpen() {
        return TimingsOpen;
    }

    public void setTimingsOpen(String TimingsOpen) {
        this.TimingsOpen = TimingsOpen;
    }

    public String getLicenseNumber() {
        return LicenseNumber;
    }

    public void setLicenseNumber(String LicenseNumber) {
        this.LicenseNumber = LicenseNumber;
    }

    public String getRestaurantCode() {
        return RestaurantCode;
    }

    public void setRestaurantCode(String RestaurantCode) {
        this.RestaurantCode = RestaurantCode;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String Landmark) {
        this.Landmark = Landmark;
    }

    public String getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(String RestaurantId) {
        this.RestaurantId = RestaurantId;
    }

    public String getCreditCardValidTill() {
        return CreditCardValidTill;
    }

    public String getLogoUrl() {
        return LogoUrl;
    }

    public void setCreditCardValidTill(String CreditCardValidTill) {
        this.CreditCardValidTill = CreditCardValidTill;
    }

    public void setLogoUrl(String LogoUrl) {
        this.LogoUrl = LogoUrl;
    }

    public AllRating[] getAllRating() {
        return AllRating;
    }

    public void setAllRating(AllRating[] AllRating) {
        this.AllRating = AllRating;
    }

    public String getTagLine() {
        return TagLine;
    }

    public void setTagLine(String TagLine) {
        this.TagLine = TagLine;
    }

    public String getMacID() {
        return MacID;
    }

    public void setMacID(String MacID) {
        this.MacID = MacID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String ContactNumber) {
        this.ContactNumber = ContactNumber;
    }

    public String getCreditCardNumber() {
        return CreditCardNumber;
    }

    public void setCreditCardNumber(String CreditCardNumber) {
        this.CreditCardNumber = CreditCardNumber;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String ContactPerson) {
        this.ContactPerson = ContactPerson;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String Website) {
        this.Website = Website;
    }

    public String getGeoLatitude() {
        return GeoLatitude;
    }

    public void setGeoLatitude(String GeoLatitude) {
        this.GeoLatitude = GeoLatitude;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getMailingAddress() {
        return MailingAddress;
    }

    public void setMailingAddress(String MailingAddress) {
        this.MailingAddress = MailingAddress;
    }

    public String getCountry() {
        return Country;
    }

    public String getBarStatus() {
        return BarStatus;
    }

    public void setBarStatus(String barStatus) {
        BarStatus = barStatus;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String ZipCode) {
        this.ZipCode = ZipCode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String Rating) {
        this.Rating = Rating;
    }

    public String getWifiName() {
        return WifiName;
    }

    public void setWifiName(String WifiName) {
        this.WifiName = WifiName;
    }

    public Images[] getImages() {
        return images;
    }

    public void setImages(Images[] images) {
        this.images = images;
    }

    public String getGeoLongitude() {
        return GeoLongitude;
    }

    public void setGeoLongitude(String GeoLongitude) {
        this.GeoLongitude = GeoLongitude;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public void setAddressLine1(String AddressLine1) {
        this.AddressLine1 = AddressLine1;
    }

    public String getAddressLine2() {
        return AddressLine2;
    }

    public void setAddressLine2(String AddressLine2) {
        this.AddressLine2 = AddressLine2;
    }

    public String getAddressLine3() {
        return AddressLine3;
    }

    public void setAddressLine3(String AddressLine3) {
        this.AddressLine3 = AddressLine3;
    }

    @Override
    public String toString() {
        return "ClassPojo [CompleteAddress = " + CompleteAddress + ", WifiPassword = " + WifiPassword + ", DisplayAddress = " + DisplayAddress + ", TimingClose = " + TimingClose + ", TimingsOpen = " + TimingsOpen + ", LicenseNumber = " + LicenseNumber + ", RestaurantCode = " + RestaurantCode + ", Landmark = " + Landmark + ", RestaurantId = " + RestaurantId + ", CreditCardValidTill = " + CreditCardValidTill + ", LogoUrl = " + LogoUrl + ", AllRating = " + AllRating + ", TagLine = " + TagLine + ", MacID = " + MacID + ", Description = " + Description + ", ContactNumber = " + ContactNumber + ", CreditCardNumber = " + CreditCardNumber + ", ContactPerson = " + ContactPerson + ", Website = " + Website + ", GeoLatitude = " + GeoLatitude + ", isDeleted = " + isDeleted + ", MailingAddress = " + MailingAddress + ", Country = " + Country + ", ZipCode = " + ZipCode + ", City = " + City + ", Name = " + Name + ", Status = " + Status + ", State = " + State + ", Rating = " + Rating + ", WifiName = " + WifiName + ", images = " + images + ", GeoLongitude = " + GeoLongitude + ", CVV = " + CVV + ", AddressLine1 = " + AddressLine1 + ", AddressLine2 = " + AddressLine2 + ", AddressLine3 = " + AddressLine3 + "]";
    }

    public class Images implements Serializable {
        private String ImageName;

        private String ImageID;

        private String RestaurantId;

        private String ImageUrl;

        public String getImageName() {
            return ImageName;
        }

        public void setImageName(String ImageName) {
            this.ImageName = ImageName;
        }

        public String getImageID() {
            return ImageID;
        }

        public void setImageID(String ImageID) {
            this.ImageID = ImageID;
        }

        public String getRestaurantId() {
            return RestaurantId;
        }

        public void setRestaurantId(String RestaurantId) {
            this.RestaurantId = RestaurantId;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        @Override
        public String toString() {
            return "ClassPojo [ImageName = " + ImageName + ", ImageID = " + ImageID + ", RestaurantId = " + RestaurantId + ", ImageUrl = " + ImageUrl + "]";
        }
    }

    public class AllRating implements Serializable {
        private String userID;

        private String Comments;

        private String RestaurantId;

        private String Rating;

        private String UpdatedOn;

        private String CreatedOn;

        private String RatingId;

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getComments() {
            return Comments;
        }

        public void setComments(String Comments) {
            this.Comments = Comments;
        }

        public String getRestaurantId() {
            return RestaurantId;
        }

        public void setRestaurantId(String RestaurantId) {
            this.RestaurantId = RestaurantId;
        }

        public String getRating() {
            return Rating;
        }

        public void setRating(String Rating) {
            this.Rating = Rating;
        }

        public String getUpdatedOn() {
            return UpdatedOn;
        }

        public void setUpdatedOn(String UpdatedOn) {
            this.UpdatedOn = UpdatedOn;
        }

        public String getCreatedOn() {
            return CreatedOn;
        }

        public void setCreatedOn(String CreatedOn) {
            this.CreatedOn = CreatedOn;
        }

        public String getRatingId() {
            return RatingId;
        }

        public void setRatingId(String RatingId) {
            this.RatingId = RatingId;
        }

        @Override
        public String toString() {
            return "ClassPojo [userID = " + userID + ", Comments = " + Comments + ", RestaurantId = " + RestaurantId + ", Rating = " + Rating + ", UpdatedOn = " + UpdatedOn + ", CreatedOn = " + CreatedOn + ", RatingId = " + RatingId + "]";
        }
    }

}
