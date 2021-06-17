//
//  covidApp.swift
//  covid
//
//  Created by SmartSense on 1.06.2021.
//

import SwiftUI

@main
struct covidApp: App {
    @Environment(\.scenePhase) var scenePhase
    
    var body: some Scene {
        WindowGroup {
            MainView()
            //SplashView()
            //EmailVerifyView()
        }/*.onChange(of: scenePhase){ phase in
            switch phase{
            case .active:
                print("Active")
            case .inactive:
                print("Inactive")
            case .background:
                print("Background")
            default:
                print("defualt")
            }
        }*/
    }
}
