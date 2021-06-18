//
//  HomeView.swift
//  covid
//
//  Created by SmartSense on 16.06.2021.
//

import SwiftUI

struct HomeView: View {
    var body: some View {
        NavigationView{
            ScrollView(.vertical) {
                VStack{
                    HomeQuarantineView()
                    HomeHeartView()
                    HomeNoConnectionView()
                    HomeConnectedView()
                    HomeVitalView()
                    
                   
                }
            }.navigationTitle("tab_home")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar{
                Button(action: {
                    
                }) {
                    Image(systemName: "gearshape.fill")
                        .accessibilityLabel("Settings")
                        .foregroundColor(Color.colorPrimary)
                }
            }
        }
        
        
    }
}

/* If user not selected a quarantine location show this view */
struct HomeQuarantineView: View {
    
    @State private var showConnectionAlert = false
    
    var body: some View {
        
        VStack {
            Button(action: {
                
            }, label: {
                ZStack {
                    /* For look like a cardview */
                    RoundedRectangle(cornerRadius: 10, style: .continuous)
                        .fill(Color.componentColor)
                        .shadow(radius: 0.5)
                        .padding(.all, 1)
                    
                    HStack(alignment: .center){
                        Image(systemName: "house.fill")
                            .font(.system(size: 25))
                            .foregroundColor(.colorPrimary)
                            .padding(.trailing, 16)
                        
                        VStack(alignment: .leading){
                            Text("home_title")
                                .font(.body)
                                .foregroundColor(.primary)
                            
                            Text("home_hint")
                                .font(.footnote)
                                .foregroundColor(.secondary)
                                .padding(.top, 1)
                        }
                    }
                    .padding([.trailing, .top, .bottom], 16)
                }
                .padding([.trailing, .leading], 16)
                .padding(.top, 16)
                .padding(.bottom, 4)
            })
        }
        
    }
}

/* Home heart view shows connection status */
struct HomeHeartView: View {
    
    var body: some View {
        
        VStack {
            Button(action: {
                
            }, label: {
                ZStack {
                    /* For look like a cardview */
                    RoundedRectangle(cornerRadius: 10, style: .continuous)
                        .fill(Color.componentColor)
                        .shadow(radius: 0.5)
                        .padding(.all, 1)
                    
                    HStack(alignment: .center){
                        Image(systemName: "heart.fill")
                            .font(.system(size: 120))
                            .foregroundColor(.passiveColor)
                        
                    }
                    .padding(.all, 16)
                }
                .padding([.trailing, .leading], 16)
                .padding([.top, .bottom], 4)
            })
        }
        
    }
}

/* If bluetooth connection not established show this view */
struct HomeNoConnectionView: View {
    
    var body: some View {
        VStack {
            Button(action: {
                
            }, label: {
                ZStack(alignment: .bottom) {
                    /* For look like a cardview */
                    RoundedRectangle(cornerRadius: 10, style: .continuous)
                        .fill(Color.componentColor)
                        .shadow(radius: 0.5)
                        .padding(.all, 1)
                    VStack(alignment: .center){
                        Text("bluetooth_connection_status")
                            .font(.body)
                            .foregroundColor(.primary)
                        
                        Text("bluetooth_no_connection_click")
                            .font(.footnote)
                            .foregroundColor(.secondary)
                            .padding(.top, 1)
                    }.padding(.all, 16)
                    
                    /* For cardview bottom line */
                    Rectangle()
                        .fill(Color.passiveColor)
                        .frame(minWidth: 0, maxWidth: .infinity, maxHeight: 5, alignment: .bottomLeading)
                        .clipped()
                    
                }
                .clipShape(RoundedRectangle(cornerRadius: 12)) /* For cardview bottom line */
                .padding([.trailing, .leading], 16)
                .padding([.top, .bottom], 4)
                
            })
        }
        
    }
}

/* If connected a bluetooth this view */
struct HomeConnectedView: View {
    
    var body: some View {
        
        HStack{
            
            /* Connected Bluetooth View */
            ZStack(alignment: .bottom) {
                /* For look like a cardview */
                RoundedRectangle(cornerRadius: 10, style: .continuous)
                    .fill(Color.componentColor)
                    .shadow(radius: 0.5)
                    .padding(.all, 1)
                
                
                VStack(alignment: .center){
                    Text("bluetooth")
                        .font(.body)
                        .foregroundColor(.primary)
                    
                    Image(systemName: "heart.fill")
                        .font(.system(size: 25))
                        .foregroundColor(.colorPrimary)
                        .padding(.top, 1)
                }.padding(.all, 16)
                
                /* For cardview bottom line */
                Rectangle()
                    .fill(Color.passiveColor)
                    .frame(minWidth: 0, maxWidth: .infinity, maxHeight: 5, alignment: .bottomLeading)
                    .clipped()
            }
            .clipShape(RoundedRectangle(cornerRadius: 12)) /* For cardview bottom line */
            .padding([.top, .bottom], 4)
            
            
            /* Connected Wearing View */
            ZStack(alignment: .bottom) {
                /* For look like a cardview */
                RoundedRectangle(cornerRadius: 10, style: .continuous)
                    .fill(Color.componentColor)
                    .shadow(radius: 0.5)
                    .padding(.all, 1)
                
                
                VStack(alignment: .center){
                    Text("wearing")
                        .font(.body)
                        .foregroundColor(.primary)
                    
                    Image(systemName: "applewatch")
                        .font(.system(size: 25))
                        .foregroundColor(.colorPrimary)
                        .padding(.top, 1)
                }.padding(.all, 16)
                
                /* For cardview bottom line */
                Rectangle()
                    .fill(Color.passiveColor)
                    .frame(minWidth: 0, maxWidth: .infinity, maxHeight: 5, alignment: .bottomLeading)
                    .clipped()
            }
            .clipShape(RoundedRectangle(cornerRadius: 12)) /* For cardview bottom line */
            .padding([.top, .bottom], 4)
            
            
            
            /* Connected Battery View */
            ZStack(alignment: .bottom) {
                /* For look like a cardview */
                RoundedRectangle(cornerRadius: 10, style: .continuous)
                    .fill(Color.componentColor)
                    .shadow(radius: 0.5)
                    .padding(.all, 1)
                
                
                VStack(alignment: .center){
                    Text("battery")
                        .font(.body)
                        .foregroundColor(.primary)
                    
                    HStack{
                        Image(systemName: "battery.100")
                            .font(.system(size: 25))
                            .foregroundColor(.colorPrimary)
                            
                        Text("%100")
                            .font(.caption)
                            .foregroundColor(.primary)
                        
                    }.padding(.top, 9)
                    
                }.padding([.leading, .trailing], 8)
                .padding([.top, .bottom], 16)
                
                /* For cardview bottom line */
                Rectangle()
                    .fill(Color.passiveColor)
                    .frame(minWidth: 0, maxWidth: .infinity, maxHeight: 5, alignment: .bottomLeading)
                    .clipped()
            
            }
            .clipShape(RoundedRectangle(cornerRadius: 12)) /* For cardview bottom line */
            .padding([.top, .bottom], 4)
            
        }.padding([.leading, .trailing], 16)
        
    }
}


/* Temp, Heart rate, SpO2 last data showing in this view */
struct HomeVitalView: View {
    
    var body: some View {
        VStack {
            
            //Last temperature value and click see chart
            Button(action: {
                
            }, label: {
                ZStack(alignment: .leading) {
                    
                    /* For look like a cardview */
                    RoundedRectangle(cornerRadius: 10, style: .continuous)
                        .fill(Color.componentColor)
                        .shadow(radius: 0.5)
                        .padding(.all, 1)
                    
                    HStack(alignment: .center, spacing: 16){
                        Image(systemName: "flame.fill")
                            .font(.system(size: 25))
                            .foregroundColor(.temperatureColor)
                        
                        VStack(alignment: .leading, spacing: 4){
                            
                            Text("temperature")
                                .font(.body)
                                .foregroundColor(.primary)
                            
                            Text("no_measurement")
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                            
                        }
                    }.padding(.all, 16)
                }
                .padding([.trailing, .leading], 16)
                .padding([.top, .bottom], 4)
                
            })
            
            
            //Last heart rate value and click see chart
            Button(action: {
                
            }, label: {
                ZStack(alignment: .leading) {
                    
                    /* For look like a cardview */
                    RoundedRectangle(cornerRadius: 10, style: .continuous)
                        .fill(Color.componentColor)
                        .shadow(radius: 0.5)
                        .padding(.all, 1)
                    
                    HStack(alignment: .center, spacing: 16){
                        Image(systemName: "heart.fill")
                            .font(.system(size: 25))
                            .foregroundColor(.heartColor)
                        
                        VStack(alignment: .leading, spacing: 4){
                            
                            Text("heart")
                                .font(.body)
                                .foregroundColor(.primary)
                            
                            Text("no_measurement")
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                            
                        }
                    }.padding(.all, 16)
                }
                .padding([.trailing, .leading], 16)
                .padding([.top, .bottom], 4)
                
            })
            
            
            //Last spO2 value and click see chart
            Button(action: {
                
            }, label: {
                ZStack(alignment: .leading) {
                    
                    /* For look like a cardview */
                    RoundedRectangle(cornerRadius: 10, style: .continuous)
                        .fill(Color.componentColor)
                        .shadow(radius: 0.5)
                        .padding(.all, 1)
                    
                    HStack(alignment: .center, spacing: 16){
                        Image(systemName: "heart.fill")
                            .font(.system(size: 25))
                            .foregroundColor(.SpO2Color)
                        
                        VStack(alignment: .leading, spacing: 4){
                            
                            Text("spo2")
                                .font(.body)
                                .foregroundColor(.primary)
                            
                            Text("no_measurement")
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                            
                        }
                    }.padding(.all, 16)
                }
                .padding([.trailing, .leading], 16)
                .padding([.top, .bottom], 4)
                
            })
            
            /*Button(action: {
                
            }, label: {
                Text("company")
                    .font(.body)
                    .foregroundColor(Color.colorPrimary)
            })*/
             
        }
    }
}


/* If temp, heart rate, spo2 values danger show view */




struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        HomeView()
        HomeView()
            .preferredColorScheme(.dark)
    }
}
