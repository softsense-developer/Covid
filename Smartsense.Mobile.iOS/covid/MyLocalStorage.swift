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
    
    /*public static var userToken: String{
        set{
            UserDefaults.standard.setValue(newValue, forKey: USER_TOKEN)
        }
        get{
            return UserDefaults.standard.string(forKey: USER_TOKEN) ?? ""
        }
    }*/
    
    public static var userToken: String{
        set{
            keychain.set(newValue, forKey: USER_TOKEN)
        }
        get{
            return keychain.get(USER_TOKEN) ?? ""
        }
    }
}
