//
//  File.swift
//  covid
//
//  Created by SmartSense on 6.08.2021.
//

import Foundation

struct Vital: Hashable, Codable, Identifiable{
    var id: String
    var type: Int
    var data: Int
    var saveType: Int
    var date: Date
    
    init(id: String, type: Int, data: Int, saveType: Int, date: Date) {
        self.id = id
        self.type = type
        self.data = data
        self.saveType = saveType
        self.date = date
    }
    
    init(random: Bool = false){
            if random{
                let type = [Constant.Temperature, Constant.HeartRate, Constant.SpO2]
                let randomType = type[Int.random(in: 0..<type.count)]
                
                let randomData = Int.random(in: 40..<100)
                
                let serialNumber = UUID().uuidString.components(separatedBy: "-").first!
                
                self.init(id: serialNumber, type: randomType, data: randomData, saveType: Constant.SaveAuto, date: Date())
            }else{
                self.init(id: "", type: 0, data: 0, saveType: 0, date: Date())
            }
        }
}
