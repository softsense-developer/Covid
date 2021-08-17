//
//  MainView.swift
//  covid
//
//  Created by SmartSense on 15.06.2021.
//

import SwiftUI

struct MainView: View {
    @State private var selectedTab: Tab = .home
    @State private var otherTabClicked: Bool = false
    
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
                    Image("bluetooth")
                        .renderingMode(.template)
                    Text("tab_connection")
                }
                .tag(Tab.connection)
            DataView()
                .tabItem{
                    Label("tab_data", systemImage: "doc.text")
                }
                .tag(Tab.data)
            MedicineView()
                .tabItem{
                    Label("tab_medicine", systemImage: "pills")
                }
                .tag(Tab.medicine)
            OtherView()
                .tabItem{
                    Label("tab_other", systemImage: "ellipsis.circle")
                }
            .tag(Tab.other)
            
            
        }.accentColor(.colorPrimary)
        
        
    }
}




struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView()
         //.preferredColorScheme(.dark)
    }
}



