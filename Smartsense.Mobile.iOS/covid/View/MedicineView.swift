//
//  MedicineView.swift
//  covid
//
//  Created by SmartSense on 16.06.2021.
//

import SwiftUI

struct MedicineView: View {
    var body: some View {
        NavigationView{
            ScrollView(.vertical) {
                VStack{
                  Text("medicine view")
                    
                    
                }
            }.navigationTitle("tab_medicine")
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

struct MedicineView_Previews: PreviewProvider {
    static var previews: some View {
        MedicineView()
    }
}
