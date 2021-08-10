//
//  MedicineView.swift
//  covid
//
//  Created by SmartSense on 16.06.2021.
//

import SwiftUI

struct MedicineView: View {
    @State private var showingSetting = false
    @State private var showingAddEdit = false
    @State private var isHaveMedicine = true
    @State var id: Int = 0
    
    var body: some View {
        NavigationView{
            VStack(alignment: .center){
                if !isHaveMedicine{
                    VStack(alignment: .center){
                        Spacer()
                        Text("no_medicine_text")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                            .multilineTextAlignment(.center)
                        Spacer()
                    }
                }else{
                    ScrollView(.vertical) {
                        VStack(alignment: .center){
                            MedicineViewContent(id: id)
                        }
                    }
                }
            }.padding(.all, 8)
            .navigationTitle("tab_medicine")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar{
                ToolbarItem(placement: .navigationBarTrailing) {
                    /*NavigationLink(
                     destination: SettingsView(),
                     label: {
                     Label("Settings", systemImage: "gearshape.fill")
                     .foregroundColor(Color.colorPrimary)
                     })*/
                    
                    HStack{
                        Button(action: {
                            showingSetting.toggle()
                        }) {
                            Image(systemName: "gearshape.fill")
                                .accessibilityLabel("Settings")
                        }
                        
                        //Medicine add button
                        Button(action: {
                            showingAddEdit.toggle()
                        }) {
                            Image(systemName: "plus")
                                .padding(.leading, 16)
                                .accessibilityLabel("Add")
                        }
                    }
                    
                }
                
                
            }.sheet(isPresented: $showingSetting) {
                NavigationView{
                    SettingsView()
                }
            }
            .sheet(isPresented: $showingAddEdit) {
                NavigationView{
                    MedicineAddEdit()
                }
            }
        }.navigationViewStyle(StackNavigationViewStyle())
    }
}

struct MedicineViewContent: View {
    @State private var showingAddEdit = false
    @State var id: Int
    
    let medicines = [Medicine(id: "a1", medicineName: "Aspirin", onComingUsageTime: Date().addingTimeInterval(-60)),
                     Medicine(id: "a2", medicineName: "Karbamazepin", onComingUsageTime: Date().addingTimeInterval(-120)),
                     Medicine(id: "a3", medicineName: "Levatirasetam", onComingUsageTime: Date().addingTimeInterval(-180)),
                     Medicine(id: "a4", medicineName: "Okskarbamazepin", onComingUsageTime: Date().addingTimeInterval(-240)),
                     Medicine(id: "a5", medicineName: "Lamotrijin", onComingUsageTime: Date().addingTimeInterval(-300)),
                     Medicine(id: "a6", medicineName: "Topiramat", onComingUsageTime: Date().addingTimeInterval(-360)),
                     Medicine(id: "a7", medicineName: "Fenitoin", onComingUsageTime: Date().addingTimeInterval(-420)),
                     Medicine(id: "a8", medicineName: "Fenobarbital", onComingUsageTime: Date().addingTimeInterval(-480))]
    
    var body: some View {
        VStack{
            ForEach(medicines, id: \.self) { medicine in
                Button(action: {
                    self.id = id
                    showingAddEdit.toggle()
                }){
                    MedicineRow2(medicine: medicine)
                        .foregroundColor(.primary)
                }
            }
        }.sheet(isPresented: $showingAddEdit) {
            NavigationView{
                MedicineEdit(medicine: medicines[id])
            }
        }
    }
}


struct MedicineView_Previews: PreviewProvider {
    static var previews: some View {
        MedicineView()
    }
}
