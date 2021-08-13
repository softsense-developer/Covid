//
//  Companion.swift
//  covid
//
//  Created by SmartSense on 12.08.2021.
//

import Foundation

struct Companion: Hashable, Codable, Identifiable{
    var id: Int64
    var name: String
    var surname: String
    var email: String
    var password: String
    
    init(id: Int64, name: String, surname: String, email: String, password: String) {
        self.id = id
        self.name = name
        self.surname = surname
        self.email = email
        self.password = password
    }
}
