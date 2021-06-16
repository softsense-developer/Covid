//
//  Encodable.swift
//  covid
//
//  Created by SmartSense on 9.06.2021.
//

import Foundation

struct JSON {
    static var encoder = JSONEncoder()
}

extension Encodable {
    subscript(key: String) -> Any? {
        return toJson[key]
    }
    var toJson: [String: Any] {
        return (try? JSONSerialization.jsonObject(with: JSON.encoder.encode(self))) as? [String: Any] ?? [:]
    }
}
