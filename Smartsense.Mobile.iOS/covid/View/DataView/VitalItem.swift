//
//  VitalItem.swift
//  covid
//
//  Created by SmartSense on 6.08.2021.
//

import SwiftUI

struct VitalItem: View {
    var vital: Vital
    var numberFormatter: NumberFormatter = Formatters.floatFormatter
    var dateFormatter = DateFormat()
    
    
    var body: some View {
        VStack {
            ZStack(alignment: .leading) {
                /* For look like a cardview */
                RoundedRectangle(cornerRadius: 10, style: .continuous)
                    .fill(Color.componentColor)
                    .shadow(radius: 0.5)
                    .padding(.all, 1)
                
                HStack(alignment: .center, spacing: 16){
                    
                    if vital.type == Constant.Temperature{
                        Image("thermometer")
                            .resizable()
                            .renderingMode(.template)
                            .scaledToFit()
                            .foregroundColor(Color.temperatureColor)
                            .frame(width: 30, height: 30)
                            .padding(.top, 1)
                    }else if vital.type == Constant.HeartRate{
                        Image("heart")
                            .resizable()
                            .renderingMode(.template)
                            .scaledToFit()
                            .foregroundColor(Color.heartColor)
                            .frame(width: 30, height: 30)
                            .padding(.top, 1)
                    }else{
                        Image("spo2")
                            .resizable()
                            .renderingMode(.template)
                            .scaledToFit()
                            .foregroundColor(Color.SpO2Color)
                            .frame(width: 30, height: 30)
                            .padding(.top, 1)
                    }
                    
                    VStack(alignment: .leading, spacing: 4){
                        HStack(alignment: .top, spacing: 4){
                            
                            if vital.type == Constant.Temperature && vital.type == Constant.HeartRate{
                                Text(NSNumber(value: vital.data), formatter: numberFormatter)
                                    .font(.title3)
                                    .foregroundColor(.primary)
                            }else{
                                Text(String(vital.data))
                                    .font(.title3)
                                    .foregroundColor(.primary)
                            }
                            
                            
                            if vital.type == Constant.Temperature{
                                Text("temp_unit")
                                    .font(.subheadline)
                                    .foregroundColor(.secondary)
                            }else if vital.type == Constant.HeartRate{
                                Text("heart_unit")
                                    .font(.subheadline)
                                    .foregroundColor(.secondary)
                            }else{
                                Text("spo2_unit")
                                    .font(.subheadline)
                                    .foregroundColor(.secondary)
                            }
                            
                            
                        }
                        
                        
                        Text(dateFormatter.dateMonthToMinute(date: vital.date))
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                        
                    }
                }.padding(.all, 16)
            }
            .padding([.top, .bottom], 2)
        }
    }
}


struct VitalItem2: View {
    var vital: Vital
    var numberFormatter: NumberFormatter = Formatters.floatFormatter
    var dateFormatter = DateFormat()
    
    
    var body: some View {
        VStack(alignment: .leading, spacing: 4){
            HStack(alignment: .center, spacing: 16){
                if vital.type == Constant.Temperature{
                    Image("thermometer")
                        .resizable()
                        .renderingMode(.template)
                        .scaledToFit()
                        .foregroundColor(Color.temperatureColor)
                        .frame(width: 30, height: 30)
                        .padding(.top, 1)
                }else if vital.type == Constant.HeartRate{
                    Image("heart")
                        .resizable()
                        .renderingMode(.template)
                        .scaledToFit()
                        .foregroundColor(Color.heartColor)
                        .frame(width: 30, height: 30)
                        .padding(.top, 1)
                }else{
                    Image("spo2")
                        .resizable()
                        .renderingMode(.template)
                        .scaledToFit()
                        .foregroundColor(Color.SpO2Color)
                        .frame(width: 30, height: 30)
                        .padding(.top, 1)
                }
                
                VStack(alignment: .leading, spacing: 4){
                    HStack(alignment: .top, spacing: 4){
                        
                        if vital.type == Constant.Temperature && vital.type == Constant.HeartRate{
                            Text(NSNumber(value: vital.data), formatter: numberFormatter)
                                .font(.body)
                                .foregroundColor(.primary)
                            
                        }else{
                            Text(String(vital.data))
                                .font(.body)
                                .foregroundColor(.primary)
                        }
                        
                        
                        if vital.type == Constant.Temperature{
                            Text("temp_unit")
                                .font(.footnote)
                                .foregroundColor(.secondary)
                        }else if vital.type == Constant.HeartRate{
                            Text("heart_unit")
                                .font(.footnote)
                                .foregroundColor(.secondary)
                        }else{
                            Text("spo2_unit")
                                .font(.footnote)
                                .foregroundColor(.secondary)
                        }
                        
                        
                    }
                    
                    
                    Text(dateFormatter.dateMonthToMinute(date: vital.date))
                        .font(.caption)
                        .foregroundColor(.secondary)
                    
                }
                
                Spacer()
            }.padding(.horizontal, 16)
            .padding([.top, .bottom], 8)
            
            Divider().padding(.leading, 64)
        }
        
    }
}

struct VitalItem_Previews: PreviewProvider {
    static var previews: some View {
        VitalItem2(vital: Vital(random: true))
        
    }
}
