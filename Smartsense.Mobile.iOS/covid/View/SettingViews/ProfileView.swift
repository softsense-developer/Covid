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

struct ProfileView: View {
    @State var name: String = ""
    @State var surname: String = ""
    @State var phone: String = ""
    @State var email: String = ""
    
    @State var isNameValid: Bool = false
    @State var isSurnameValid: Bool = false
    @State var isPhoneValid: Bool = false
    
    @State var firstClickToChange: Bool = false
    @State var isLoading: Bool = false
    
    private let apiService: ApiServiceProtocol = ApiService()
    private let apiText = ApiConstantText()
    private var validation = Validation()
    
    
    var body: some View {
        ScrollView(.vertical) {
            
            VStack(alignment: .center, spacing: 16){
                
                //Name view start
                VStack(alignment: .leading){
                    Text("name")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                        .padding(.leading, 75)
                    
                    HStack {
                        
                        ZStack{
                            Circle()
                                .frame(width: 55, height: 55)
                                .foregroundColor(.textFieldBackground)
                                .opacity(1)
                            
                            Image(systemName: "person")
                                .font(.system(size: 20))
                                .foregroundColor(.secondary)
                            
                        }
                        
                        TextField("name", text: $name)
                            .textFieldStyle(CapsuleTextFieldStyle())
                            .onReceive(Just(name), perform: { newValue in
                                isNameValid = validation.validateName(name: name)
                            })
                    }
                    
                    if !isNameValid && firstClickToChange{
                        Text("enter_valid_name")
                            .font(.caption)
                            .foregroundColor(.red)
                            .padding(.leading, 75)
                    }
                    
                }
                //Name view end
                
                //Surname view start
                VStack(alignment: .leading){
                    Text("surname")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                        .padding(.leading, 75)
                    
                    HStack {
                        ZStack{
                            Circle()
                                .frame(width: 55, height: 55)
                                .foregroundColor(.textFieldBackground)
                                .opacity(1)
                            
                            Image(systemName: "person")
                                .font(.system(size: 20))
                                .foregroundColor(.secondary)
                            
                        }
                        
                        TextField("surname", text: $surname)
                            .textFieldStyle(CapsuleTextFieldStyle())
                            .onReceive(Just(surname), perform: { newValue in
                                isSurnameValid = validation.validateName(name: surname)
                            })
                    }
                    
                    if !isSurnameValid && firstClickToChange{
                        Text("enter_valid_surname")
                            .font(.caption)
                            .foregroundColor(.red)
                            .padding(.leading, 75)
                    }
                    
                }
                //Surname view end
                
                
                //Phone view start
                VStack(alignment: .leading, spacing: 4.0){
                    Text("user_phone")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                        .padding(.leading, 75)
                    
                    HStack {
                        ZStack{
                            Circle()
                                .frame(width: 55, height: 55)
                                .foregroundColor(.textFieldBackground)
                                .opacity(1)
                            
                            Image(systemName: "phone")
                                .font(.system(size: 20))
                                .foregroundColor(.secondary)
                            
                        }
                        
                        TextField("user_phone_hint", text: $phone)
                            .textFieldStyle(CapsuleTextFieldStyle())
                            .keyboardType(.phonePad)
                            .onReceive(Just(phone), perform: { newValue in
                                isPhoneValid = validation.validaPhoneNumber(phoneNumber: phone)
                            })
                        
                    }
                    
                    if !isPhoneValid && firstClickToChange{
                        Text("enter_valid_phone")
                            .font(.caption)
                            .foregroundColor(.red)
                            .padding(.leading, 75)
                    }
                }
                //Phone view end
                
                
                //Email view start
                VStack(alignment: .leading, spacing: 4.0){
                    Text("email")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                        .padding(.leading, 75)
                    
                    HStack {
                        ZStack{
                            Circle()
                                .frame(width: 55, height: 55)
                                .foregroundColor(.textFieldBackground)
                                .opacity(1)
                            
                            Image(systemName: "envelope")
                                .font(.system(size: 20))
                                .foregroundColor(.secondary)
                            
                        }
                        
                        TextField("email", text: $email)
                            .textFieldStyle(CapsuleTextFieldStyle())
                            .disabled(true)
                        
                    }
                }
                //Email view end
                
                
                /*Button(action: {
                    firstClickToChange = true
                    if 1 == 1{
                        if isNameValid && isSurnameValid && isPhoneValid {
                            //let request =  UserRegisterRequest(name: name, surname: surname,email: email, password: password)
                            //register(request: request)
                        }
                    }else if 1 == 2{
                        if isNameValid && isSurnameValid && isPhoneValid{
                            //let request =  UserLoginRequest(email: email, password: password, isRemember: true)
                            //login(request: request)
                        }
                    }
                }, label: {
                    Text("update")
                        .frame(maxWidth: .infinity)
                        .frame(height: 15.0)
                })
                .buttonStyle(FilledButtonStyle())
                .padding(.top, 10)
                .foregroundColor(Color.white)*/
                
                LoadingButton(action: {
                    firstClickToChange = true
                    if 1 == 1{
                        if isNameValid && isSurnameValid && isPhoneValid {
                            //let request =  UserRegisterRequest(name: name, surname: surname,email: email, password: password)
                            //register(request: request)
                        }
                    }else if 1 == 2{
                        if isNameValid && isSurnameValid && isPhoneValid{
                            //let request =  UserLoginRequest(email: email, password: password, isRemember: true)
                            //login(request: request)
                        }
                    }
                }, isLoading: $isLoading) {
                    Text("update")
                        .frame(maxWidth: .infinity)
                        .frame(height: 15.0)
                        .foregroundColor(Color.white)
                }.padding(.top, 10)
                
                
                
            }.padding(24)
            
        }.navigationBarTitleDisplayMode(.large)
        .navigationTitle("user_data")
        .padding(.top, 4)
        .padding(.bottom, 4)
        
        
        
    }
}

struct ProfileView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileView()
        //.preferredColorScheme(.dark)
    }
}

