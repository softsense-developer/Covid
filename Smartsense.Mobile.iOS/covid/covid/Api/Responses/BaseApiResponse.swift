//
//  BaseApiResponse.swift
//  covid
//
//  Created by SmartSense on 8.06.2021.
//

import Foundation

protocol BaseApiResponse {
    var code: String?  { get set }
    var message: String?  { get set }
    var errors: [String?]?  { get set }
}
