//
//  UserLoginRequest.swift
//  covid
//
//  Created by SmartSense on 9.06.2021.
//

import Foundation

struct UserRegisterRequest: BaseApiRequest, Encodable {
    private(set) var name: String?
    private(set) var surname: String?
    private(set) var email: String?
    private(set) var password: String?
    var userId: Int64?
    
    internal init(name: String? = nil, surname: String? = nil, email: String? = nil, password: String? = nil) {
        self.name = name
        self.surname = surname
        self.email = email
        self.password = password
    }
}

