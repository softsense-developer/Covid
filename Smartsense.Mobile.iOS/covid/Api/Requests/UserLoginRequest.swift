//
//  UserLoginRequest.swift
//  covid
//
//  Created by SmartSense on 9.06.2021.
//

import Foundation

struct UserLoginRequest: BaseApiRequest, Encodable {
    private(set) var email: String?
    private(set) var password: String?
    private(set) var isRemember: Bool?
    var userId: Int64?
    
    init(email: String? = nil, password: String? = nil, isRemember: Bool? = nil) {
        self.email = email
        self.password = password
        self.isRemember = isRemember
    }
    
}

