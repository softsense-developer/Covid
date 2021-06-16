//
//  EmailVerifyView.swift
//  covid
//
//  Created by SmartSense on 16.06.2021.
//

import SwiftUI

struct EmailVerifyView: View {
    
    @State var isProgressViewShowing = false
    @State var progressViewText = ""
    
    private let apiService: ApiServiceProtocol = ApiService()
    private let apiText = ApiConstantText()
    
    var body: some View {
        VStack {
            
            GeometryReader{ geo in
                VStack {
                    
                    Image("logo")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: geo.size.width/2)
                    
                    LottieView(fileName: "emailverify")
                        .frame(width: geo.size.width)
                }
                
            }.padding(50)
        }
    }
    
    /*
     * Api login method
     */
    private func login(request: UserLoginRequest) {
        progressViewText = NSLocalizedString("login_dot", comment: "")
        isProgressViewShowing = true
        
        apiService.login(request: request, onSuccess: {(response) in
            MyKeychain.userID = String(response.userId ?? -1)
            MyKeychain.userPhone = response.phone ?? ""
            MyKeychain.userToken = response.token ?? ""
            MyKeychain.userRoleID = String(response.roleId ?? -1)
            MyKeychain.isEmailVerified = true
            
        },onFail: {(data) in
            isProgressViewShowing = false
            print(data ?? "")
        })
    }
}

struct EmailVerifyView_Previews: PreviewProvider {
    static var previews: some View {
        EmailVerifyView()
        EmailVerifyView()
            .preferredColorScheme(.dark)
    }
}
