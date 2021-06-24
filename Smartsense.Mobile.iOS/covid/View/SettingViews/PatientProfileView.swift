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

struct PatientProfileView: View {
    
    @State var name: String = ""
    @State var surname: String = ""
    @State var phone: String = ""
    @State var email: String = ""
    
    @State var isNameValid: Bool = false
    @State var isSurnameValid: Bool = false
    @State var isPhoneValid: Bool = false
    
    @State var firstClickToChange: Bool = false
    @State var isProgressViewShowing = false
    @State var progressViewText = ""
    
    private let apiService: ApiServiceProtocol = ApiService()
    private let apiText = ApiConstantText()
    private var validation = Validation()
    
    @State var selectedDate: Date = Date.init()
    
    @State private var selectedGender = NSLocalizedString("dont_specify", comment: "")
    let strengths = [NSLocalizedString("dont_specify", comment: ""), NSLocalizedString("female", comment: ""), NSLocalizedString("male", comment: "")]
    
    var body: some View {
        ScrollView(.vertical) {
            VStack{
                VStack(alignment: .center, spacing: 16){
                    
                    //User identity view start
                    VStack(alignment: .leading){
                        Text("user_identity")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                            .padding(.leading, 75)
                        
                        HStack {
                            
                            ZStack{
                                Circle()
                                    .frame(width: 55, height: 55)
                                    .foregroundColor(.textFieldBackground)
                                
                                Image(systemName: "person")
                                    .font(.system(size: 20))
                                    .foregroundColor(.secondary)
                            }
                            
                            TextField("user_identity", text: $name)
                                .foregroundColor(.primary)
                                .onReceive(Just(name), perform: { newValue in
                                    isNameValid = validation.validateName(name: name)
                                }).padding()
                                .background(Capsule().fill(Color.textFieldBackground))
                        }
                        
                        if !isNameValid && firstClickToChange{
                            Text("enter_valid_name")
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 10)
                        }
                        
                    }
                    //User identity view end
                    
                    
                    //Date of birth view start
                    VStack(alignment: .leading){
                        Text("user_date_of_birth")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                            .padding(.leading, 75)
                        
                        
                        HStack {
                            ZStack{
                                Circle()
                                    .frame(width: 55, height: 55)
                                    .foregroundColor(.textFieldBackground)
                                    .opacity(1)
                                
                                Image(systemName: "calendar")
                                    .font(.system(size: 20))
                                    .foregroundColor(.secondary)
                                
                            }
                            
                            
                            DatePicker("", selection: $selectedDate, in: ...Date(), displayedComponents: .date)
                                .accentColor(.colorPrimary)
                                .padding()
                                .labelsHidden()
                            
                            Spacer()
                            /*TextField("user_date_of_birth", text: $surname)
                             .foregroundColor(.primary)
                             .onReceive(Just(surname), perform: { newValue in
                             isSurnameValid = validation.validateName(name: surname)
                             }).padding()
                             .background(Capsule().fill(Color.textFieldBackground))*/
                        }
                        
                        if !isSurnameValid && firstClickToChange{
                            Text("enter_valid_surname")
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 10)
                        }
                        
                    }
                    //Date of birth view end
                    
                    
                    //Gender view start
                    VStack(alignment: .leading, spacing: 4.0){
                        Text("user_gender")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                            .padding(.leading, 75)
                        
                        HStack {
                            
                            ZStack{
                                Circle()
                                    .frame(width: 50, height: 50)
                                    .foregroundColor(.textFieldBackground)
                                    .opacity(1)
                                
                                Image(systemName: "phone")
                                    .font(.system(size: 20))
                                    .foregroundColor(.secondary)
                                
                            }.padding(.trailing, 18)
                            
                            
                            Picker(selectedGender, selection: $selectedGender) {
                                ForEach(strengths, id: \.self) {
                                    Text($0)
                                }
                            }
                            .pickerStyle(MenuPickerStyle())
                            .accentColor(.colorPrimary)
                            .padding()
                            .background( RoundedRectangle(cornerRadius: 10)
                                            .fill(Color.greyColor)
                                            .frame(height: 40)
                                            .opacity(0.2))
                            
                            
                            Spacer()
                        }
                        
                        if !isPhoneValid && firstClickToChange{
                            Text("enter_valid_phone")
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 10)
                        }
                    }
                    //Gender view end
                    
                    
                    //Email view start
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
                                .disabled(true)
                            
                        }.padding()
                        .background(Capsule().fill(Color.textFieldBackground))
                    }
                    //Email view end
                    
                    
                    Button(action: {
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
                            .frame(height: 50.0)
                        
                        
                    })
                    .buttonStyle(BorderlessButtonStyle())
                    .font(.title3)
                    .foregroundColor(.white)
                    .background(Color.buttonBackground)
                    .cornerRadius(90.0)
                    .padding(.top, 10)
                    
                    
                    
                }.padding(24)
            }
        }.navigationTitle("patient_data")
        .navigationBarTitleDisplayMode(.inline)
        /*.modal(isPresented: $modalIsDisplayed) {
         
         }*/
    }
}

struct PatientProfileView_Previews: PreviewProvider {
    static var previews: some View {
        PatientProfileView()
    }
}

