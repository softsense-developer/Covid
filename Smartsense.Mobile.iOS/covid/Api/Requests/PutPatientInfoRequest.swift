//
//  UserLoginRequest.swift
//  covid
//
//  Created by SmartSense on 9.06.2021.
//

import Foundation

struct PutPatientInfoRequest: BaseApiRequest, Encodable {

    private(set) var dateOfBirth: String?
    private(set) var identityNumber: String?
    private(set) var gender: Int?
    private(set) var userStatus: Int?
    private(set) var diagnosis: String?
    private(set) var bloodGroup: String?
    var userId: Int64?
    
    init(dateOfBirth: String? = nil, identityNumber: String? = nil, gender: Int? = nil, userStatus: Int? = nil, diagnosis: String? = nil, bloodGroup: String? = nil) {
        self.dateOfBirth = dateOfBirth
        self.identityNumber = identityNumber
        self.gender = gender
        self.userStatus = userStatus
        self.diagnosis = diagnosis
        self.bloodGroup = bloodGroup
    }
    
}

