//
//  UserLoginRequest.swift
//  covid
//
//  Created by SmartSense on 9.06.2021.
//

import Foundation

struct UserInfoChangeRequest: BaseApiRequest, Encodable {
    private(set) var name: String?
    private(set) var surname: String?
    private(set) var phone: String?
    var userId: Int64?
    
    init(name: String? = nil, surname: String? = nil, phone: String? = nil) {
        self.name = name
        self.surname = surname
        self.phone = phone
    }
}

