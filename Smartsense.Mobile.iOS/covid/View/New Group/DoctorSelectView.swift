//
//  ProfileView.swift
//  covid
//
//  Created by SmartSense on 24.06.2021.
//

import SwiftUI
import Alamofire
import NotificationBannerSwift

struct DoctorSelectView: View {
    @State var isDoctorSelected = false
    @State private var hospitalSelection = -1
    @State private var hospitals = ["Hacettepe", "Batman Yaşam Hastanesi", "Gazi Hastanesi"]
    @State var hospitalSearchText = ""
    
    @State private var doctorSelection = -1
    @State private var doctors = ["Mehmet Oz", "Mustafa Sönmez"]
    @State var doctorSearchText = ""
    
    @State var isLoading: Bool = false
    @State var beforeAddDoctorTextIsShowing: Bool = true
    @State var doctorAddInfoIsShowing: Bool = true
    @State var addChangeButtonText = NSLocalizedString("add_doctor", comment: "")
    
    init() {
        UITableView.appearance().sectionFooterHeight = 0
        UITableViewCell.appearance().backgroundColor = UIColor.clear
    }
    
    var body: some View {
        VStack {
            Form{
                Section{
                    Picker(selection: $hospitalSelection, label: Text("hospital")){
                        /* ForEach(0 ..< hospitals.count){
                         Text(self.hospitals[$0]).tag($0)
                         }*/
                        
                        SearchBar(text: $hospitalSearchText, searchText: NSLocalizedString("search_hospital", comment: ""))
                        if self.hospitalSearchText != "" {
                            if hospitals.filter{$0.hasPrefix(hospitalSearchText)}.count != 0{
                                ForEach(0..<hospitals.filter{$0.hasPrefix(hospitalSearchText)}.count) {
                                    Text(hospitals.filter{$0.hasPrefix(hospitalSearchText)}[$0]).tag($0)
                                }.resignKeyboardOnDragGesture()
                            }
                            
                            
                        }else if self.hospitalSearchText == "" {
                            ForEach(0..<hospitals.count) {
                                Text(hospitals[$0]).tag($0)
                            }.resignKeyboardOnDragGesture()
                        }
                    }.padding(.vertical, 8)
                    
                    Picker(selection: $doctorSelection, label: Text("doctor")){
                        
                        SearchBar(text: $doctorSearchText, searchText: NSLocalizedString("search_doctor", comment: ""))
                        if self.doctorSearchText != "" {
                            if doctors.filter{$0.hasPrefix(doctorSearchText)}.count != 0{
                                ForEach(0..<doctors.filter{$0.hasPrefix(doctorSearchText)}.count) {
                                    Text(doctors.filter{$0.hasPrefix(doctorSearchText)}[$0]).tag($0)
                                }.resignKeyboardOnDragGesture()
                            }
                            
                            
                        }else if self.doctorSearchText == "" {
                            ForEach(0..<doctors.count) {
                                Text(doctors[$0]).tag($0)
                            }.resignKeyboardOnDragGesture()
                        }
                    }.padding(.vertical, 8)
                    
                }
                
                if doctorAddInfoIsShowing{
                    Section{
                        Text("add_doctor_info")
                            .font(.caption)
                            .foregroundColor(.secondary)
                            .padding(.vertical, 8)
                        
                        if beforeAddDoctorTextIsShowing{
                            //Before add doctor text
                            Section{
                                Text("add_doctor_info_before")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                                    .padding(.vertical, 8)
                            }
                        }
                    }
                }
                
                Section{
                    //Doctor save change button
                    LoadingButton(action: {
                        if hospitalSelection != -1 && doctorSelection != -1{
                            doctorAddInfoIsShowing = false
                            addChangeButtonText = NSLocalizedString("change_doctor", comment: "")
                        }else{
                            GrowingNotificationBanner(title: NSLocalizedString("select_doctor_not_empty", comment: ""), style: .danger).show(bannerPosition: .top)
                        }
                    }, isLoading: $isLoading) {
                        Text(addChangeButtonText)
                            .frame(maxWidth: .infinity)
                            .frame(height: 15.0)
                            .foregroundColor(Color.white)
                    }.frame(maxWidth: .infinity)
                    
                }.listRowBackground(Color.clear)
                
            }.padding(.top, -130)
            
        }.navigationTitle("doctor")
        .navigationBarTitleDisplayMode(.inline)
        
    }
}


struct DoctorSelectView_Previews: PreviewProvider {
    static var previews: some View {
        DoctorSelectView()
            //.preferredColorScheme(/*@START_MENU_TOKEN@*/.dark/*@END_MENU_TOKEN@*/)
    }
}
