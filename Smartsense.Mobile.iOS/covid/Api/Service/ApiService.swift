//
//  Service.swift
//  covid
//
//  Created by SmartSense on 8.06.2021.
//

import Foundation
import Alamofire
import NotificationBannerSwift

let headers: HTTPHeaders = [
    //eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ2YWx1ZSI6IjExIiwiZXhwIjoxNjMxMTg2OTk1LCJpc3MiOiJzbWFydHNlbnNlLmNvbS50ciIsImF1ZCI6InNtYXJ0c2Vuc2UuY29tLnRyIn0.03EEbailX8HvpYgCDUPG31djTwVtCXAvmmNLuMk_Mek
    .authorization(bearerToken: MyKeychain.userToken),
    .accept("application/json"),
    .acceptCharset("utf-8")
]

extension Path {
    func withBaseUrl() -> String {
        return "http://api.smartsense.com.tr\(self.rawValue)"
    }
}

enum Path: String {
    case LOGIN = "/api/auth/login"
    case REGISTER = "/api/auth/register"
    case PASSWORD_FORGET = "/api/auth/PasswordForgot"
    case PATIENT_GET_INFO = "/api/patient/getinfo"
    case PATIENT_GET_QUARANTINE_STATUS = "/api/patient/quarantinestatus"
    case TOKEN_REFRESH = "/api/auth/refreshtoken"
    case PASSWORD_CHANGE = "/api/auth/changepassword"
    case USER_INFO_CHANGE = "/api/auth/putinfo"
    case PATIENT_INFO_UPDATE = "/api/patient/putinfo"
}

public enum ErrorCode: Int  {
    case LOGIN = 100
    case REGISTER = 110
    case PASSWORD_FORGET = 120
    case PATIENT_GET_INFO = 130
    case PATIENT_GET_QUARANTINE_STATUS = 140
    case TOKEN_REFRESH = 150
    case PASSWORD_CHANGE = 160
    case USER_INFO_CHANGE = 170
    case PATIENT_INFO_UPDATE = 180
}

struct ApiService: ApiServiceProtocol {
   
    private let apiText = ApiConstantText()
    var banner = NotificationBanner(title: NSLocalizedString("error_occurred", comment: "")+" "+String(ErrorCode.LOGIN.rawValue+1), style: .danger)
    
    func login(request: UserLoginRequest, onSuccess: @escaping (UserLoginResponse) -> Void, onFail: @escaping (String?) -> Void) {
        if Connectivity.isConnectedToInternet{
            AF.request(Path.LOGIN.withBaseUrl(), method: .post, parameters: request.toJson, encoding: JSONEncoding.default, headers: headers).responseJSON{ response in
                guard response.data != nil else {
                    onFail(response.debugDescription)
                    return
                }
                responseHandler(errorCode: ErrorCode.LOGIN.rawValue+2, response: response, onSuccess: {(responseHandled) in
                    do{
                        let responseData = try JSONDecoder().decode(UserLoginResponse.self, from: response.data!)
                        onSuccess(responseData)
                    }catch{
                        print(error.localizedDescription)
                        NotificationBanner(title: NSLocalizedString("error_occurred", comment: "")+" "+String(ErrorCode.LOGIN.rawValue+1), style: .danger).show(bannerPosition: .top)
                    }
                },onFail: {(data) in
                    onFail(data ?? "")
                })
            }
        }else{
            onFail("")
            StatusBarNotificationBanner(title: NSLocalizedString("no_connection", comment: ""), style: .danger).show()
        }
    }
    
    func register(request: UserRegisterRequest, onSuccess: @escaping (UserRegisterResponse) -> Void, onFail: @escaping (String?) -> Void) {
        if Connectivity.isConnectedToInternet{
            AF.request(Path.REGISTER.withBaseUrl(), method: .post, parameters: request.toJson, encoding: JSONEncoding.default, headers: headers).responseJSON{ response in
                responseHandler(errorCode: ErrorCode.REGISTER.rawValue+2, response: response, onSuccess: {(responseHandled) in
                    do{
                        let responseData = try JSONDecoder().decode(UserRegisterResponse.self, from: response.data!)
                        onSuccess(responseData)
                    }catch{
                        print(error.localizedDescription)
                        NotificationBanner(title: NSLocalizedString("error_occurred", comment: "")+" "+String(ErrorCode.REGISTER.rawValue+1), style: .danger).show(bannerPosition: .top)
                    }
                },onFail: {(data) in
                    onFail(data ?? "")
                })
            }
        }else{
            onFail("")
            StatusBarNotificationBanner(title: NSLocalizedString("no_connection", comment: ""), style: .danger).show()
        }
        
    }
    
    func passwordForget(request: UserPassForgetRequest, onSuccess: @escaping (UserPassForgetResponse) -> Void, onFail: @escaping (String?) -> Void) {
        if Connectivity.isConnectedToInternet{
            AF.request(Path.PASSWORD_FORGET.withBaseUrl(), method: .post, parameters: request.toJson, encoding: JSONEncoding.default, headers: headers).responseJSON{ response in
                responseHandler(errorCode: ErrorCode.PASSWORD_FORGET.rawValue+2, response: response, onSuccess: {(responseHandled) in
                    do{
                        let responseData = try JSONDecoder().decode(UserPassForgetResponse.self, from: response.data!)
                        onSuccess(responseData)
                    }catch{
                        print(error.localizedDescription)
                        NotificationBanner(title: NSLocalizedString("error_occurred", comment: "")+" "+String(ErrorCode.PASSWORD_FORGET.rawValue+1), style: .danger).show(bannerPosition: .top)
                    }
                },onFail: { (data) in
                    onFail(data ?? "")
                })
            }
        }else{
            onFail("")
            StatusBarNotificationBanner(title: NSLocalizedString("no_connection", comment: ""), style: .danger).show()
        }
    }
    
    func getPatientInfo(onSuccess: @escaping (PatientInfoResponse) -> Void, onFail: @escaping (String?) -> Void) {
        if Connectivity.isConnectedToInternet{
            AF.request(Path.PATIENT_GET_INFO.withBaseUrl(), method: .get, headers: headers).responseJSON{ response in
               
                responseHandler(errorCode: ErrorCode.PATIENT_GET_INFO.rawValue+2, response: response, onSuccess: {(responseHandled) in
                    do{
                        let responseData = try JSONDecoder().decode(PatientInfoResponse.self, from: response.data!)
                        onSuccess(responseData)
                    }catch{
                        print(error.localizedDescription)
                        NotificationBanner(title: NSLocalizedString("error_occurred", comment: "")+" "+String(ErrorCode.PATIENT_GET_INFO.rawValue+1), style: .danger).show(bannerPosition: .top)
                    }
                },onFail: { (data) in
                    onFail(data ?? "")
                })
            }
        }else{
            onFail("")
            StatusBarNotificationBanner(title: NSLocalizedString("no_connection", comment: ""), style: .danger).show()
        }
    }
    
    func getPatientQuarantineStatus(onSuccess: @escaping (QuarantineStatusResponse) -> Void, onFail: @escaping (String?) -> Void) {
        if Connectivity.isConnectedToInternet{
            AF.request(Path.PATIENT_GET_QUARANTINE_STATUS.withBaseUrl(), method: .get, headers: headers).responseJSON{ response in
               
                responseHandler(errorCode: ErrorCode.PATIENT_GET_QUARANTINE_STATUS.rawValue+2, response: response, onSuccess: {(responseHandled) in
                    do{
                        let responseData = try JSONDecoder().decode(QuarantineStatusResponse.self, from: response.data!)
                        onSuccess(responseData)
                    }catch{
                        print(error.localizedDescription)
                        NotificationBanner(title: NSLocalizedString("error_occurred", comment: "")+" "+String(ErrorCode.PATIENT_GET_QUARANTINE_STATUS.rawValue+1), style: .danger).show(bannerPosition: .top)
                    }
                },onFail: { (data) in
                    onFail(data ?? "")
                })
            }
        }else{
            onFail("")
            StatusBarNotificationBanner(title: NSLocalizedString("no_connection", comment: ""), style: .danger).show()
        }
    }
   
    func refreshToken(request: TokenRefreshRequest, onSuccess: @escaping (TokenRefreshResponse) -> Void, onFail: @escaping (String?) -> Void) {
        if Connectivity.isConnectedToInternet{
            AF.request(Path.TOKEN_REFRESH.withBaseUrl(), method: .post, parameters: request.toJson, encoding: JSONEncoding.default, headers: headers).responseJSON{ response in
                guard response.data != nil else {
                    onFail(response.debugDescription)
                    return
                }
                responseHandler(errorCode: ErrorCode.TOKEN_REFRESH.rawValue+2, response: response, onSuccess: {(responseHandled) in
                    do{
                        let responseData = try JSONDecoder().decode(TokenRefreshResponse.self, from: response.data!)
                        onSuccess(responseData)
                    }catch{
                        print(error.localizedDescription)
                        NotificationBanner(title: NSLocalizedString("error_occurred", comment: "")+" "+String(ErrorCode.TOKEN_REFRESH.rawValue+1), style: .danger).show(bannerPosition: .top)
                    }
                },onFail: {(data) in
                    onFail(data ?? "")
                })
            }
        }else{
            onFail("")
            StatusBarNotificationBanner(title: NSLocalizedString("no_connection", comment: ""), style: .danger).show()
        }
    }
    
    
    func passwordChange(request: PasswordChangeRequest, onSuccess: @escaping (PasswordChangeResponse) -> Void, onFail: @escaping (String?) -> Void) {
        if Connectivity.isConnectedToInternet{
            AF.request(Path.PASSWORD_CHANGE.withBaseUrl(), method: .post, parameters: request.toJson, encoding: JSONEncoding.default, headers: headers).responseJSON{ response in
                guard response.data != nil else {
                    onFail(response.debugDescription)
                    return
                }
                responseHandler(errorCode: ErrorCode.PASSWORD_CHANGE.rawValue + 2, response: response, onSuccess: {(responseHandled) in
                    do{
                        let responseData = try JSONDecoder().decode(PasswordChangeResponse.self, from: response.data!)
                        onSuccess(responseData)
                    }catch{
                        print(error.localizedDescription)
                        NotificationBanner(title: NSLocalizedString("error_occurred", comment: "") + " " + String(ErrorCode.PASSWORD_CHANGE.rawValue + 1), style: .danger).show(bannerPosition: .top)
                    }
                },onFail: {(data) in
                    onFail(data ?? "")
                })
            }
        }else{
            onFail("")
            StatusBarNotificationBanner(title: NSLocalizedString("no_connection", comment: ""), style: .danger).show()
        }
    }
    
    func userInfoChange(request: UserInfoChangeRequest, onSuccess: @escaping (UserInfoChangeResponse) -> Void, onFail: @escaping (String?) -> Void) {
        if Connectivity.isConnectedToInternet{
            AF.request(Path.USER_INFO_CHANGE.withBaseUrl(), method: .post, parameters: request.toJson, encoding: JSONEncoding.default, headers: headers).responseJSON{ response in
                guard response.data != nil else {
                    onFail(response.debugDescription)
                    return
                }
                responseHandler(errorCode: ErrorCode.USER_INFO_CHANGE.rawValue + 2, response: response, onSuccess: {(responseHandled) in
                    do{
                        let responseData = try JSONDecoder().decode(UserInfoChangeResponse.self, from: response.data!)
                        onSuccess(responseData)
                    }catch{
                        print(error.localizedDescription)
                        NotificationBanner(title: NSLocalizedString("error_occurred", comment: "") + " " + String(ErrorCode.USER_INFO_CHANGE.rawValue + 1), style: .danger).show(bannerPosition: .top)
                    }
                },onFail: {(data) in
                    onFail(data ?? "")
                })
            }
        }else{
            onFail("")
            StatusBarNotificationBanner(title: NSLocalizedString("no_connection", comment: ""), style: .danger).show()
        }
    }
    
    
    func updatePatientInfo(request: PutPatientInfoRequest, onSuccess: @escaping (PutPatientInfoResponse) -> Void, onFail: @escaping (String?) -> Void) {
        if Connectivity.isConnectedToInternet{
            AF.request(Path.PATIENT_INFO_UPDATE.withBaseUrl(), method: .put, parameters: request.toJson, encoding: JSONEncoding.default, headers: headers).responseJSON{ response in
                guard response.data != nil else {
                    onFail(response.debugDescription)
                    return
                }
                responseHandler(errorCode: ErrorCode.PATIENT_INFO_UPDATE.rawValue + 2, response: response, onSuccess: {(responseHandled) in
                    do{
                        let responseData = try JSONDecoder().decode(PutPatientInfoResponse.self, from: response.data!)
                        onSuccess(responseData)
                    }catch{
                        print(error.localizedDescription)
                        NotificationBanner(title: NSLocalizedString("error_occurred", comment: "") + " " + String(ErrorCode.PATIENT_INFO_UPDATE.rawValue + 1), style: .danger).show(bannerPosition: .top)
                    }
                },onFail: {(data) in
                    onFail(data ?? "")
                })
            }
        }else{
            onFail("")
            StatusBarNotificationBanner(title: NSLocalizedString("no_connection", comment: ""), style: .danger).show()
        }
    }
    
    
    
    
    
    func responseHandler(errorCode: Int, response: AFDataResponse<Any>, onSuccess: @escaping (AFDataResponse<Any>) -> Void, onFail: @escaping (String?) -> Void) {
        if let httpStatusCode = response.response?.statusCode {
            if httpStatusCode == 200{
                do{
                    guard let data = response.data else {
                        onFail(response.debugDescription)
                        GrowingNotificationBanner(title: NSLocalizedString("error_occurred", comment: "")+" "+String(errorCode), style: .warning).show(bannerPosition: .top)
                        return
                    }
                    
                    let responseData = try JSONDecoder().decode(UserLoginResponse.self, from: data)
                    if  responseData.code == "200" {
                        onSuccess(response)
                    }else if responseData.code == "400"{
                        print("400")
                        print(responseData.errors ?? "")
                        if let errorList = responseData.errors {
                            if errorList.count != 1{
                                var errors: String = ""
                                for (index, error) in errorList.enumerated() {
                                    guard index<errorList.count-1 else {
                                        break
                                    }
                                    errors.append(error ?? "")
                                    errors.append("\n")
                                }
                                GrowingNotificationBanner(title: NSLocalizedString("validation_error", comment: ""), subtitle: errors, style: .danger).show(bannerPosition: .top)
                            }else{
                                GrowingNotificationBanner(title: errorList[0] ?? "", style: .danger).show(bannerPosition: .top)
                            }
                        }
                        onFail("")
                    }else{
                        GrowingNotificationBanner(title: NSLocalizedString("error_occurred", comment: "")+" "+String(errorCode+1), style: .danger).show(bannerPosition: .top)
                    }
                }catch{
                    print(error.localizedDescription)
                    GrowingNotificationBanner(title: NSLocalizedString("error_occurred", comment: "")+" "+String(errorCode+2), style: .danger).show(bannerPosition: .top)
                }
                
            }else if httpStatusCode == ApiConstant.BAD_REQUEST{
                GrowingNotificationBanner(title: apiText.getApiText(value: ApiConstant.BAD_REQUEST), style: .warning).show(bannerPosition: .top)
            }else if httpStatusCode == ApiConstant.UNAUTHORIZED{
                GrowingNotificationBanner(title: apiText.getApiText(value: ApiConstant.UNAUTHORIZED), style: .warning).show(bannerPosition: .top)
            }else if httpStatusCode == ApiConstant.FORBIDDEN{
                GrowingNotificationBanner(title: apiText.getApiText(value: ApiConstant.FORBIDDEN), style: .warning).show(bannerPosition: .top)
            }else if httpStatusCode == ApiConstant.INTERNAL_SERVER{
                GrowingNotificationBanner(title: apiText.getApiText(value: ApiConstant.INTERNAL_SERVER), style: .warning).show(bannerPosition: .top)
            }else{
                GrowingNotificationBanner(title: NSLocalizedString("error_occurred", comment: "")+" "+String(errorCode+3), style: .danger).show(bannerPosition: .top)
            }
            onFail("")
        } else {
            GrowingNotificationBanner(title: NSLocalizedString("error_occurred", comment: "")+" "+String(errorCode+4), style: .danger).show(bannerPosition: .top)
        }
    }
    
}

struct Connectivity {
    
    static let sharedInstance = NetworkReachabilityManager()!
    
    static var isConnectedToInternet:Bool {
        return self.sharedInstance.isReachable
    }
    
}
