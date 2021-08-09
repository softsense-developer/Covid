//
//  HomeView.swift
//  covid
//
//  Created by SmartSense on 16.06.2021.
//

import SwiftUI


struct HomeView: View {
    @State private var showingSetting = false
    
    var body: some View {
        NavigationView{
            ScrollView(.vertical) {
                VStack{
                    HomeQuarantineView()
                    HomeHeartView()
                    HomeNoConnectionView()
                    HomeConnectedView()
                    //HomeMedicineReminderView()
                    HomeAlertView()
                    HomeVitalView()
                    
                    
                }
            }.navigationTitle("tab_home")
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
        }.navigationViewStyle(StackNavigationViewStyle())
        
        
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
                    
                    Image("bluetooth")
                        .resizable()
                        .renderingMode(.template)
                        .foregroundColor(Color.colorPrimary)
                        .scaledToFit()
                        .frame(width: 25, height: 25)
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


/* If have a vital alert show this view */
struct HomeAlertView: View {
    @State var alertVitalType = Constant.SpO2
    var alertImageName = "spo2"
    var alertData = "%90"
    var alertDate = "22/06 12:51"
    var quarantineType = Constant.QuarantineAtHome
    @State var quarantineWarningText: String = ""
    
    var body: some View {
        VStack {
            ZStack(alignment: .bottom) {
                /* For look like a cardview */
                RoundedRectangle(cornerRadius: 10, style: .continuous)
                    .fill(Color.componentColor)
                    .shadow(radius: 0.5)
                    .padding(.all, 1)
                
                
                let alertText = NSLocalizedString("warning_spo2", comment: "")
                    + " " + NSLocalizedString("quarantine_home_warning", comment: "")
                
                /*if alertVitalType == Constant.SpO2{
                 if quarantineType == Constant.QuarantineInHospital{
                 quarantineWarningText = NSLocalizedString("quarantine_hospital_warning", comment: "")
                 }else{
                 quarantineWarningText = NSLocalizedString("quarantine_home_warning", comment: "")
                 }
                 
                 var alertText = NSLocalizedString("warning_spo2", comment: "")
                 + " " + quarantineWarningText
                 
                 }else if alertVitalType == Constant.HeartRate{
                 if quarantineType == Constant.QuarantineInHospital{
                 quarantineWarningText = NSLocalizedString("quarantine_hospital_warning", comment: "")
                 }else{
                 quarantineWarningText = NSLocalizedString("quarantine_home_warning", comment: "")
                 }
                 
                 var alertText = NSLocalizedString("warning_heartrate_low", comment: "")
                 + " " + quarantineWarningText
                 }else{
                 if quarantineType == Constant.QuarantineInHospital{
                 quarantineWarningText = NSLocalizedString("quarantine_hospital_warning", comment: "")
                 }else{
                 quarantineWarningText = NSLocalizedString("quarantine_home_warning", comment: "")
                 }
                 
                 var alertText = NSLocalizedString("warning_temp", comment: "")
                 + " " + quarantineWarningText
                 }*/
                
                
                
                
                VStack(alignment: .leading, spacing: 16){
                    
                    HStack(alignment: .center) {
                        Image(alertImageName)
                            .resizable()
                            .renderingMode(.template)
                            .scaledToFit()
                            .foregroundColor(Color.SpO2Color)
                            .frame(width: 20, height: 20)
                            .padding(.top, 1)
                        
                        if alertVitalType == Constant.SpO2{
                            Text("warning_spo2_title")
                                .font(.body)
                                .foregroundColor(.primary)
                        }else if alertVitalType == Constant.HeartRate{
                            Text("warning_heartrate_title")
                                .font(.body)
                                .foregroundColor(.primary)
                        }else{
                            Text("warning_temp_title")
                                .font(.body)
                                .foregroundColor(.primary)
                        }
                        
                        Spacer()
                        
                        Text(alertData)
                            .font(.subheadline)
                            .foregroundColor(.red)
                        
                        Spacer()
                        
                        Text(alertDate)
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                    
                    Text(alertText)
                        .font(.subheadline)
                        .foregroundColor(.primary)
                    
                    Button(action: {
                        print("ok clicked")
                    }, label: {
                        Text("ok")
                            .frame(maxWidth: .infinity)
                            .frame(height: 40)
                    }).accentColor(.colorPrimary)
                    
                }.padding([.trailing, .leading], 16)
                .padding([.top, .bottom], 16)
                
            }
            .padding([.trailing, .leading], 16)
            .padding([.top, .bottom], 4)
            
            
        }
        
    }
}

/* If user have a medicine reminder show this view */
struct HomeMedicineReminderView: View {
    
    var body: some View {
        VStack {
            ZStack(alignment: .bottom) {
                
                /* For look like a cardview */
                RoundedRectangle(cornerRadius: 10, style: .continuous)
                    .fill(Color.componentColor)
                    .shadow(radius: 0.5)
                    .padding(.all, 1)
                
                VStack(alignment: .leading, spacing: 16){
                    
                    HStack(alignment: .center) {
                        Image("medicine")
                            .resizable()
                            .renderingMode(.template)
                            .scaledToFit()
                            .foregroundColor(Color.colorPrimary)
                            .frame(width: 20, height: 20)
                            .padding(.top, 1)
                        
                        Text("medicine_reminder_notify")
                            .font(.body)
                            .foregroundColor(.primary)
                        
                        Spacer()
                        
                        Text("10/04 10:56")
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                    
                    Button(action: {
                        print("ok clicked")
                    }, label: {
                        Text("ok")
                            .frame(maxWidth: .infinity)
                            .frame(height: 40)
                    }).accentColor(.colorPrimary)
                    
                }.padding([.trailing, .leading], 16)
                .padding([.top, .bottom], 16)
                
            }
            .padding([.trailing, .leading], 16)
            .padding([.top, .bottom], 4)
            
            
        }
        
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
                    
                    HStack(alignment: .center, spacing: 11){
                        
                        Image("thermometer")
                            .resizable()
                            .renderingMode(.template)
                            .scaledToFit()
                            .foregroundColor(Color.temperatureColor)
                            .frame(width: 30, height: 30)
                            .padding(.top, 1)
                        
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
                        
                        Image("heart")
                            .resizable()
                            .renderingMode(.template)
                            .scaledToFit()
                            .foregroundColor(Color.heartColor)
                            .frame(width: 25, height: 25)
                            .padding(.top, 1)
                        
                        
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
                        
                        Image("spo2")
                            .resizable()
                            .renderingMode(.template)
                            .foregroundColor(Color.SpO2Color)
                            .scaledToFit()
                            .frame(width: 25, height: 25)
                            .padding(.top, 1)
                        
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
            
        }.padding(.bottom, 8)
    }
}


/* If temp, heart rate, spo2 values danger show view */




struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        //HomeView()
        HomeView()
        //.preferredColorScheme(.dark)
    }
}
