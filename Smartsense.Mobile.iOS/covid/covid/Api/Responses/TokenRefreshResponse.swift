//
//  UserLoginResponse.swift
//  covid
//
//  Created by SmartSense on 8.06.2021.
//

import Foundation

struct TokenRefreshResponse: BaseApiResponse, Codable{
    let userId: Int64?
    let token: String?
    var code: String?
    var message: String?
    var errors: [String?]?

     init(userId: Int64?, token: String?, code: String? = nil, message: String? = nil, errors: [String?]? = nil) {
        self.userId = userId
        self.token = token
        self.code = code
        self.message = message
        self.errors = errors
    }
}
