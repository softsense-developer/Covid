//
//  ProfileView.swift
//  covid
//
//  Created by SmartSense on 24.06.2021.
//

import SwiftUI

struct DoctorSelectView: View {
    var body: some View {
        ScrollView(.vertical) {
            VStack{
                Text("doctor")
            }
        }.navigationTitle("doctor")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct DoctorSelectView_Previews: PreviewProvider {
    static var previews: some View {
        DoctorSelectView()
    }
}
