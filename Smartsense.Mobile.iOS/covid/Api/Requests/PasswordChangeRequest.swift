//
//  UserLoginRequest.swift
//  covid
//
//  Created by SmartSense on 9.06.2021.
//

import Foundation

struct PasswordChangeRequest: BaseApiRequest, Encodable {
    private(set) var oldPassword: String?
    private(set) var newPassword: String?
    private(set) var confirmPassword: String?
    var userId: Int64?
    
    internal init(oldPassword: String? = nil, newPassword: String? = nil, confirmPassword: String? = nil) {
        self.oldPassword = oldPassword
        self.newPassword = newPassword
        self.confirmPassword = confirmPassword
    }
    
}

