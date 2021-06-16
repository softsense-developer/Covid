//
//  PostModel.swift
//  covid
//
//  Created by SmartSense on 9.06.2021.
//

import Foundation

struct PostModel: Codable {
    let userID: Int?
    let id: Int?
    let title: String?
    let completed: Bool?
}
