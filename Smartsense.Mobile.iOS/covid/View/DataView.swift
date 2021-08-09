//
//  DataView.swift
//  covid
//
//  Created by SmartSense on 16.06.2021.
//

import SwiftUI

struct DataView: View {
    @State private var showingSetting = false
    
    var body: some View {
        NavigationView{
            ScrollView(.vertical) {
                VStack{
                    DataViewContent()
                }.padding(.all, 8)
            }.navigationTitle("tab_data")
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

struct DataViewContent: View {
    
    init() {
        
        //this changes the "thumb" that selects between items
        UISegmentedControl.appearance().selectedSegmentTintColor = UIColor(.componentColor)
        
        //and this changes the color for the whole "bar" background
        UISegmentedControl.appearance().backgroundColor = UIColor(.textFieldBackground)
        
        //this will change the font size
        UISegmentedControl.appearance().setTitleTextAttributes([.font : UIFont.preferredFont(forTextStyle: .largeTitle)], for: .normal)
        
        //these lines change the text color for various states
        UISegmentedControl.appearance().setTitleTextAttributes([.foregroundColor : UIColor(.colorPrimary)], for: .selected)
        UISegmentedControl.appearance().setTitleTextAttributes([.foregroundColor : UIColor(.secondary)], for: .normal)
    }
    
    @State private var selectedFilter = NSLocalizedString("all", comment: "")
    let dataFilter = [NSLocalizedString("all", comment: ""),
                      NSLocalizedString("temperature", comment: ""),
                      NSLocalizedString("heart", comment: ""),
                      NSLocalizedString("spo2", comment: "")]
    
    @State private var selectedFilterInt = -1
    
    let vitals = [Vital(random: true),
                  Vital(random: true),
                  Vital(random: true),
                  Vital(random: true),
                  Vital(random: true),
                  Vital(random: true),
                  Vital(random: true),
                  Vital(random: true),
                  Vital(random: true),
                  Vital(random: true)]
    
    
 
    
    var body: some View {
        VStack{
            Picker(selectedFilter, selection: $selectedFilter) {
                ForEach(dataFilter, id: \.self) {
                    Text($0)
                }
            }
            .pickerStyle(SegmentedPickerStyle())
         
            
            ForEach(filterData, id: \.self) { vital in
                VitalItem2(vital: vital)
            }
            
        }
    }
    
    var filterData: [Vital] {
            if selectedFilter == NSLocalizedString("temperature", comment: ""){
                return vitals.filter { $0.type == 2 }
            }else if selectedFilter == NSLocalizedString("heart", comment: ""){
                return vitals.filter { $0.type == 1 }
            }else if selectedFilter == NSLocalizedString("spo2", comment: ""){
                return vitals.filter { $0.type == 0 }
            }else{
                return vitals
            }
        }
}


struct DataView_Previews: PreviewProvider {
    static var previews: some View {
        DataView()
        //.preferredColorScheme(.dark)
    }
}
