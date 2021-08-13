//
//  CompanionAddEdit.swift
//  covid
//
//  Created by SmartSense on 12.08.2021.
//

import SwiftUI
import Combine
import Alamofire
import NotificationBannerSwift

struct CompanionAdd: View {
    var body: some View {
        ScrollView(.vertical) {
            VStack{
                /*if companion != nil{
                 
                 }else{
                 
                 }*/
                CompanionAddEditContent()
            }
            
        }
        .navigationTitle("companion_add")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct CompanionAddEditContent: View {
    @State var name: String = ""
    @State var surname: String = ""
    @State var email: String = ""
    @State var password: String = ""
    
    @State var isNameValid: Bool = false
    @State var isSurnameValid: Bool = false
    @State var isEmailValid: Bool = false
    @State var isPasswordValid: Bool = false
    @State private var showPassword = false
    
    @State var saveEditClickFirst: Bool = false
    @State var isLoading: Bool = false
    
    
    let apiService: ApiServiceProtocol = ApiService()
    let apiText = ApiConstantText()
    var validation = Validation()
    
    var body: some View{
        VStack(alignment: .center, spacing: 16){
            
            //Companion name view start
            VStack(alignment: .leading){
                Text("companion_name")
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
                    
                    TextField("companion_name", text: $name)
                        .textFieldStyle(CapsuleTextFieldStyle())
                        .onReceive(Just(name), perform: { newValue in
                            isNameValid = validation.validateName(name: name)
                        })
                }
                
                if !isNameValid && saveEditClickFirst{
                    Text("enter_valid_name")
                        .font(.caption)
                        .foregroundColor(.red)
                        .padding(.leading, 75)
                }
                
            }
            //Companion name view end
            
            //Companion surname view start
            VStack(alignment: .leading){
                Text("companion_surname")
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
                    
                    TextField("companion_surname", text: $surname)
                        .textFieldStyle(CapsuleTextFieldStyle())
                        .onReceive(Just(surname), perform: { newValue in
                            isSurnameValid = validation.validateName(name: surname)
                        })
                }
                
                if !isSurnameValid && saveEditClickFirst{
                    Text("enter_valid_surname")
                        .font(.caption)
                        .foregroundColor(.red)
                        .padding(.leading, 75)
                }
                
            }
            //Companion surname view end
            
            //Companion email view start
            VStack(alignment: .leading){
                Text("companion_email")
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
                    
                    TextField("companion_email", text: $email)
                        .textFieldStyle(CapsuleTextFieldStyle())
                        .onReceive(Just(email), perform: { newValue in
                            isEmailValid = validation.validateEmail(email: email)
                        })
                }
                
                if !isEmailValid && saveEditClickFirst{
                    Text("enter_valid_email")
                        .font(.caption)
                        .foregroundColor(.red)
                        .padding(.leading, 75)
                }
                
            }
            //Companion email view end
            
            //Companion pass view start
            VStack(alignment: .leading){
                Text("companion_password")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .padding(.leading, 75)
                
                HStack {
                    ZStack{
                        Circle()
                            .frame(width: 55, height: 55)
                            .foregroundColor(.textFieldBackground)
                        
                        Image(systemName: "lock")
                            .font(.system(size: 20))
                            .foregroundColor(.secondary)
                    }
                    
                    VStack(alignment: .leading, spacing: 4.0){
                        
                        HStack {
                            if showPassword {
                                TextField("companion_password", text: $password)
                                    .foregroundColor(.primary)
                                    .onReceive(Just(password), perform: { newValue in
                                        isPasswordValid = validation.validatePassword(password: password)
                                    })
                            } else {
                                SecureField("companion_password", text: $password)
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
                        
                    }
                    
                }
                if !isPasswordValid && saveEditClickFirst{
                    Text("enter_valid_password")
                        .font(.caption)
                        .foregroundColor(.red)
                        .padding(.leading, 75)
                }
                
            }
            //Companion pass view end
            
            //Companion password info
            Text("companion_password_info")
                .font(.caption)
                .foregroundColor(.secondary)
                .padding(.all, 16)
                .background(
                    RoundedRectangle(cornerRadius: 16)
                        .fill(Color.textFieldBackground)
                        .frame(height: .infinity)
                )
            
            
            //Companion save edit button
            LoadingButton(action: {
                saveEditClickFirst = true
                if isNameValid && isSurnameValid && isEmailValid && isPasswordValid {
                    isLoading = true
                }
                
            }, isLoading: $isLoading) {
                Text("companion_add")
                    .frame(maxWidth: .infinity)
                    .frame(height: 15.0)
                    .foregroundColor(Color.white)
            }.frame(maxWidth: .infinity)
            .padding(.top, 8)
            
            
        }.padding(.all, 16)
    }
}



struct CompanionAddEdit_Previews: PreviewProvider {
    static var previews: some View {
        CompanionAdd()
    }
}
