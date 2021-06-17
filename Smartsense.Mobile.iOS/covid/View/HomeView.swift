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
                }
            }.navigationTitle("tab_home")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar{
                Button(action: {
                    
                }) {
                    Image(systemName: "person.crop.circle")
                        .accessibilityLabel("User Profile")
                }
            }
        }
        
        
    }
}

struct HomeQuarantineView: View {
    
    @State private var showConnectionAlert = false
    
    var body: some View {
        
        Button(action: {
            
        }, label: {
            ZStack {
                RoundedRectangle(cornerRadius: 15, style: .continuous)
                    .fill(Color.componentColor)
                    .shadow(radius: 0.5)
                    .padding([.leading, .trailing], 1)
                
                HStack(alignment: .center){
                    Image(systemName: "house.fill")
                        .font(.system(size: 25))
                        .foregroundColor(.colorPrimary)
                        .padding(.trailing, 4)
                    
                    VStack(alignment: .leading){
                        Text("home_title")
                            .font(.body)
                            .foregroundColor(.primary)
                        
                        Text("home_hint")
                            .font(.footnote)
                            .foregroundColor(.secondary)
                            .padding(.top, 1)
                    }
                }.padding()
            }
            .padding()
        })
        
        
    }
    
    
    
}
struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        HomeView()
    }
}
