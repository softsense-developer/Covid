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
    @State var medicineName: String = ""
    @State var isMedicineValid: Bool = false
    @State var isLoading: Bool = false
    
    @State var timeDoses = [MedicimeTimeDose]()
    
    let apiService: ApiServiceProtocol = ApiService()
    let apiText = ApiConstantText()
    var validation = Validation()
    
    @State private var currentNumber = 1
    
    var body: some View{
        VStack(alignment: .center, spacing: 8){
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
            
            ForEach(timeDoses, id: \.self) { medicineDose in
                MedicineTimeDoseRow(medicineTime: medicineDose)
                    .foregroundColor(.primary)
            }.onDelete { index in
                self.timeDoses.remove(atOffsets: index)
            }
            
            //If you want to use list you should dont use scrollview
            /*List {
             ForEach(timeDoses, id: \.self) { timeDose in
             MedicineTimeDoseRow(medicineTime: timeDose)
             }
             .onDelete(perform: removeRows)
             }
             .listStyle(PlainListStyle())
             */
            
            
            Button(action: {
                timeDoses.append(MedicimeTimeDose(id: "1", dose: "1.0", time: Date()))
                
            }){
                
                ZStack(alignment: .leading) {
                    Image("medicine")
                        .resizable()
                        .renderingMode(.template)
                        .scaledToFit()
                        .foregroundColor(.white)
                        .frame(width: 30, height: 30)
                        .padding(.leading, 32)
                    
                    Text("medicine_reminder_add")
                        .frame(maxWidth: .infinity)
                        .frame(height: 15.0)
                        .foregroundColor(Color.white)
                }
                
            }.background(
                RoundedRectangle(cornerRadius: 8)
                    .fill(Color.colorPrimary)
                    .frame(height: 50)
            ).padding(.vertical, 24)
            
            LoadingButton(action: {
                saveEditClickFirst = true
                isLoading = true
            }, isLoading: $isLoading) {
                Text("medicine_add")
                    .frame(maxWidth: .infinity)
                    .frame(height: 15.0)
                    .foregroundColor(Color.white)
            }.frame(maxWidth: .infinity)
            
            
        }.padding(.all, 16)
    }
    
    func removeRows(at offsets: IndexSet) {
        timeDoses.remove(atOffsets: offsets)
    }
}


struct MedicineAddEdit_Previews: PreviewProvider {
    static var previews: some View {
        MedicineAddEdit()
        //MedicineAddEdit(medicine: Medicine(id: "asf", medicineName: "Aspirin", onComingUsageTime: Date()))
        //.preferredColorScheme(/*@START_MENU_TOKEN@*/.dark/*@END_MENU_TOKEN@*/)
        //ContentViewa()
        
        
    }
}


struct ContentViewa: View {
    @State var saveEditClickFirst: Bool = false
    @State var medicineName: String = ""
    @State var isMedicineValid: Bool = false
    @State var isLoading: Bool = false
    
    @State private var timeDoses = [MedicimeTimeDose]()
    
    let apiService: ApiServiceProtocol = ApiService()
    let apiText = ApiConstantText()
    var validation = Validation()
    
    var body: some View {
        VStack{
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
            
            VStack {
                List {
                    ForEach(timeDoses, id: \.self) { timeDose in
                        MedicineTimeDoseRow(medicineTime: timeDose)
                    }
                    .onDelete(perform: removeRows)
                }
                .listStyle(PlainListStyle())
                
                Button("Add Number") {
                    timeDoses.append(MedicimeTimeDose(id: "1", dose: "1.0", time: Date()))
                }
            }
        }
        .navigationBarItems(leading: EditButton())
    }
    
    func removeRows(at offsets: IndexSet) {
        timeDoses.remove(atOffsets: offsets)
    }
}


