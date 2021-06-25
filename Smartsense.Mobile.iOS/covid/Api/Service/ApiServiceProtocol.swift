//
//  ApiServiceProtocol.swift
//  covid
//
//  Created by SmartSense on 9.06.2021.
//

import Foundation
import Alamofire

protocol ApiServiceProtocol {
    func login(request: UserLoginRequest, onSuccess: @escaping (UserLoginResponse) -> Void, onFail: @escaping (String?) -> Void)
    func register(request: UserRegisterRequest, onSuccess: @escaping (UserRegisterResponse) -> Void, onFail: @escaping (String?) -> Void)
    func passwordForget(request: UserPassForgetRequest, onSuccess: @escaping (UserPassForgetResponse) -> Void, onFail: @escaping (String?) -> Void)
    func getPatientInfo(onSuccess: @escaping (PatientInfoResponse) -> Void, onFail: @escaping (String?) -> Void)
    func getPatientQuarantineStatus(onSuccess: @escaping (QuarantineStatusResponse) -> Void, onFail: @escaping (String?) -> Void)
    func refreshToken(request: TokenRefreshRequest, onSuccess: @escaping (TokenRefreshResponse) -> Void, onFail: @escaping (String?) -> Void)
    func passwordChange(request: PasswordChangeRequest, onSuccess: @escaping (PasswordChangeResponse) -> Void, onFail: @escaping (String?) -> Void)
    func userInfoChange(request: UserInfoChangeRequest, onSuccess: @escaping (UserInfoChangeResponse) -> Void, onFail: @escaping (String?) -> Void)
    func updatePatientInfo(request: PutPatientInfoRequest, onSuccess: @escaping (PutPatientInfoResponse) -> Void, onFail: @escaping (String?) -> Void)
}
