//
//  ProfileView.swift
//  covid
//
//  Created by SmartSense on 24.06.2021.
//

import SwiftUI

struct MeasurementView: View {
    var body: some View {
        ScrollView(.vertical) {
            VStack{
                Text("measurement_settings")
            }
        }.navigationTitle("measurement_settings")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct MeasurementView_Previews: PreviewProvider {
    static var previews: some View {
        MeasurementView()
    }
}
