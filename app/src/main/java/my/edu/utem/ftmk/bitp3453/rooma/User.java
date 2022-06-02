package my.edu.utem.ftmk.bitp3453.rooma;

public class User {

    String Email;
    String Address;
    String FullName;
    String Password;
    String PhoneNum;
    String PictureURL;
    String Uid;
    String UserType;
    String Verify, IcURL, SelfieURL, RequestDate, RequestTime, RegisterDate;

    public String getRegisterDate() {
        return RegisterDate;
    }

    public void setRegisterDate(String registerDate) {
        RegisterDate = registerDate;
    }

    public String getIcURL() {
        return IcURL;
    }

    public void setIcURL(String icURL) {
        IcURL = icURL;
    }

    public String getSelfieURL() {
        return SelfieURL;
    }

    public void setSelfieURL(String selfieURL) {
        SelfieURL = selfieURL;
    }

    public String getRequestDate() {
        return RequestDate;
    }

    public void setRequestDate(String requestDate) {
        RequestDate = requestDate;
    }

    public String getRequestTime() {
        return RequestTime;
    }

    public void setRequestTime(String requestTime) {
        RequestTime = requestTime;
    }

    public String getVerify() {
        return Verify;
    }

    public void setVerify(String verify) {
        Verify = verify;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    String Status;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    public String getPictureURL() {
        return PictureURL;
    }

    public void setPictureURL(String picture_URL) {
        PictureURL = picture_URL;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
