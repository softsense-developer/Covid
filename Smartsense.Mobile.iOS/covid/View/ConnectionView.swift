//
//  ConnectionView.swift
//  covid
//
//  Created by SmartSense on 16.06.2021.
//

import SwiftUI

struct ConnectionView: View {
    
    @State private var showingSetting = false
    @State var isSmartSenseWatchConnected: Bool = true
    @State var isSmartSenseWatchPaired: Bool = true
    @State var connectedDeviceName: String = "Smartsense Band"
    
    var body: some View {
        NavigationView{
            ScrollView(.vertical) {
                VStack {
                    Button(action: {
                        
                    }, label: {
                        ZStack(alignment: .bottom) {
                            /* For look like a cardview */
                            RoundedRectangle(cornerRadius: 10, style: .continuous)
                                .fill(Color.componentColor)
                                .shadow(radius: 0.5)
                                .padding(.all, 1)
                            
                            VStack(alignment: .center, spacing: 16){
                                HStack(alignment: .center, spacing: 16){
                                  
                                    Text("smartsense_band")
                                        .font(.body)
                                        .foregroundColor(.primary)
                                    
                                    Spacer()
                                    
                                    Image("smartsense_watch")
                                        .resizable()
                                        .scaledToFit()
                                        .scaledToFit()
                                        .frame(width: 40, height: 40)
                                        .padding(.top, 1)
                                    
                                   
                                }
                                
                                if isSmartSenseWatchConnected{
                                    if isSmartSenseWatchPaired{
                                        let connectionText = connectedDeviceName + " " + NSLocalizedString("connected", comment: "")
                                        Text(connectionText)
                                            .font(.callout)
                                            .foregroundColor(.primary)
                                    }else{
                                        let connectionText = connectedDeviceName + " " + NSLocalizedString("not_connected", comment: "")
                                        Text(connectionText)
                                            .font(.callout)
                                            .foregroundColor(.primary)
                                    }
                                    
                                    HStack(alignment: .top, spacing: 8){
                                        
                                        Button(action: {
                                            
                                        }, label: {
                                            Text("disconnect_pairing")
                                                .padding(.all, 10)
                                                .frame(maxWidth: .infinity)
                                                .foregroundColor(.white)
                                                .background(Color.colorPrimary)
                                                .cornerRadius(8)
                                        })
                                        
                                        Button(action: {
                                            
                                        }, label: {
                                            Text("disconnect")
                                                .padding(.all, 10)
                                                .frame(maxWidth: .infinity)
                                                .foregroundColor(.white)
                                                .background(Color.colorPrimary)
                                                .cornerRadius(8)
                                            
                                        })
                                    }
                                  
                                }
                            }.padding(.all, 16)
                           
                            
                        }
                        .padding([.trailing, .leading], 16)
                        .padding(.bottom, 4)
                        .padding(.top, 16)
                        
                    })
                }
            }.navigationTitle("tab_connection")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar{
                ToolbarItem(placement: .navigationBarTrailing) {
                    /*NavigationLink(
                        destination: SettingsView(),
                        label: {
                            Label("Settings", systemImage: "gearshape.fill")
                                .foregroundColor(Color.colorPrimary)
                        })*/
                    Button(action: { showingSetting.toggle() }) {
                        Image(systemName: "gearshape.fill")
                            .accessibilityLabel("Settings")
                    }
                    
                }
                
            }.sheet(isPresented: $showingSetting) {
                NavigationView{
                    SettingsView()
                }
            }
        }
        
       
    }
}

struct ConnectionView_Previews: PreviewProvider {
    static var previews: some View {
        ConnectionView()
        /*ConnectionView()
            .preferredColorScheme(.dark)*/
    }
}
