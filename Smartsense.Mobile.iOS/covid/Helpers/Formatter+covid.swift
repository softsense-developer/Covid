//
//  Formatter+covid.swift
//  covid
//
//  Created by SmartSense on 2.06.2021.
//

import Foundation


struct Formatters {
    static let floatFormatter: NumberFormatter = {
        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        formatter.minimumFractionDigits = 0
        formatter.maximumFractionDigits = 1
        return formatter
    }()
}


class DateFormat{
    public func dateMonthToMinute(date: Date) -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "MM/dd HH:mm"
        let newDateString = formatter.string(from: date)
        return newDateString
    }
}
