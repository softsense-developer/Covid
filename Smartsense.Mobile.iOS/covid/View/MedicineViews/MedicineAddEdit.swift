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
                MedicineAddEditContent()
            }
            
        }
        .navigationTitle("medicine_add")
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct MedicineEdit: View {
    var medicine: Medicine
    @State var medicineName: String = ""
    
    var body: some View {
        ScrollView(.vertical) {
            VStack{
                /*if medicine != nil{
                 
                 }else{
                 
                 }*/
                MedicineAddEditContent(medicineName: medicine.medicineName)
                
                
            }
        }
        .navigationTitle("medicine_edit")
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
    
    var body: some View{
        VStack(alignment: .center, spacing: 8){
            //Medicine name view start
            VStack(alignment: .leading){
                Text("medicine_name")
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
                    
                    TextField("medicine_name", text: $medicineName)
                        .textFieldStyle(CapsuleTextFieldStyle())
                        .onReceive(Just(medicineName), perform: { newValue in
                            isMedicineValid = validation.validateString(string: medicineName, maxLength: 50)
                        })
                }
                
                if !isMedicineValid && saveEditClickFirst{
                    Text("enter_valid_medicine_name")
                        .font(.caption)
                        .foregroundColor(.red)
                        .padding(.leading, 75)
                }
                
            }
            //Medicine name view end
            
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


