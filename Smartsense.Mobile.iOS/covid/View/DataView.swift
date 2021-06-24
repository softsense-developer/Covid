//
//  DataView.swift
//  covid
//
//  Created by SmartSense on 16.06.2021.
//

import SwiftUI

struct DataView: View {
    var body: some View {
        NavigationView{
            ScrollView(.vertical) {
                VStack{
                  Text("data view")                    
                }
            }.navigationTitle("tab_data")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar{
                ToolbarItem(placement: .navigationBarTrailing) {
                    NavigationLink(
                        destination: SettingsView(),
                        label: {
                            Label("Settings", systemImage: "gearshape.fill")
                                .foregroundColor(Color.colorPrimary)
                        })
                }
            }
        }
    }
}

struct DataView_Previews: PreviewProvider {
    static var previews: some View {
        DataView()
    }
}
