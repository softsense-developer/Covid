//
//  OtherView.swift
//  covid
//
//  Created by SmartSense on 23.06.2021.
//

import SwiftUI

struct SettingsView: View {
    var body: some View {
        
        
        
        ScrollView(.vertical) {
            VStack{
                SettingBodyView()
            }
        }.navigationTitle("settings")
        .navigationBarTitleDisplayMode(.inline)
        
        
        
        
        
    }
}

struct SettingBodyView: View {
    let circleSize: CGFloat = 60
    let rectSize: CGFloat = 45
    let rectIconSize: CGFloat = 15
    let iconSize: CGFloat = 25
    
    var body: some View{
        GeometryReader { geo in
            VStack(alignment: .leading, spacing: 8){
                
                Text("account")
                    .foregroundColor(.primary)
                    .font(.title.weight(Font.Weight.regular))
                    .padding(.bottom, 16)
                
                //Profile view
                NavigationLink(
                    destination: ProfileView(),
                    label: {
                        
                        //Profile view
                        HStack(alignment: .center, spacing: 8){
                            ZStack{
                                Circle()
                                    .frame(width: circleSize, height: circleSize)
                                    .foregroundColor(.greyColor)
                                    .opacity(0.1)
                                
                                Image(systemName: "person.fill")
                                    .font(.system(size: iconSize))
                                    .foregroundColor(.greyColor)
                            }
                            
                            VStack{
                                Text("Gökhan Dağtekin")
                                    .foregroundColor(.primary)
                                    .font(.title3)
                                    .padding(.leading, 16)
                                
                                Text("user_data")
                                    .foregroundColor(.secondary)
                                    .font(.body)
                            }
                            
                            Spacer()
                            
                            ZStack{
                                RoundedRectangle(cornerRadius: 15)
                                    .fill(Color.greyColor)
                                    .frame(width: rectSize, height: rectSize)
                                    .opacity(0.2)
                                
                                Image(systemName: "chevron.right")
                                    .font(.system(size: rectIconSize))
                                    .foregroundColor(.greyColor)
                            }
                        }.padding(.bottom, 8)
                        
                    })
                
                
                //Patient data view
                NavigationLink(
                    destination: PatientProfileView(),
                    label: {
                        
                        //Patient data view
                        HStack(alignment: .center, spacing: 8){
                            ZStack{
                                Circle()
                                    .frame(width: circleSize, height: circleSize)
                                    .foregroundColor(.green500)
                                    .opacity(0.1)
                                
                                Image(systemName: "heart.text.square.fill")
                                    .font(.system(size: iconSize))
                                    .foregroundColor(.green500)
                            }
                            
                            Text("patient_data")
                                .foregroundColor(.primary)
                                .font(.title3)
                                .padding(.leading, 16)
                            
                            Spacer()
                            
                            ZStack{
                                RoundedRectangle(cornerRadius: 15)
                                    .fill(Color.greyColor)
                                    .frame(width: rectSize, height: rectSize)
                                    .opacity(0.2)
                                
                                Image(systemName: "chevron.right")
                                    .font(.system(size: rectIconSize))
                                    .foregroundColor(.greyColor)
                            }
                        }.padding(.bottom, 8)
                        
                    })
                
                
                //Password change view
                NavigationLink(
                    destination: PasswordChangeView(),
                    label: {
                        
                        //Password change view
                        HStack(alignment: .center, spacing: 8){
                            ZStack{
                                Circle()
                                    .frame(width: circleSize, height: circleSize)
                                    .foregroundColor(.blue500)
                                    .opacity(0.1)
                                
                                Image(systemName: "lock.fill")
                                    .font(.system(size: iconSize))
                                    .foregroundColor(.blue500)
                            }
                            
                            Text("password_change")
                                .foregroundColor(.primary)
                                .font(.title3)
                                .padding(.leading, 16)
                            
                            Spacer()
                            
                            ZStack{
                                RoundedRectangle(cornerRadius: 15)
                                    .fill(Color.greyColor)
                                    .frame(width: rectSize, height: rectSize)
                                    .opacity(0.2)
                                
                                Image(systemName: "chevron.right")
                                    .font(.system(size: rectIconSize))
                                    .foregroundColor(.greyColor)
                            }
                        }.padding(.bottom, 8)
                        
                    })
                
                Text("settings")
                    .foregroundColor(.primary)
                    .font(.title.weight(Font.Weight.regular))
                    .padding([.top, .bottom], 16)
                
                //Measurement settings view
                NavigationLink(
                    destination: MeasurementView(),
                    label: {
                        
                        //Measurement settings view
                        HStack(alignment: .center, spacing: 8){
                            ZStack{
                                Circle()
                                    .frame(width: circleSize, height: circleSize)
                                    .foregroundColor(.purple500)
                                    .opacity(0.1)
                                
                                Image(systemName: "timer")
                                    .font(.system(size: iconSize))
                                    .foregroundColor(.purple500)
                            }
                            
                            Text("measurement_settings")
                                .foregroundColor(.primary)
                                .font(.title3)
                                .padding(.leading, 16)
                            
                            Spacer()
                            
                            ZStack{
                                RoundedRectangle(cornerRadius: 15)
                                    .fill(Color.greyColor)
                                    .frame(width: rectSize, height: rectSize)
                                    .opacity(0.2)
                                
                                Image(systemName: "chevron.right")
                                    .font(.system(size: rectIconSize))
                                    .foregroundColor(.greyColor)
                            }
                        }.padding(.bottom, 8)
                        
                    })
                
                //Logout view
                HStack(alignment: .center, spacing: 8){
                    ZStack{
                        Circle()
                            .frame(width: circleSize, height: circleSize)
                            .foregroundColor(.red500)
                            .opacity(0.1)
                        
                        Image(systemName: "escape")
                            .font(.system(size: iconSize))
                            .foregroundColor(.red500)
                    }
                    
                    Text("logout")
                        .foregroundColor(.primary)
                        .font(.title3)
                        .padding(.leading, 16)
                    
                    
                }.padding(.bottom, 8)
                
                
            }.padding(.leading, 32)
            .padding(.trailing, 16)
            .padding([.top, .bottom], 16)
            .frame(width: geo.size.width, height: nil, alignment: .leading)
            
        }
    }
}



struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
        //.preferredColorScheme(.dark)
    }
}
