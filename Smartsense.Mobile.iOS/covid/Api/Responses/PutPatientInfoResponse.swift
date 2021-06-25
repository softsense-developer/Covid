//
//  UserLoginResponse.swift
//  covid
//
//  Created by SmartSense on 8.06.2021.
//

import Foundation

struct PutPatientInfoResponse: BaseApiResponse, Codable{
    var code: String?
    var message: String?
    var errors: [String?]?
    
    init(code: String? = nil, message: String? = nil, errors: [String?]? = nil) {
        self.code = code
        self.message = message
        self.errors = errors
    }
}
