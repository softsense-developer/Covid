//
//  UserLoginResponse.swift
//  covid
//
//  Created by SmartSense on 8.06.2021.
//

import Foundation

struct UserLoginResponse: BaseApiResponse, Codable{
    let userId: Int64?
    let token: String?
    let name: String?
    let surname: String?
    let phone: String?
    let roleId: Int?
    var code: String?
    var message: String?
    var errors: [String?]?
    
    init(userId: Int64?, token: String?, name: String?, surname: String?, phone: String?, roleId: Int?, code: String? = nil, message: String? = nil, errors: [String?]? = nil) {
        self.userId = userId
        self.token = token
        self.name = name
        self.surname = surname
        self.phone = phone
        self.roleId = roleId
        self.code = code
        self.message = message
        self.errors = errors
    }
    
    /*  let userID: Int
     let token, name, surname, phone: String
     let roleID: Int
     let code, message: String
     let errors: [String]
     
     enum CodingKeys: String, CodingKey {
     case userID = "userId"
     case token, name, surname, phone
     case roleID = "roleId"
     case code, message, errors
     }*/
}
