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
                    VStack(alignment: .center){
                        MedicineViewContent()
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
    @State var id: Int = 0
    @State var medicineToDelete: Medicine?
    @ObservedObject var medicineData = MedicineStore()
    
    var body: some View {
        VStack{
            List {
                ForEach(medicineData.medicines, id: \.self) { medicine in
                    /*Button(action: {
                     self.id = id
                     showingAddEdit.toggle()
                     }){
                     MedicineRow2(medicine: medicine)
                     .foregroundColor(.primary)
                     }*/
                    
                    
                    //Edit with navigation link
                    NavigationLink(destination:
                                    MedicineEdit(medicine: medicine,
                                                 medicineName: medicine.medicineName)
                    ){
                        MedicineRow2(medicine: medicine)
                            .foregroundColor(.primary)
                    }
                }.onDelete { index in
                    let medicineToDelete = medicineData.medicine(at: index)
                    self.medicineToDelete = medicineToDelete
                }
            }
        }
        //Edit with sheet
        /*.sheet(isPresented: $showingAddEdit) {
            NavigationView{
                MedicineEdit(medicine: medicineData.medicines[id])
            }
        }*/
        .actionSheet(item: $medicineToDelete){ (medicine) -> ActionSheet in
            ActionSheet(
                title: Text("are_you_sure"),
                message: Text((NSLocalizedString("you_will_delete_medicine", comment: "") + " " + (medicine.medicineName))),
                buttons: [
                    .cancel(Text("cancel")),
                    .destructive(Text("delete"), action: {
                        if let indexSet = medicineData.indexSet(for: medicine){
                            medicineData.delete(at: indexSet)
                        }
                    })
                ])
        }
    }
}


struct MedicineView_Previews: PreviewProvider {
    static var previews: some View {
        MedicineView()
    }
}
