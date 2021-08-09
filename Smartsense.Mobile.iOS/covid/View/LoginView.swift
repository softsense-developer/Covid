//
//  LoginView.swift
//  covid
//
//  Created by SmartSense on 1.06.2021.
//

import SwiftUI
import Combine
import Alamofire
import NotificationBannerSwift

struct LoginView: View {
   
    @StateObject var observedLoginData = ObservedLoginData()
    
    
    var body: some View {
        if !observedLoginData.isLoggedIn && !observedLoginData.isRegistered{
            LoginSignUpView().environmentObject(observedLoginData)
        }else{
            if observedLoginData.isLoggedIn{
                MainView()
            }else{
                EmailVerifyView()
            }
        }
    }
    

    
}


struct LoginSignUpView: View {
    @EnvironmentObject var observedLoginData: ObservedLoginData
    
    @State var name: String = ""
    @State var surname: String = ""
    @State var email: String = ""
    @State var password: String = ""
    @State private var showPassword = false
    @State var viewType: Int = Constant.SignUpView; //0 = Register, 1 = Login, 2 = Password Forget
    @State var isNameValid: Bool = false
    @State var isSurnameValid: Bool = false
    @State var isEmailValid: Bool = false
    @State var isPasswordValid: Bool = false
    @State var firstClicktoSignUpLogin: Bool = false
    @State var sheetType: Int = Constant.TermOfConditions;
    @State var showSheet: Bool = false
    @State var isProgressViewShowing = false
    @State var progressViewText = ""
    
    private let apiService: ApiServiceProtocol = ApiService()
    private let apiText = ApiConstantText()
    private var validation = Validation()
    
    
    var body: some View {
        GeometryReader { geometry in
            ScrollView(.vertical) {
                VStack(alignment: .leading, spacing: 4.0){
                    /*
                     ** Page title
                     */
                    if viewType == Constant.SignUpView{
                        Text("create_account")
                            .font(.title2)
                            .fontWeight(.semibold)
                            .foregroundColor(.primary)
                            .padding(.top, /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/)
                    }else if viewType == Constant.LoginView{
                        Text("login")
                            .font(.title2)
                            .fontWeight(.semibold)
                            .foregroundColor(.primary)
                            .padding(.top, /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/)
                    }else if viewType == Constant.PasswordForgetView{
                        Text("reset_password")
                            .font(.title2)
                            .fontWeight(.semibold)
                            .foregroundColor(.primary)
                            .padding(.top, /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/)
                    }
                    
                    /*
                     ** Name-Surname TextFields
                     */
                    if viewType == Constant.SignUpView{
                        HStack(alignment: .top){
                            VStack(alignment: .leading){
                                Text("name")
                                    .font(.subheadline)
                                    .foregroundColor(.secondary)
                                    .padding(.leading, 10)
                                
                                HStack {
                                    Image(systemName: "person")
                                        .foregroundColor(.secondary)
                                    TextField("name", text: $name)
                                        .foregroundColor(.primary)
                                        .onReceive(Just(name), perform: { newValue in
                                            isNameValid = validation.validateName(name: name)
                                        })
                                }.padding()
                                .background(Capsule().fill(Color.textFieldBackground))
                                
                                if !isNameValid && firstClicktoSignUpLogin{
                                    Text("enter_valid_name")
                                        .font(.caption)
                                        .foregroundColor(.red)
                                        .padding(.leading, 10)
                                }
                                
                            }
                            
                            VStack(alignment: .leading){
                                Text("surname")
                                    .font(.subheadline)
                                    .foregroundColor(.secondary)
                                    .padding(.leading, 10)
                                
                                HStack {
                                    TextField("surname", text: $surname)
                                        .foregroundColor(.primary)
                                        .onReceive(Just(surname), perform: { newValue in
                                            isSurnameValid = validation.validateName(name: surname)
                                        })
                                    
                                }.padding()
                                .background(Capsule().fill(Color.textFieldBackground))
                                
                                if !isSurnameValid && firstClicktoSignUpLogin{
                                    Text("enter_valid_surname")
                                        .font(.caption)
                                        .foregroundColor(.red)
                                        .padding(.leading, 10)
                                }
                                
                            }
                        }
                        .padding(.top, 10)
                    }
                    
                    /*
                     ** E-mail TextField
                     */
                    VStack(alignment: .leading, spacing: 4.0){
                        Text("email")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                            .padding(.leading, 10)
                        
                        HStack {
                            Image(systemName: "envelope")
                                .foregroundColor(.secondary)
                            TextField("email", text: $email)
                                .foregroundColor(.primary)
                                .keyboardType(.emailAddress)
                                .onReceive(Just(email), perform: { newValue in
                                    isEmailValid = validation.validateEmail(email: email)
                                })
                            
                        }.padding()
                        .background(Capsule().fill(Color.textFieldBackground))
                        
                        if !isEmailValid && firstClicktoSignUpLogin{
                            Text("enter_valid_email")
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 10)
                        }
                        
                        
                    }.padding(.top, /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/)
                    
                    /*
                     ** Password TextField
                     */
                    if viewType == Constant.LoginView || viewType == Constant.SignUpView{
                        VStack(alignment: .leading, spacing: 4.0){
                            Text("password")
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                                .padding(.leading, 10)
                            
                            HStack {
                                Image(systemName: "lock")
                                    .foregroundColor(.secondary)
                                if showPassword {
                                    TextField("password", text: $password)
                                        .foregroundColor(.primary)
                                        .onReceive(Just(password), perform: { newValue in
                                            isPasswordValid = validation.validatePassword(password: password)
                                        })
                                } else {
                                    SecureField("password", text: $password)
                                        .foregroundColor(.primary)
                                        .onReceive(Just(password), perform: { newValue in
                                            isPasswordValid = validation.validatePassword(password: password)
                                        })
                                }
                                Button(action: { self.showPassword.toggle()}) {
                                    Image(systemName: showPassword ?  "eye.slash" : "eye")
                                        .foregroundColor(.secondary)
                                }
                            }.padding()
                            .background(Capsule().fill(Color.textFieldBackground))
                            
                            
                            if !isPasswordValid && firstClicktoSignUpLogin{
                                Text("enter_valid_password")
                                    .font(.caption)
                                    .foregroundColor(.red)
                                    .padding(.leading, 10)
                            }
                            
                            
                        }.padding(.top, /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/)
                    }
                    
                    
                    /*
                     ** Legal text
                     */
                    if viewType == Constant.SignUpView{
                        VStack(alignment: .center, spacing: 0) {
                            HStack(alignment: .top){
                                Text("sign_up_legal_1")
                                Text("sign_up_legal_2")
                                    .foregroundColor(.colorPrimary)
                                    .onTapGesture {
                                        showSheet = true
                                        sheetType = Constant.TermOfConditions
                                    }
                            }
                            HStack(alignment: .top){
                                Text("sign_up_legal_3")
                                Text("sign_up_legal_4")
                                    .foregroundColor(.colorPrimary)
                                    .onTapGesture {
                                        showSheet = true
                                        sheetType = Constant.PrivacyPolicy
                                    }
                                Text("sign_up_legal_5")
                            }
                            
                        }.font(.caption)
                        .foregroundColor(.secondary)
                        .padding(.all, /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/)
                    }
                    
                    
                    /*
                     ** Login - Sign up - Reset Password button
                     */
                    Button(action: {
                        firstClicktoSignUpLogin = true
                        if viewType == Constant.SignUpView{
                            if isNameValid && isSurnameValid && isEmailValid && isPasswordValid{
                                let request =  UserRegisterRequest(name: name, surname: surname,email: email, password: password)
                                register(request: request)
                            }
                        }else if viewType == Constant.LoginView{
                            if isEmailValid && isPasswordValid{
                                let request =  UserLoginRequest(email: email, password: password, isRemember: true)
                                login(request: request)
                            }
                        }else if viewType == Constant.PasswordForgetView{
                            if isEmailValid{
                                let request =  UserPassForgetRequest(email: email)
                                passwordForget(request: request)
                            }
                        }
                    }, label: {
                        if viewType == Constant.SignUpView{
                            Text("sign_up")
                                .frame(maxWidth: .infinity)
                                .frame(height: 50.0)
                        }else if viewType == Constant.LoginView{
                            Text("login")
                                .frame(maxWidth: .infinity)
                                .frame(height: 50.0)
                        }else if viewType == Constant.PasswordForgetView{
                            Text("send_reset")
                                .frame(maxWidth: .infinity)
                                .frame(height: 50.0)
                        }
                        
                    })
                    .buttonStyle(BorderlessButtonStyle())
                    .font(.title3)
                    .foregroundColor(.white)
                    .background(Color.buttonBackground)
                    .cornerRadius(90.0)
                    .padding(.top, 10)
                    
                    
                    /*
                     ** Have account - create account text button
                     */
                    Button(action: {
                        if viewType == Constant.SignUpView{
                            viewType = Constant.LoginView
                        }else if viewType == Constant.LoginView{
                            viewType = Constant.SignUpView
                        }else if viewType == Constant.PasswordForgetView{
                            viewType = Constant.SignUpView
                        }
                        firstClicktoSignUpLogin = false
                    }, label: {
                        if viewType == Constant.SignUpView{
                            Text("have_account")
                                .font(.subheadline)
                                .fontWeight(.semibold)
                        }else if viewType == Constant.LoginView{
                            Text("create_an_account")
                                .font(.subheadline)
                                .fontWeight(.semibold)
                        }else if viewType == Constant.PasswordForgetView{
                            Text("create_an_account")
                                .font(.subheadline)
                                .fontWeight(.semibold)
                        }
                    })
                    .buttonStyle(BorderlessButtonStyle())
                    .foregroundColor(.secondary)
                    .padding(.top, 20)
                    .padding(.bottom, /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/)
                    .frame(maxWidth: .infinity, alignment: .center)
                    
                    if viewType == Constant.LoginView{
                        /*
                         ** Forget password - Have account text button
                         */
                        Button(action: {
                            if viewType == Constant.LoginView{
                                viewType = Constant.PasswordForgetView
                            }else if viewType == Constant.PasswordForgetView{
                                viewType = Constant.LoginView
                            }
                            firstClicktoSignUpLogin = false
                        }, label: {
                            if viewType == Constant.LoginView{
                                Text("forget_password")
                                    .font(.subheadline)
                                    .fontWeight(.semibold)
                            }else if viewType == Constant.PasswordForgetView{
                                Text("have_account")
                                    .font(.subheadline)
                                    .fontWeight(.semibold)
                            }
                        })
                        .buttonStyle(BorderlessButtonStyle())
                        .foregroundColor(.secondary)
                        .padding(.bottom, /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/)
                        .frame(maxWidth: .infinity, alignment: .center)
                    }
                    
                    
                }
                .sheet(isPresented: $showSheet) {
                    /*
                     ** Terms of conditions and privacy policy sheet
                     */
                    ScrollView{
                        if sheetType == Constant.TermOfConditions{
                            Text("terms_of_conditions")
                                .font(.title2)
                                .foregroundColor(.primary)
                                .padding([.top, .leading, .trailing])
                            Text("terms_of_conditions_text")
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                                .padding()
                        }else if sheetType == Constant.PrivacyPolicy{
                            Text("privacy_policy")
                                .font(.title2)
                                .foregroundColor(.primary)
                                .padding([.top, .leading, .trailing])
                            Text("privacy_policy_text")
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                                .padding()
                        }
                        
                    }
                }
                /*
                 ** like cardview
                 */
                .padding()
                .background(Color.componentColor)
                .cornerRadius(15)
                .shadow(radius: 0.5)
                /*.overlay(
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(Color(.sRGB, red: 150/255, green: 150/255, blue: 150/255, opacity: 0.1), lineWidth: 1)
                )*/
                /*
                 ** Scrollview center view
                 */
                .padding(.horizontal)
                .frame(width: geometry.size.width)
                .frame(minHeight: geometry.size.height)
                
            }.progressDialog(isShowing: $isProgressViewShowing, message: progressViewText)
        }
    }
    
    
    
    /*
     ** Api login method
     */
    private func login(request: UserLoginRequest) {
        progressViewText = NSLocalizedString("login_dot", comment: "")
        isProgressViewShowing = true
        
        apiService.login(request: request, onSuccess: {(response) in
            print(response)
            MyKeychain.userID = String(response.userId ?? -1)
            MyKeychain.userName = response.name ?? ""
            MyKeychain.userSurname = response.surname ?? ""
            MyKeychain.userPhone = response.phone ?? ""
            MyKeychain.userToken = response.token ?? ""
            MyKeychain.userRoleID = String(response.roleId ?? -1)
            MyKeychain.userEmail = email
            MyKeychain.userPassword = password
            MyKeychain.isEmailVerified = true
            //print(MyKeychain.userToken)
        
            //getPatientInfo()
            
            GrowingNotificationBanner(title: NSLocalizedString("login_dot", comment: ""), style: .success).show(bannerPosition: .top)
            observedLoginData.isLoggedIn = true
    
        },onFail: {(data) in
            isProgressViewShowing = false
            firstClicktoSignUpLogin = false
            password = ""
            print(data ?? "")
        })
    }
    
    /*
     ** Api register method
     */
    private func register(request: UserRegisterRequest) {
        progressViewText = NSLocalizedString("register_dot", comment: "")
        isProgressViewShowing = true
        
        apiService.register(request: request, onSuccess: {(response) in
            print(response)
            
            isProgressViewShowing = false
            MyKeychain.userName = name
            MyKeychain.userSurname = surname
            MyKeychain.userEmail = email
            MyKeychain.userPassword = password
            GrowingNotificationBanner(title: NSLocalizedString("verification_info", comment: ""), style: .success).show(bannerPosition: .top)
            observedLoginData.isRegistered = true
        }, onFail: {(data) in
            isProgressViewShowing = false
            print(data ?? "")
        })
    }
    
    
    /*
     ** Api password forget method
     */
    private func passwordForget(request: UserPassForgetRequest) {
        progressViewText = NSLocalizedString("sending_dot", comment: "")
        isProgressViewShowing = true
        
        apiService.passwordForget(request: request, onSuccess: {(response) in
            isProgressViewShowing = false
            print(response)
            let banner = GrowingNotificationBanner(title: NSLocalizedString("password_reset_info", comment: ""), style: .success);
            banner.dismiss()
            banner.show(bannerPosition: .top)
        }, onFail: {(data) in
            isProgressViewShowing = false
            print(data ?? "")
        })
    }
    
    
    /*
     ** If user logged in get user patient info
     */
    private func getPatientInfo() {
        apiService.getPatientInfo(onSuccess: {(response) in
            isProgressViewShowing = false
            print(response)
            MyKeychain.userIdentity = response.identityNumber ?? ""
            MyKeychain.userDoctorID = String(response.doctorId ?? -1)
            MyKeychain.userBirthday = response.dateOfBirth ?? ""
            MyKeychain.userTrackingType = String(response.userStatus ?? -1)
            MyKeychain.userDiagnosis = response.diagnosis ?? ""
            MyKeychain.userHospitalName = response.hospitalName ?? ""
            MyKeychain.userDoctorNameSurname = response.doctorName ?? ""
            MyKeychain.userBloodGroup = response.bloodGroup ?? ""
            
            GrowingNotificationBanner(title: NSLocalizedString("login_dot", comment: ""), style: .success).show(bannerPosition: .top)
            observedLoginData.isLoggedIn = true
        }, onFail: {(data) in
            isProgressViewShowing = false
            firstClicktoSignUpLogin = false
            password = ""
            print(data ?? "")
        })
    }
}

class ObservedLoginData: ObservableObject {
    @Published var isRegistered = false
    @Published var isLoggedIn = false
}


struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
        /*LoginView()
            .preferredColorScheme(/*@START_MENU_TOKEN@*/.dark/*@END_MENU_TOKEN@*/)*/
    }
}

