//
//  EmailVerifyView.swift
//  covid
//
//  Created by SmartSense on 16.06.2021.
//

import SwiftUI
import NotificationBannerSwift

struct EmailVerifyView: View {
    @StateObject var observedData = ObservedData()
    
    var body: some View {
       
        if !observedData.isMailVerified && !observedData.isBackToLogin{
            MailVerifyView().environmentObject(observedData)
        }else{
            if observedData.isMailVerified{
                MainView()
            }else{
                LoginView()
            }
        }
    }
}

struct MailVerifyView: View {
    @EnvironmentObject var observedData: ObservedData
    
    private let apiService: ApiServiceProtocol = ApiService()
    private let apiText = ApiConstantText()
    
    @State var isProgressViewShowing = false
    @State var progressViewText = ""
    
    var body: some View {
        GeometryReader{ geo in
            VStack {
                
                Spacer()
                
                /* App logo */
                Image("logo")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: geo.size.width/2)
                
                Spacer()
                
                /* Verification animation */
                LottieView(fileName: "emailverify")
                    .frame(width: geo.size.width/1.5, height: geo.size.width/1.5)
                
                VStack {
                    
                    /* Verification info text */
                    Text("verification_info")
                        .font(.body)
                        .foregroundColor(.primary)
                        .padding()
                    
                    
                    /* Return to login button */
                    Button(action: {
                        observedData.isBackToLogin = true
                    }, label: {
                        Text("back_to_login")
                            .frame(maxWidth: .infinity)
                            .frame(height: 40)
                    })
                    .padding([.top, .bottom], 4)
                    .accentColor(.colorPrimary)
                    
                    /* Email verifyed button */
                    Button(action: {
                        login()
                    }, label: {
                        Text("email_verified")
                            .frame(maxWidth: .infinity)
                            .frame(height: 40)
                    })
                    .padding([.top, .bottom], 4)
                    .accentColor(.colorPrimary)
                    
                    
                }
                .padding(4)
                .background(Color.componentColor)
                .cornerRadius(15)
                .shadow(radius: 0.5)
                .padding()
            }
            
        }.progressDialog(isShowing: $isProgressViewShowing, message: progressViewText)
    }
    
    
    
    /*
     * Api login method
     */
    private func login() {
        progressViewText = NSLocalizedString("login_dot", comment: "")
        isProgressViewShowing = true
        
        let request = UserLoginRequest(email: MyKeychain.userEmail,
                                       password: MyKeychain.userPassword,
                                       isRemember: true)
        
        apiService.login(request: request, onSuccess: {(response) in
            MyKeychain.userID = String(response.userId ?? -1)
            MyKeychain.userPhone = response.phone ?? ""
            MyKeychain.userToken = response.token ?? ""
            MyKeychain.userRoleID = String(response.roleId ?? -1)
            MyKeychain.isEmailVerified = true
            
            GrowingNotificationBanner(title: NSLocalizedString("login_dot", comment: ""), style: .success).show(bannerPosition: .top)
            observedData.isMailVerified = true
        },onFail: {(data) in
            isProgressViewShowing = false
            print(data ?? "")
        })
    }
}


class ObservedData: ObservableObject {
    @Published var isMailVerified = false
    @Published var isBackToLogin = false
}



struct EmailVerifyView_Previews: PreviewProvider {
    static var previews: some View {
        EmailVerifyView()
        EmailVerifyView()
            .preferredColorScheme(.dark)
    }
}
