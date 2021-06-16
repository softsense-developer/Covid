//
//  ApiConstantText.swift
//  covid
//
//  Created by SmartSense on 9.06.2021.
//

import Foundation

public class ApiConstantText {
    func getApiText(value: Int) -> String {
        switch value {
        case ApiConstant.DATA_INSERTED:
            return NSLocalizedString("data_inserted", comment: "")
        case ApiConstant.DATA_NOT_INSERTED:
            return NSLocalizedString("data_not_inserted", comment: "")
        case ApiConstant.TOKEN_REFRESHED:
            return NSLocalizedString("token_refreshed", comment: "")
        case ApiConstant.TOKEN_EXPIRED:
            return NSLocalizedString("token_expried", comment: "")
        case ApiConstant.REQUEST_OK:
            return NSLocalizedString("request_ok", comment: "")
        case ApiConstant.BAD_REQUEST:
            return NSLocalizedString("bad_request", comment: "")
        case ApiConstant.UNAUTHORIZED:
            return NSLocalizedString("unauthorized", comment: "")
        case ApiConstant.FORBIDDEN:
            return NSLocalizedString("forbidden", comment: "")
        case ApiConstant.INTERNAL_SERVER:
            return NSLocalizedString("internal_server", comment: "")
        default:
            return NSLocalizedString("error_occurred", comment: "400")
        }
    }
}


