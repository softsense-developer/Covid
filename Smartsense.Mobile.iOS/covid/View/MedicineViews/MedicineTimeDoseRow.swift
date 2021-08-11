//
//  MedicineTimeDoseRow.swift
//  covid
//
//  Created by SmartSense on 11.08.2021.
//

import SwiftUI

struct MedicineTimeDoseRow: View {
    
    @State var medicineTime: MedicimeTimeDose
    let doses = ["0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0", "4.5", "5.0"]
    
    var body: some View {
        HStack(alignment: .center, spacing: 16) {
            
            //For delete time dose view
            Button(action: {
    
            }){
                Image(systemName: "xmark")
                    .font(.system(size: 20))
                    .foregroundColor(.red500)
                    .padding(.top, 24)
                    .padding(.leading, 16)
            }
           
            //Space between views
            Spacer()
            
            //Time picker and label
            VStack(alignment: .center, spacing: 8) {
                Text("time")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                
                DatePicker("", selection: $medicineTime.time, displayedComponents: .hourAndMinute)
                    .accentColor(.colorPrimary)
                    .labelsHidden()
            }
            
            //Space between views
            Spacer()
            
            //Dose picker and label
            VStack(alignment: .center, spacing: 8) {
                Text("medicine_dose")
                    .font(.subheadline)
                    .padding(.bottom, 4)
                    .foregroundColor(.secondary)
                
                Picker(medicineTime.dose, selection: $medicineTime.dose) {
                    ForEach(doses, id: \.self) {
                        Text($0)
                    }
                }
                .pickerStyle(MenuPickerStyle())
                .accentColor(.colorPrimary)
                .foregroundColor(.colorPrimary)
                .padding(.horizontal, 24)
                .background(RoundedRectangle(cornerRadius: 8)
                                .fill(Color.greyColor)
                                .frame(height: 35)
                                .opacity(0.2))
                
            }.padding(.trailing, 16)
        }
        
    }
}

struct MedicineTimeDoseRow_Previews: PreviewProvider {
    static var previews: some View {
        MedicineTimeDoseRow(medicineTime: MedicimeTimeDose(id: "1", dose: "1.0", time: Date()))
    }
}
