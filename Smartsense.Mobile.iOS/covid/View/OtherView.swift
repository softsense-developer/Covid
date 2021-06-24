//
//  OtherView.swift
//  covid
//
//  Created by SmartSense on 23.06.2021.
//

import SwiftUI

struct OtherView: View {
    var body: some View {
        NavigationView{
            ScrollView(.vertical) {
                VStack{
                    OtherNew()
                }
            }.navigationTitle("tab_other")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar{
                ToolbarItem(placement: .navigationBarTrailing) {
                    NavigationLink(
                        destination: SettingsView(),
                        label: {
                            Label("Settings", systemImage: "gearshape.fill")
                                .foregroundColor(Color.colorPrimary)
                        })
                }
            }
            //.navigationBarColor(UIColor(Color.navigationBarColor))
            
        }
        
    }
}

struct OtherNew: View {
    let circleSize: CGFloat = 60
    let rectSize: CGFloat = 45
    let rectIconSize: CGFloat = 15
    let iconSize: CGFloat = 25
    
    var body: some View{
        GeometryReader { geo in
            VStack(alignment: .leading, spacing: 8){
                
                
                //Companion view
                NavigationLink(
                    destination: CompanionView(),
                    label: {
                        
                        //Companion view
                        HStack(alignment: .center, spacing: 8){
                            ZStack{
                                Circle()
                                    .frame(width: circleSize, height: circleSize)
                                    .foregroundColor(.orange500)
                                    .opacity(0.1)
                                
                                Image(systemName: "person.fill.badge.plus")
                                    .font(.system(size: iconSize))
                                    .foregroundColor(.orange500)
                            }
                            
                            Text("companion")
                                .foregroundColor(.primary)
                                .font(.title3)
                                .padding(.leading, 16)
                            
                            Spacer()
                            
                            ZStack{
                                RoundedRectangle(cornerRadius: 20)
                                    .fill(Color.greyColor)
                                    .frame(width: rectSize, height: rectSize)
                                    .opacity(0.2)
                                
                                Image(systemName: "chevron.right")
                                    .font(.system(size: rectIconSize))
                                    .foregroundColor(.greyColor)
                            }
                        }.padding(.bottom, 8)
                        
                    })
                
                //Requests view
                NavigationLink(
                    destination: RequestView(),
                    label: {
                        
                        //Requests view
                        HStack(alignment: .center, spacing: 8){
                            ZStack{
                                Circle()
                                    .frame(width: circleSize, height: circleSize)
                                    .foregroundColor(.blue500)
                                    .opacity(0.1)
                                
                                Image(systemName: "bell.fill")
                                    .font(.system(size: iconSize))
                                    .foregroundColor(.blue500)
                            }
                            
                            Text("request")
                                .foregroundColor(.primary)
                                .font(.title3)
                                .padding(.leading, 16)
                            
                            Spacer()
                            
                            ZStack{
                                RoundedRectangle(cornerRadius: 20)
                                    .fill(Color.greyColor)
                                    .frame(width: rectSize, height: rectSize)
                                    .opacity(0.2)
                                
                                Image(systemName: "chevron.right")
                                    .font(.system(size: rectIconSize))
                                    .foregroundColor(.greyColor)
                            }
                        }.padding(.bottom, 8)
                        
                    })
                
                
                
                //Select doktor view
                NavigationLink(
                    destination: DoctorSelectView(),
                    label: {
                        
                        //Select doktor view
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
                            
                            Text("doctor")
                                .foregroundColor(.primary)
                                .font(.title3)
                                .padding(.leading, 16)
                            
                            Spacer()
                            
                            ZStack{
                                RoundedRectangle(cornerRadius: 20)
                                    .fill(Color.greyColor)
                                    .frame(width: rectSize, height: rectSize)
                                    .opacity(0.2)
                                
                                Image(systemName: "chevron.right")
                                    .font(.system(size: rectIconSize))
                                    .foregroundColor(.greyColor)
                            }
                        }.padding(.bottom, 8)
                        
                    })
                
                
            }.padding(.leading, 32)
            .padding(.trailing, 16)
            .padding([.top, .bottom], 16)
            .frame(width: geo.size.width, height: nil, alignment: .leading)
            
        }
    }
}


struct OtherView_Previews: PreviewProvider {
    static var previews: some View {
        OtherView()
         //.preferredColorScheme(.dark)
    }
}
