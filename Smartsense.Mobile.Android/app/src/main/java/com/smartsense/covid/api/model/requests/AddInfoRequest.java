package com.smartsense.covid.api.model.requests;

public class AddInfoRequest extends BaseApiRequest{

    private String identityNumber;
    private int gender;
    private String dateOfBirth;
    private int userStatus;
    private String diagnosis;


    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    @Override
    public String toString() {
        return "AddInfoRequest{" +
                "identityNumber='" + identityNumber + '\'' +
                ", gender=" + gender +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", userStatus=" + userStatus +
                ", diagnosis='" + diagnosis + '\'' +
                '}';
    }
}
