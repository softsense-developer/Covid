//
//  Medicine.swift
//  covid
//
//  Created by SmartSense on 10.08.2021.
//

import Foundation

struct Medicine: Hashable, Codable, Identifiable{
    var id: String
    var medicineName: String
    var onComingUsageTime: Date
    
    init(id: String, medicineName: String, onComingUsageTime: Date) {
        self.id = id
        self.medicineName = medicineName
        self.onComingUsageTime = onComingUsageTime
    }
}
