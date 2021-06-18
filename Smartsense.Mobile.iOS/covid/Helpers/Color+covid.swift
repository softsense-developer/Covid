//
//  Color+covid.swift
//  covid
//
//  Created by SmartSense on 1.06.2021.
//

import SwiftUI

extension Color{
    
    private static var colorWhite: Color{
        return Color("BackgroundColor")
    }
    
    static var colorPrimary: Color{
        return Color("ColorPrimary")
    }
    
    static var textFieldBackground: Color{
        return Color("TextFieldBackground")
    }
    
    static var componentColor: Color{
        return Color("ComponentColor")
    }
    
    static var passiveColor: Color{
        return Color("PassiveColor")
    }
    
    static var heartColor: Color{
        return Color("HeartColor")
    }
    
    static var temperatureColor: Color{
        return Color("TemperatureColor")
    }
    
    static var SpO2Color: Color{
        return Color("SpO2Color")
    }
    
    static var cardActiveColor: Color{
        return Color("CardActiveColor")
    }
    
    static var backgroundColor: Color{
        return colorWhite;
    }
    
    static var buttonBackground: Color{
        return colorPrimary;
    }
}
