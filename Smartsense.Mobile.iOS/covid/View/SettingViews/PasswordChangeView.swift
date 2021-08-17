//
//  ProfileView.swift
//  covid
//
//  Created by SmartSense on 24.06.2021.
//

import SwiftUI
import Combine
import Alamofire
import NotificationBannerSwift

struct PasswordChangeView: View {
    
    @State var oldPassword: String = ""
    @State var newPassword: String = ""
    @State var newPasswordAgain: String = ""
    
    @State var isOldPasswordValid: Bool = false
    @State var isNewPasswordValid: Bool = false
    @State var isNewPasswordAgainValid: Bool = false
    
    @State private var showOldPassword = false
    @State private var showNewPassword = false
    @State private var showNewPasswordAgain = false
    
    @State var firstClickToChange: Bool = false
    @State var isLoading: Bool = false
    
    private let apiService: ApiServiceProtocol = ApiService()
    private let apiText = ApiConstantText()
    private var validation = Validation()
    
    var body: some View {
        ScrollView(.vertical) {
            VStack{
                VStack(alignment: .center, spacing: 16){
                    
                    //User old pass view start
                    VStack(alignment: .leading){
                        Text("current_password")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                            .padding(.leading, 85)
                        
                        HStack {
                            ZStack{
                                Circle()
                                    .frame(width: 55, height: 55)
                                    .foregroundColor(.textFieldBackground)
                                
                                Image(systemName: "lock.rotation")
                                    .font(.system(size: 20))
                                    .foregroundColor(.secondary)
                            }.padding(.trailing, 8)
                            
                            VStack(alignment: .leading, spacing: 4.0){
                                
                                HStack {
                                    if showOldPassword {
                                        TextField("current_password", text: $oldPassword)
                                            .foregroundColor(.primary)
                                            .onReceive(Just(oldPassword), perform: { newValue in
                                                isOldPasswordValid = validation.validatePassword(password: oldPassword)
                                            })
                                    } else {
                                        SecureField("current_password", text: $oldPassword)
                                            .foregroundColor(.primary)
                                            .onReceive(Just(oldPassword), perform: { newValue in
                                                isOldPasswordValid = validation.validatePassword(password: oldPassword)
                                            })
                                    }
                                    Button(action: { self.showOldPassword.toggle()}) {
                                        Image(systemName: showOldPassword ?  "eye.slash" : "eye")
                                            .foregroundColor(.secondary)
                                    }
                                }.padding()
                                .background(Capsule().fill(Color.textFieldBackground))
                                
                            }
                            
                        }
                        if !isOldPasswordValid && firstClickToChange{
                            Text("enter_valid_password")
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 85)
                        }
                        
                    }
                    //User old pass view end
                    
                    
                    
                    //User new pass view start
                    VStack(alignment: .leading){
                        Text("new_password")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                            .padding(.leading, 85)
                        
                        HStack {
                            ZStack{
                                Circle()
                                    .frame(width: 55, height: 55)
                                    .foregroundColor(.textFieldBackground)
                                
                                Image(systemName: "lock")
                                    .font(.system(size: 20))
                                    .foregroundColor(.secondary)
                            }.padding(.trailing, 8)
                            
                            VStack(alignment: .leading, spacing: 4.0){
                                
                                HStack {
                                    if showNewPassword {
                                        TextField("new_password", text: $newPassword)
                                            .foregroundColor(.primary)
                                            .onReceive(Just(newPassword), perform: { newValue in
                                                isNewPasswordValid = validation.validatePassword(password: newPassword)
                                            })
                                    } else {
                                        SecureField("new_password", text: $newPassword)
                                            .foregroundColor(.primary)
                                            .onReceive(Just(newPassword), perform: { newValue in
                                                isNewPasswordValid = validation.validatePassword(password: newPassword)
                                            })
                                    }
                                    Button(action: { self.showNewPassword.toggle()}) {
                                        Image(systemName: showNewPassword ?  "eye.slash" : "eye")
                                            .foregroundColor(.secondary)
                                    }
                                }.padding()
                                .background(Capsule().fill(Color.textFieldBackground))
                                
                            }
                            
                        }
                        if !isNewPasswordValid && firstClickToChange{
                            Text("enter_valid_password")
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 85)
                        }
                        
                    }
                    //User new pass view end
                    
                    
                    //User new pass validate view start
                    VStack(alignment: .leading){
                        Text("new_password_again")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                            .padding(.leading, 85)
                        
                        HStack {
                            ZStack{
                                Circle()
                                    .frame(width: 55, height: 55)
                                    .foregroundColor(.textFieldBackground)
                                
                                Image(systemName: "lock")
                                    .font(.system(size: 20))
                                    .foregroundColor(.secondary)
                            }.padding(.trailing, 8)
                            
                            VStack(alignment: .leading, spacing: 4.0){
                                
                                HStack {
                                    if showNewPasswordAgain {
                                        TextField("new_password_again", text: $newPasswordAgain)
                                            .foregroundColor(.primary)
                                            .onReceive(Just(newPasswordAgain), perform: { newValue in
                                                isNewPasswordAgainValid = validation.validatePassword(password: newPasswordAgain)
                                            })
                                    } else {
                                        SecureField("new_password_again", text: $newPasswordAgain)
                                            .foregroundColor(.primary)
                                            .onReceive(Just(newPasswordAgain), perform: { newValue in
                                                isNewPasswordAgainValid = validation.validatePassword(password: newPasswordAgain)
                                            })
                                    }
                                    Button(action: { self.showNewPasswordAgain.toggle()}) {
                                        Image(systemName: showNewPasswordAgain ?  "eye.slash" : "eye")
                                            .foregroundColor(.secondary)
                                    }
                                }.padding()
                                .background(Capsule().fill(Color.textFieldBackground))
                                
                            }
                            
                        }
                        if !isNewPasswordAgainValid && firstClickToChange{
                            Text("enter_valid_password")
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 85)
                        }
                        
                    }
                    //User new pass validate view end
                    
                    
                    //Change password button
                    /*Button(action: {
                        firstClickToChange = true
                        if isOldPasswordValid && isNewPasswordValid && isNewPasswordAgainValid{
                            if oldPassword != newPassword{
                                if newPassword == newPasswordAgain{
                                    let request =  PasswordChangeRequest(oldPassword: oldPassword,
                                                                         newPassword: newPassword,
                                                                         confirmPassword: newPasswordAgain)
                                    changePassword(request: request)
                                }else{
                                    GrowingNotificationBanner(title: NSLocalizedString("password_new_again_not_same", comment: "")
                                                              , style: .warning).show(bannerPosition: .top)
                                }
                            }else{
                                GrowingNotificationBanner(title: NSLocalizedString("old_new_password_same", comment: "")
                                                          , style: .warning).show(bannerPosition: .top)
                            }
                        }
                    }, label: {
                        Text("password_change")
                            .frame(maxWidth: .infinity)
                            .frame(height: 15.0)
                    })
                    .buttonStyle(FilledButtonStyle())
                    .padding(.top, 10)*/
                    
                    
                    LoadingButton(action: {
                        firstClickToChange = true
                        if isOldPasswordValid && isNewPasswordValid && isNewPasswordAgainValid{
                            if oldPassword != newPassword{
                                if newPassword == newPasswordAgain{
                                    let request =  PasswordChangeRequest(oldPassword: oldPassword,
                                                                         newPassword: newPassword,
                                                                         confirmPassword: newPasswordAgain)
                                    changePassword(request: request)
                                }else{
                                    GrowingNotificationBanner(title: NSLocalizedString("password_new_again_not_same", comment: "")
                                                              , style: .warning).show(bannerPosition: .top)
                                }
                            }else{
                                GrowingNotificationBanner(title: NSLocalizedString("old_new_password_same", comment: "")
                                                          , style: .warning).show(bannerPosition: .top)
                            }
                        }
                    }, isLoading: $isLoading) {
                        Text("password_change")
                            .frame(maxWidth: .infinity)
                            .frame(height: 15.0).foregroundColor(Color.white)
                    } .padding(.top, 10)
                    
                    
                }
            }.padding(24)
        }
        .navigationTitle("password_change")
        .navigationBarTitleDisplayMode(.large)
    }
    
    
    
    // Change password api request
    private func changePassword(request: PasswordChangeRequest) {
        isLoading = true
        
        apiService.passwordChange(request: request, onSuccess: {(response) in
            print(response.toJson)
            isLoading = false
            MyKeychain.userPassword = newPassword
            oldPassword = ""
             newPassword = ""
             newPasswordAgain = ""
            GrowingNotificationBanner(title: NSLocalizedString("password_changed", comment: "")
                                      , style: .success).show(bannerPosition: .top)
        }, onFail: {(data) in
            isLoading = false
            firstClickToChange = false
            oldPassword = ""
             newPassword = ""
             newPasswordAgain = ""
            print(data ?? "")
        })
    }
    
}

struct PasswordChangeView_Previews: PreviewProvider {
    static var previews: some View {
        PasswordChangeView()
    }
}
