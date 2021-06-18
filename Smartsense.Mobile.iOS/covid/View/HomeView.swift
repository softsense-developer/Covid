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
                }
            }.navigationTitle("tab_home")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar{
                Button(action: {
                    
                }) {
                    Image(systemName: "gearshape.fill")
                        .accessibilityLabel("Settings")
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
                    RoundedRectangle(cornerRadius: 15, style: .continuous)
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
                    RoundedRectangle(cornerRadius: 15, style: .continuous)
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
                    RoundedRectangle(cornerRadius: 15, style: .continuous)
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
                .clipShape(RoundedRectangle(cornerRadius: 17)) /* For cardview bottom line */
                .padding([.trailing, .leading], 16)
                .padding([.top, .bottom], 4)
                
            })
        }
        
    }
}

/* If connected a bluetooth this view */
struct HomeConnectedView: View {
    
    var body: some View {
        VStack {
        Button(action: {
            
        }, label: {
            ZStack(alignment: .bottom) {
                /* For look like a cardview */
                RoundedRectangle(cornerRadius: 15, style: .continuous)
                    .fill(Color.componentColor)
                    .shadow(radius: 0.5)
                    .padding(.all, 1)
                
                
                /* For cardview bottom line */
                Rectangle()
                    .fill(Color.passiveColor)
                    .frame(minWidth: 0, maxWidth: .infinity, maxHeight: 5, alignment: .bottomLeading)
                    .clipped()
                
                
              
                
            }
            .clipShape(RoundedRectangle(cornerRadius: 17)) /* For cardview bottom line */
            .padding([.trailing, .leading], 16)
            .padding([.top, .bottom], 4)
            
        })
            
            /*VStack(alignment: .center){
                Text("bluetooth")
                    .font(.body)
                    .foregroundColor(.primary)
                
                Image("bluetooth")
                    .resizable()
                    .frame(width: 10, height: 10, alignment: /*@START_MENU_TOKEN@*/.center/*@END_MENU_TOKEN@*/)
                
                
            }*/
        }
        
    }
}



struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        HomeView()
        HomeView()
            .preferredColorScheme(.dark)
    }
}
