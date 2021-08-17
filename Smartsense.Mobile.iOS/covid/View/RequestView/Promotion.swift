//
//  Promotion.swift
//  covid
//
//  Created by SmartSense on 13.08.2021.
//

import Foundation

struct Promotion: Hashable, Codable, Identifiable{
    var id: Int64
    var userID: Int64
    var name: String
    var surname: String
    var requestStatus: Bool
    
    init(id: Int64, userID: Int64, name: String, surname: String, requestStatus: Bool) {
        self.id = id
        self.userID = userID
        self.name = name
        self.surname = surname
        self.requestStatus = requestStatus
    }
    
}
