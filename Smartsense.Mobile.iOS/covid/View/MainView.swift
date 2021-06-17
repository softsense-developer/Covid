//
//  MainView.swift
//  covid
//
//  Created by SmartSense on 15.06.2021.
//

import SwiftUI

struct MainView: View {
    @State private var selectedTab: Tab = .home
    
    enum Tab {
        case home
        case connection
        case data
        case medicine
        case other
    }
    var body: some View {
        TabView(selection: $selectedTab){
            HomeView()
                .tabItem {
                    Label("tab_home", systemImage: "house")
                }
                .tag(Tab.home)
            ConnectionView()
                .tabItem {
                    Label("tab_connection", systemImage: "bolt.horizontal")
                }
                .tag(Tab.connection)
            DataView()
                .tabItem{
                    Label("tab_data", systemImage: "list.dash")
                }
                .tag(Tab.data)
            MedicineView()
                .tabItem{
                    Label("tab_medicine", systemImage: "doc.on.doc")
                }
                .tag(Tab.medicine)
            Button("tab_other", action:{
                /*MyKeychain.clear()
                SplashView()*/
            })
                .tabItem {
                    Label("tab_other", systemImage: "ellipsis")
                }
                .tag(Tab.other)
        }
        .accentColor(.colorPrimary)
        
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView()
        MainView()
            .preferredColorScheme(.dark)
    }
}
