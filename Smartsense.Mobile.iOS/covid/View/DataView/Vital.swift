//
//  File.swift
//  covid
//
//  Created by SmartSense on 6.08.2021.
//

import Foundation

class VitalHistory: NSObject, Identifiable{
    var type: Int
    var data: Float
    var saveType: Int
    var date: Int64
    
    init(type: Int, data: Float, saveType: Int, date: Int64) {
        self.type = type
        self.data = data
        self.saveType = saveType
        self.date = date
    }
    
    convenience init(random: Bool = false){
            if random{
                let type = ["New", "Used", "Mint"]
                var idx = Int.random(in: 0..<conditions.count)
                let randomConditions = conditions[idx]
                
                let names = ["Resident Evil", "Gears of War", "Halo", "God of War"]
                idx = Int.random(in: 0..<names.count)
                let randomName = names[idx]
                
                idx = Int.random(in: 0..<6)
                let randomTitle = "\(randomConditions) \(randomName) \(idx)"
                
                let serialNumber = UUID().uuidString.components(separatedBy: "-").first!
                let priceInDollars = Double(Int.random(in: 0...70))
                
                self.init(name: randomTitle, priceInDollars: priceInDollars, serialNumber: serialNumber)
            }else{
                self.init(name: "", priceInDollars: 0, serialNumber: "")
            }
        }
}
