//
//  MedicineAddEdit.swift
//  covid
//
//  Created by SmartSense on 10.08.2021.
//

import SwiftUI
import Combine
import Alamofire
import NotificationBannerSwift

struct MedicineAddEdit: View {
    var body: some View {
        ScrollView(.vertical) {
            VStack{
                /*if medicine != nil{
                    
                }else{
                    
                }*/
                MedicineAddEditContent(medicineName: "")
                
                
            }
        }
        .navigationTitle("medicine_add")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct MedicineEdit: View {
    var medicine: Medicine
   
    var body: some View {
        ScrollView(.vertical) {
            VStack{
                /*if medicine != nil{
                    
                }else{
                    
                }*/
                MedicineAddEditContent(medicineName: medicine.medicineName)
                
                
            }
        }
        .navigationTitle("medicine_add")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct MedicineAddEditContent: View {
    @State var saveEditClickFirst: Bool = false
    @State var medicineName: String
    @State var isMedicineValid: Bool = false
    @State var isLoading: Bool = false
    
    let apiService: ApiServiceProtocol = ApiService()
    let apiText = ApiConstantText()
    var validation = Validation()
    
    var body: some View{
        VStack(alignment: .center, spacing: 16){
            VStack(alignment: .leading, spacing: 4){
                Text("medicine_name")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .padding(.leading, 10)
                
                HStack {
                    Image("medicine")
                        .resizable()
                        .renderingMode(.template)
                        .scaledToFit()
                        .foregroundColor(.secondary)
                        .frame(width: 24, height: 24)
                        .padding(.top, 1)
                    
                    TextField("medicine_name", text: $medicineName)
                        .foregroundColor(.primary)
                        .keyboardType(.default)
                        .onReceive(Just(medicineName), perform: { newValue in
                            isMedicineValid = validation.validateString(string: medicineName, maxLength: 50)
                        })
                }.padding()
                .background(Capsule().fill(Color.textFieldBackground))
                
                if !isMedicineValid && saveEditClickFirst{
                    Text("enter_valid_medicine_name")
                        .font(.caption)
                        .foregroundColor(.red)
                        .padding(.leading, 10)
                }
            }
           
            LoadingButton(action: {
                saveEditClickFirst = true
                isLoading = true
            }, isLoading: $isLoading) {
                Text("medicine_add")
                    .frame(maxWidth: .infinity)
                    .frame(height: 15.0).foregroundColor(Color.white)
            }
            
            
        }.padding(.all, 16)
    }
}


struct MedicineAddEdit_Previews: PreviewProvider {
    static var previews: some View {
        MedicineAddEdit()
        //MedicineAddEdit(medicine: Medicine(id: "asf", medicineName: "Aspirin", onComingUsageTime: Date()))
            //.preferredColorScheme(/*@START_MENU_TOKEN@*/.dark/*@END_MENU_TOKEN@*/)
    }
}
