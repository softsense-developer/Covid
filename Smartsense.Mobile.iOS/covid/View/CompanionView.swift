//
//  ProfileView.swift
//  covid
//
//  Created by SmartSense on 24.06.2021.
//

import SwiftUI

struct CompanionView: View {
    var body: some View {
        ScrollView(.vertical) {
            VStack{
                Text("companion")
            }
        }.navigationTitle("companion")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct CompanionView_Previews: PreviewProvider {
    static var previews: some View {
        CompanionView()
    }
}
