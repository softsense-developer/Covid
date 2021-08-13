//
//  ProfileView.swift
//  covid
//
//  Created by SmartSense on 24.06.2021.
//

import SwiftUI

struct CompanionView: View {
    @State private var isHaveCompanion = true
    @State private var showingAddEdit = false
    
    var body: some View {
        VStack(alignment: .center){
            if !isHaveCompanion{
                VStack(alignment: .center){
                    Spacer()
                    Text("no_companion")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                        .multilineTextAlignment(.center)
                    Spacer()
                }
            }else{
                VStack(alignment: .center){
                    CompanionViewContent()
                }
                
            }
        }
        .navigationTitle("companion")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar{
            ToolbarItem(placement: .navigationBarTrailing) {
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
        .sheet(isPresented: $showingAddEdit) {
            NavigationView{
                CompanionAdd()
            }
        }
    }
}

struct CompanionViewContent: View {
    @State private var showingAddEdit = false
    @State var id: Int = 0
    @State var companionToDelete: Companion?
    @ObservedObject var companionData = CompanionStore()
    
    var body: some View {
        VStack{
            List {
                ForEach(companionData.companions, id: \.self) { companion in
                    //Edit with button
                    /*Button(action: {
                     self.id = id
                     showingAddEdit.toggle()
                     }){
                     CompanionRow(companion: companion)
                     .foregroundColor(.primary)
                     }*/
                    
                    //Edit with navigation link
                    /*NavigationLink(destination:
                     CompanionEdit(companion: companion,
                     name: companion.name,
                     surname: companion.surname,
                     email: companion.email,
                     password: companion.password)){
                     CompanionRow(companion: companion)
                     }*/
                    
                    CompanionRow(companion: companion)
                        .foregroundColor(.primary)
                }.onDelete { index in
                    let companionToDelete = companionData.companion(at: index)
                    self.companionToDelete = companionToDelete
                    
                }
            }
        }
        //Edit with button
        /*.sheet(isPresented: $showingAddEdit) {
         NavigationView{
         //CompanionEdit(companion: companionData.companions[0], name: companionData.companions, surname: "", email: "", password: "")
         }
         }*/.actionSheet(item: $companionToDelete){ (companion) -> ActionSheet in
            ActionSheet(
                title: Text("companion_delete"),
                message: Text((NSLocalizedString("you_will_delete_companion", comment: "") + " " + ((companion.name) + " " + (companion.surname)))),
                buttons: [
                    .cancel(Text("cancel")),
                    .destructive(Text("delete"), action: {
                        if let indexSet = companionData.indexSet(for: companion){
                            companionData.delete(at: indexSet)
                        }
                    })
                ])
         }
    }
}

struct CompanionView_Previews: PreviewProvider {
    static var previews: some View {
        CompanionView()
    }
}
