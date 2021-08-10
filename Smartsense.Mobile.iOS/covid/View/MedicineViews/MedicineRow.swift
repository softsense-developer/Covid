//
//  MedicineRow.swift
//  covid
//
//  Created by SmartSense on 10.08.2021.
//

import SwiftUI

struct MedicineRow: View {
    var medicine: Medicine
    var dateFormatter = DateFormat()
    
    var body: some View {
        VStack {
            ZStack(alignment: .leading) {
                /* For look like a cardview */
                RoundedRectangle(cornerRadius: 16, style: .continuous)
                    .fill(Color.componentColor)
                    .shadow(radius: 0.5)
                    .padding(.all, 1)
                
                HStack(alignment: .center, spacing: 16){
                    
                    //Medicine icon
                    Image("medicine_fill")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 30, height: 30)
                        .padding(.top, 1)
                    
                    //Medicine name and time view start
                    VStack(alignment: .leading, spacing: 4){
                        
                        Text(String(medicine.medicineName))
                            .font(.body)
                        
                        Text(NSLocalizedString("medicine_time", comment: "") + " " + dateFormatter.dateHourMinute(date: medicine.onComingUsageTime))
                            .font(.caption)
                            .foregroundColor(.colorPrimary)
                            .padding(.all, 8)
                            .background(
                                RoundedRectangle(cornerRadius: 16)
                                    .fill(Color.colorPrimary.opacity(0.2))
                                    .frame(height: 25)
                            )
                    }
                    //Medicine name and time view end
                    
                }.padding(.all, 16)
            }
            .padding([.top, .bottom], 2)
        }
    }
}


struct MedicineRow2: View {
    var medicine: Medicine
    var dateFormatter = DateFormat()
    
    
    var body: some View {
        VStack(alignment: .leading, spacing: 4){
            HStack(alignment: .center, spacing: 16){
                
                //Medicine icon
                Image("medicine_fill")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 30, height: 30)
                    .padding(.top, 1)
                
                //Medicine name and time view start
                VStack(alignment: .leading, spacing: 4){
                    Text(String(medicine.medicineName))
                        .font(.body)
                    
                    Text(NSLocalizedString("medicine_time", comment: "") + " " + dateFormatter.dateHourMinute(date: medicine.onComingUsageTime))
                        .font(.caption)
                        .foregroundColor(.colorPrimary)
                        .padding(.all, 8)
                        .background(
                            RoundedRectangle(cornerRadius: 16)
                                .fill(Color.colorPrimary.opacity(0.2))
                                .frame(height: 25)
                        )
                    
                }
                //Medicine name and time view end
                
                //Spacer for alignment .leading
                Spacer()
            }.padding(.horizontal, 16)
            .padding([.top, .bottom], 8)
            
            Divider().padding(.leading, 64)
        }
        
    }
}

struct MedicineRow_Previews: PreviewProvider {
    static var previews: some View {
        MedicineRow(medicine: Medicine(id: "asf", medicineName: "Aspirin", onComingUsageTime: Date()))
    }
}
