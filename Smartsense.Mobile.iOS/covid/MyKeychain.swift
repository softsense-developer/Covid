//
//  MyLocalStorage.swift
//  covid
//
//  Created by SmartSense on 10.06.2021.
//

import Foundation
import KeychainSwift

class MyKeychain{
    
    private static let keychain = KeychainSwift()
    
    private static let USER_TOKEN: String = "USER_TOKEN"
    private static let USER_EMAIL: String = "USER_EMAIL"
    private static let USER_PASSWORD: String = "USER_PASSWORD"
    private static let USER_NAME: String = "USER_NAME"
    private static let USER_SURNAME: String = "USER_SURNAME"
    private static let USER_PHONE: String = "USER_PHONE"
    private static let USER_ID: String = "USER_ID"
    private static let USER_ROLE_ID: String = "USER_ROLE_ID"
    private static let USER_BIRTHDAY: String = "USER_BIRTHDAY"
    private static let USER_IDENTITY: String = "USER_IDENTITY"
    private static let USER_GENDER: String = "USER_GENDER"
    private static let USER_TRACKING_TYPE: String = "USER_TRACKING_TYPE"
    private static let USER_DIAGNOSIS: String = "USER_DIAGNOSIS"
    private static let DOCTOR_ID: String = "DOCTOR_ID"
    private static let DOCTOR_NAME_SURNAME: String = "DOCTOR_NAME_SURNAME"
    private static let HOSPITAL_NAME: String = "HOSPITAL_NAME"
    private static let USER_BLOOD_GROUP: String = "USER_BLOOD_GROUP"
    private static let IS_EMAIL_VERIFIED: String = "IS_EMAIL_VERIFIED"
    private static let QUARANTINE_STATUS: String = "QUARANTINE_STATUS"
    
    /*public static var userToken: String{
        set{
            UserDefaults.standard.setValue(newValue, forKey: USER_TOKEN)
        }
        get{
            return UserDefaults.standard.string(forKey: USER_TOKEN) ?? ""
        }
    }*/
    
    public static func clear(){
        keychain.clear()
    }
    
    public static var userToken: String{
        set{
            keychain.set(newValue, forKey: USER_TOKEN)
        }
        get{
            return keychain.get(USER_TOKEN) ?? ""
        }
    }
    
    public static var userEmail: String{
        set{
            keychain.set(newValue, forKey: USER_EMAIL)
        }
        get{
            return keychain.get(USER_EMAIL) ?? ""
        }
    }
    
    public static var userPassword: String{
        set{
            keychain.set(newValue, forKey: USER_PASSWORD)
        }
        get{
            return keychain.get(USER_PASSWORD) ?? ""
        }
    }
    
    public static var userName: String{
        set{
            keychain.set(newValue, forKey: USER_NAME)
        }
        get{
            return keychain.get(USER_NAME) ?? ""
        }
    }
    
    public static var userSurname: String{
        set{
            keychain.set(newValue, forKey: USER_SURNAME)
        }
        get{
            return keychain.get(USER_SURNAME) ?? ""
        }
    }
    
    public static var userPhone: String{
        set{
            keychain.set(newValue, forKey: USER_PHONE)
        }
        get{
            return keychain.get(USER_PHONE) ?? ""
        }
    }
    
    public static var userID: String{
        set{
            keychain.set(newValue, forKey: USER_ID)
        }
        get{
            return keychain.get(USER_ID) ?? "-1"
        }
    }
    
    public static var userRoleID: String{
        set{
            keychain.set(newValue, forKey: USER_ROLE_ID)
        }
        get{
            return keychain.get(USER_ROLE_ID) ?? ""
        }
    }
    
    public static var userBirthday: String{
        set{
            keychain.set(newValue, forKey: USER_BIRTHDAY)
        }
        get{
            return keychain.get(USER_BIRTHDAY) ?? ""
        }
    }
    
    public static var userIdentity: String{
        set{
            keychain.set(newValue, forKey: USER_IDENTITY)
        }
        get{
            return keychain.get(USER_IDENTITY) ?? ""
        }
    }
    
    public static var userGender: String{
        set{
            keychain.set(newValue, forKey: USER_GENDER)
        }
        get{
            return keychain.get(USER_GENDER) ?? ""
        }
    }
    
    public static var userTrackingType: String{
        set{
            keychain.set(newValue, forKey: USER_TRACKING_TYPE)
        }
        get{
            return keychain.get(USER_TRACKING_TYPE) ?? ""
        }
    }
    
    public static var userDiagnosis: String{
        set{
            keychain.set(newValue, forKey: USER_DIAGNOSIS)
        }
        get{
            return keychain.get(USER_DIAGNOSIS) ?? ""
        }
    }
    
    public static var userDoctorID: String{
        set{
            keychain.set(newValue, forKey: DOCTOR_ID)
        }
        get{
            return keychain.get(DOCTOR_ID) ?? ""
        }
    }
    
    public static var userDoctorNameSurname: String{
        set{
            keychain.set(newValue, forKey: DOCTOR_NAME_SURNAME)
        }
        get{
            return keychain.get(DOCTOR_NAME_SURNAME) ?? ""
        }
    }
    
    public static var userHospitalName: String{
        set{
            keychain.set(newValue, forKey: HOSPITAL_NAME)
        }
        get{
            return keychain.get(HOSPITAL_NAME) ?? ""
        }
    }
    
    public static var userBloodGroup: String{
        set{
            keychain.set(newValue, forKey: USER_BLOOD_GROUP)
        }
        get{
            return keychain.get(USER_BLOOD_GROUP) ?? ""
        }
    }
    
    public static var isEmailVerified: Bool{
        set{
            keychain.set(newValue, forKey: IS_EMAIL_VERIFIED)
        }
        get{
            return (keychain.get(IS_EMAIL_VERIFIED) != nil)
        }
    }
    
    public static var quarantineStatus: Bool{
        set{
            keychain.set(newValue, forKey: QUARANTINE_STATUS)
        }
        get{
            return (keychain.get(QUARANTINE_STATUS) != nil)
        }
    }
    
    
    
    
    
}
