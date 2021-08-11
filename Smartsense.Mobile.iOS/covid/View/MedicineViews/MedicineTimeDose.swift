//
//  MedicineTimeDose.swift
//  covid
//
//  Created by SmartSense on 11.08.2021.
//

import Foundation

struct MedicimeTimeDose: Hashable, Codable, Identifiable{
    var id: String
    var dose: String
    var time: Date
    
    init(id: String, dose: String, time: Date) {
        self.id = id
        self.dose = dose
        self.time = time
    }
}
