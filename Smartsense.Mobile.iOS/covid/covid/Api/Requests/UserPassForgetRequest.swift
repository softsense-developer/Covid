//
//  UserLoginRequest.swift
//  covid
//
//  Created by SmartSense on 9.06.2021.
//

import Foundation

struct UserPassForgetRequest: BaseApiRequest, Encodable {
    private(set) var email: String?
    var userId: Int64?
    
    init(email: String? = nil) {
        self.email = email
    }
    
}

