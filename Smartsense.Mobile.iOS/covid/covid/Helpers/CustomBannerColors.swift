//
//  CustomBannerColors.swift
//  covid
//
//  Created by SmartSense on 10.06.2021.
//

import Foundation
import NotificationBannerSwift
import SwiftUI

public protocol BannerColorsProtocol {
    func color(for style: BannerStyle) -> UIColor
}

class CustomBannerColors: BannerColorsProtocol {
    
    internal func color(for style: BannerStyle) -> UIColor {
        switch style {
        case .danger:    // Your custom .danger color
            return UIColor(red: 0.8, green: 0.1, blue: 0.5, alpha: 1)
        case .info: break        // Your custom .info color
        case .customView:   break   // Your custom .customView color
        case .success:  break    // Your custom .success color
        case .warning:   break   // Your custom .warning color
        }
        return UIColor(red: 0.8, green: 0.1, blue: 0.5, alpha: 1)
    }
    
}
