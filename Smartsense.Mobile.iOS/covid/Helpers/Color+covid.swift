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
    
    static var navigationBarColor: Color{
        return Color("NavigationBarColor")
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
    
    static var greyColor: Color{
        return Color("GreyColor")
    }
    
    static var blue500: Color{
        return Color("Blue500")
    }
    
    static var orange500: Color{
        return Color("Orange500")
    }
    
    static var green500: Color{
        return Color("Green500")
    }
    
    static var red500: Color{
        return Color("Red500")
    }
    
    static var purple500: Color{
        return Color("Purple500")
    }

    
    static var backgroundColor: Color{
        return colorWhite;
    }
    
    static var buttonBackground: Color{
        return colorPrimary;
    }
}
