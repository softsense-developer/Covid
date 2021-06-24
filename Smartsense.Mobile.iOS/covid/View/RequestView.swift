//
//  ProfileView.swift
//  covid
//
//  Created by SmartSense on 24.06.2021.
//

import SwiftUI

struct RequestView: View {
    var body: some View {
        ScrollView(.vertical) {
            VStack{
                Text("request")
            }
        }.navigationTitle("request")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct RequestView_Previews: PreviewProvider {
    static var previews: some View {
        RequestView()
    }
}
