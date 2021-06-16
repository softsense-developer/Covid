//
//  UserLoginResponse.swift
//  covid
//
//  Created by SmartSense on 8.06.2021.
//

import Foundation

struct PatientInfoResponse: BaseApiResponse, Codable{

    let userId: Int64?
    let name: String?
    let surname: String?
    let phone: String?
    let identityNumber: String?
    let doctorId: Int64?
    let gender: Int?
    let dateOfBirth: String?
    let userStatus: Int?
    let diagnosis: String?
    let hospitalName: String?
    let doctorName: String?
    let bloodGroup: String?
    var code: String?
    var message: String?
    var errors: [String?]?

    init(userId: Int64?, name: String?, surname: String?, phone: String?, identityNumber: String?, doctorId: Int64?
         , gender: Int?, dateOfBirth: String?, userStatus: Int?, diagnosis: String?, hospitalName: String?
         , doctorName: String?, bloodGroup: String?, code: String? = nil, message: String? = nil, errors: [String?]? = nil) {
        self.userId = userId
        self.name = name
        self.surname = surname
        self.phone = phone
        self.identityNumber = identityNumber
        self.doctorId = doctorId
        self.gender = gender
        self.dateOfBirth = dateOfBirth
        self.userStatus = userStatus
        self.diagnosis = diagnosis
        self.hospitalName = hospitalName
        self.doctorName = doctorName
        self.bloodGroup = bloodGroup
        self.code = code
        self.message = message
        self.errors = errors
    }
    
}
