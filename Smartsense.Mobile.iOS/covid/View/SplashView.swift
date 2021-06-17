//
//  SplashView.swift
//  covid
//
//  Created by SmartSense on 15.06.2021.
//

import SwiftUI
import Combine
import Alamofire
import NotificationBannerSwift

struct SplashView: View {
    
    private let apiService: ApiServiceProtocol = ApiService()
    private let apiText = ApiConstantText()
    
    @State private var hasTimeElapsed = false
    @ObservedObject var userAuth: UserAuth = UserAuth()
    @State var isSplash = true
    @State var isSplashVisible = false
    
    var body: some View {
        VStack{
            if !hasTimeElapsed{
                Image("logo")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .padding(75)
                    .opacity(isSplashVisible ? 0.0 : 1.0)
                    .animation(.easeIn(duration: 1.0))
                    .onAppear{
                        userAuth.loggedIn()
                        userAuth.emailValid()
                        userAuth.connectionHave()
                        delaySplashAndGetApiData()
                        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                            self.isSplashVisible = true
                        }
                    }
            }else{
                if userAuth.isLoggedin {
                    //User logged in
                    if userAuth.isEmailValid{
                        //User email verified
                        if userAuth.isConnectionHave{
                            //Internet connection have
                            MainView()
                        }else{
                            //Internet connection have not
                            MainView()
                        }
                    }else{
                        //User email not verified
                        LoginView()
                    }
                }else {
                    //User not logged in
                    LoginView()
                }
            }
            
            
            
            
        }
        
    }
    
    private func delaySplashAndGetApiData() {
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
            hasTimeElapsed = true
        }
        
        refreshToken()
    }
    
    
    private func refreshToken() {
        let request = TokenRefreshRequest(email: MyKeychain.userEmail,
                                          password: MyKeychain.userPassword,
                                          isRemember: true)
        
        apiService.refreshToken(request: request, onSuccess: {(response) in
            print("refreshToken \(response)")
            MyKeychain.userToken = response.token ?? ""
            getQuarantineStatus()
        },onFail: {(data) in
            print(data ?? "")
        })
    }
    
    
    private func getQuarantineStatus() {
        apiService.getPatientQuarantineStatus(onSuccess: {(response) in
            print("quarantineStatus \(response)")
            MyKeychain.quarantineStatus = response.quarantineStatus ?? false
        }, onFail: {(data) in
            print(data ?? "")
        })
    }
    
}

class UserAuth: ObservableObject {
    @Published var isLoggedin = false
    @Published var isEmailValid = false
    @Published var isConnectionHave = false
    
    func loggedIn() {
        if Int(MyKeychain.userID) != -1{
            //User logged in
            self.isLoggedin = true
        }
        print("isLoggedin: \(isLoggedin)")
    }
    
    func emailValid(){
        if MyKeychain.isEmailVerified{
            //User email verified
            self.isEmailValid = true
        }
        print("isEmailValid: \(isEmailValid)")
    }
    
    func connectionHave(){
        if Connectivity.isConnectedToInternet{
            //Internet connection have
            //refresh token
            self.isConnectionHave = true
           
        }
        print("connectionHave: \(isConnectionHave)")
    }
    
    func logout() {
        // login request... on success:
        self.isLoggedin = false
    }
}



struct SplashView_Previews: PreviewProvider {
    static var previews: some View {
        SplashView()
        SplashView()
            .preferredColorScheme(/*@START_MENU_TOKEN@*/.dark/*@END_MENU_TOKEN@*/)
    }
}
