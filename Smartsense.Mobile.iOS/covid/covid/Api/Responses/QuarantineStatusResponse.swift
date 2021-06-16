//
//  UserLoginResponse.swift
//  covid
//
//  Created by SmartSense on 8.06.2021.
//

import Foundation

struct QuarantineStatusResponse: BaseApiResponse, Codable{
    let userId: Int64?
    let quarantineStatus: Bool?
    var code: String?
    var message: String?
    var errors: [String?]?
    
    init(userId: Int64?, quarantineStatus: Bool?, code: String? = nil, message: String? = nil, errors: [String?]? = nil) {
        self.userId = userId
        self.quarantineStatus = quarantineStatus
        self.code = code
        self.message = message
        self.errors = errors
    }
    
}
