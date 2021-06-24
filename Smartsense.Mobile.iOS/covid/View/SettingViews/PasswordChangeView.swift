//
//  ProfileView.swift
//  covid
//
//  Created by SmartSense on 24.06.2021.
//

import SwiftUI

struct PasswordChangeView: View {
    var body: some View {
        ScrollView(.vertical) {
            VStack{
                Text("password_change")
            }
        }.navigationTitle("password_change")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct PasswordChangeView_Previews: PreviewProvider {
    static var previews: some View {
        PasswordChangeView()
    }
}
