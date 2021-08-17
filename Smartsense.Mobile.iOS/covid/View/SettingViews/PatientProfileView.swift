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
    
    init() {
        
        //this changes the "thumb" that selects between items
        UISegmentedControl.appearance().selectedSegmentTintColor = UIColor(.componentColor)
        
        //and this changes the color for the whole "bar" background
        UISegmentedControl.appearance().backgroundColor = UIColor(.textFieldBackground)

        //this will change the font size
        UISegmentedControl.appearance().setTitleTextAttributes([.font : UIFont.preferredFont(forTextStyle: .largeTitle)], for: .normal)

        //these lines change the text color for various states
        UISegmentedControl.appearance().setTitleTextAttributes([.foregroundColor : UIColor(.colorPrimary)], for: .selected)
        UISegmentedControl.appearance().setTitleTextAttributes([.foregroundColor : UIColor(.secondary)], for: .normal)
    }
    
    @State var userIdentity: String = ""
    
    @State var isIdentityValid: Bool = false
    
    @State var firstClickToChange: Bool = false
    @State var isLoading: Bool = false
    @State var progressViewText = ""
    
    private let apiService: ApiServiceProtocol = ApiService()
    private let apiText = ApiConstantText()
    private var validation = Validation()
    
    @State var selectedDate: Date = Date.init()
    
    @State private var selectedGender = NSLocalizedString("dont_specify", comment: "")
    let genders = [NSLocalizedString("dont_specify", comment: ""),
                   NSLocalizedString("female", comment: ""),
                   NSLocalizedString("male", comment: "")]
    
    @State private var selectedTrackingStatus = NSLocalizedString("home_quarantine", comment: "")
    let trackingTypes = [NSLocalizedString("home_quarantine", comment: ""),
                         NSLocalizedString("hospital_quarantine", comment: ""),
                         NSLocalizedString("contact_quarantine", comment: ""),
                         NSLocalizedString("being_discharged", comment: ""),
                         NSLocalizedString("status_other", comment: "")]
    
    @State private var selectedBloodGroup = "A"
    let bloodGroups = ["A", "B", "AB", "0"]
    
    @State private var selectedBloodGroupRh = "+"
    let bloodGroupRh = ["+", "-"]
    
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
                            }.padding(.trailing, 8)
                            
                            TextField("user_identity", text: $userIdentity)
                                .keyboardType(.numberPad)
                                .textFieldStyle(CapsuleTextFieldStyle())
                                .onReceive(Just(userIdentity), perform: { newValue in
                                    isIdentityValid = validation.validateTCIdentity(tc: userIdentity)
                                })
                                
                        }
                        
                        if !isIdentityValid && firstClickToChange{
                            Text("enter_valid_identity")
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 75)
                        }
                        
                    }
                    //User identity view end
                    
                    
                    //Date of birth view start
                    VStack(alignment: .leading){
                        Text("user_date_of_birth")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                            .padding(.leading, 75)
                            .offset(y: 10)
                        
                        HStack {
                            ZStack{
                                Circle()
                                    .frame(width: 55, height: 55)
                                    .foregroundColor(.textFieldBackground)
                                    .opacity(1)
                                
                                Image(systemName: "calendar")
                                    .font(.system(size: 20))
                                    .foregroundColor(.secondary)
                                
                            }.padding(.trailing, 12)
                            
                            
                            DatePicker("", selection: $selectedDate, in: ...Date(), displayedComponents: .date)
                                .accentColor(.colorPrimary)
                                .labelsHidden()
                            
                            Spacer()
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
                                
                                Image("gender")
                                    .resizable()
                                    .renderingMode(.template)
                                    .foregroundColor(.secondary)
                                    .scaledToFit()
                                    .frame(width: 25, height: 25)
                                    .padding(.top, 1)
                                
                            }.padding(.trailing, 12)
                            
                            
                            Picker(selectedGender, selection: $selectedGender) {
                                ForEach(genders, id: \.self) {
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
                    }
                    //Gender view end
                    
                    
                    //User Status view start
                    VStack(alignment: .leading, spacing: 4.0){
                        Text("contact_status")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                            .padding(.leading, 75)
                        
                        HStack {
                            ZStack{
                                Circle()
                                    .frame(width: 50, height: 50)
                                    .foregroundColor(.textFieldBackground)
                                    .opacity(1)
                                
                                Image(systemName: "staroflife")
                                    .font(.system(size: 20))
                                    .foregroundColor(.secondary)
                                
                            }.padding(.trailing, 12)
                            
                            
                            Picker(selectedTrackingStatus, selection: $selectedTrackingStatus) {
                                ForEach(trackingTypes, id: \.self) {
                                    Text($0)
                                }
                            }
                            .pickerStyle(MenuPickerStyle())
                            .accentColor(.colorPrimary)
                            .padding()
                            .background(RoundedRectangle(cornerRadius: 10)
                                            .fill(Color.greyColor)
                                            .frame(height: 40)
                                            .opacity(0.2))
                            Spacer()
                        }
                    }
                    //User Status view end
                    
                    
                    //User Status view start
                    VStack(alignment: .leading, spacing: 4.0){
                        Text("user_blood_group")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                            .padding(.leading, 75)
                            .padding(.bottom, 8)
                        
                        HStack (alignment: .top){
                            ZStack{
                                Circle()
                                    .frame(width: 50, height: 50)
                                    .foregroundColor(.textFieldBackground)
                                    .opacity(1)
                                
                                Image(systemName: "drop")
                                    .font(.system(size: 20))
                                    .foregroundColor(.secondary)
                                
                            }.padding(.trailing, 12)
                            
                            VStack{
                                Picker(selectedBloodGroup, selection: $selectedBloodGroup) {
                                    ForEach(bloodGroups, id: \.self) {
                                        Text($0)
                                    }
                                }
                                .pickerStyle(SegmentedPickerStyle())
                                
                                Picker(selectedBloodGroupRh, selection: $selectedBloodGroupRh) {
                                    ForEach(bloodGroupRh, id: \.self) {
                                        Text($0)
                                    }
                                }
                                .pickerStyle(SegmentedPickerStyle())
                            
                            }
                            
                            Spacer()
                        }
                    }
                    //User Status view end
                    
                    
                    /*Button(action: {
                        firstClickToChange = true
                        if 1 == 1{
                            if isIdentityValid {
                                //let request =  UserRegisterRequest(name: name, surname: surname,email: email, password: password)
                                //register(request: request)
                            }
                        }else if 1 == 2{
                            if isIdentityValid {
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
                    .padding(.top, 10)*/
                    
                    
                    LoadingButton(action: {
                        firstClickToChange = true
                        if 1 == 1{
                            if isIdentityValid {
                                //let request =  UserRegisterRequest(name: name, surname: surname,email: email, password: password)
                                //register(request: request)
                            }
                        }else if 1 == 2{
                            if isIdentityValid {
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
            }
        }.navigationTitle("patient_data")
        .navigationBarTitleDisplayMode(.large)
        /*.modal(isPresented: $modalIsDisplayed) {
         
         }*/
    }
}

struct PatientProfileView_Previews: PreviewProvider {
    static var previews: some View {
        PatientProfileView()
        //.preferredColorScheme(.dark)
    }
}

