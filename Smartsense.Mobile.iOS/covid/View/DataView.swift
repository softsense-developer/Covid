//
//  DataView.swift
//  covid
//
//  Created by SmartSense on 16.06.2021.
//

import SwiftUI

struct DataView: View {
    var body: some View {
        OtherView()
    }
}

struct OtherView: View {
    
    var body: some View {
        
        GeometryReader { geo in
            VStack(alignment: .leading, spacing: 8){
                HStack(alignment: .top, spacing: 24){
                    
                    VStack(alignment: .center, spacing: 8){
                        ZStack{
                            Circle()
                                .frame(width: geo.size.width / 4, height: geo.size.width / 4)
                                .foregroundColor(.colorPrimary)
                            
                            Image(systemName: "person")
                                .font(.system(size: 45))
                                .foregroundColor(.white)
                        }
                        
                        Text("Profile")
                            .foregroundColor(.primary)
                            .font(.title3)
                    }
                    
                    
                    VStack(alignment: .center, spacing: 8){
                        ZStack{
                            Circle()
                                .frame(width: geo.size.width / 4, height: geo.size.width / 4)
                                .foregroundColor(.colorPrimary)
                            
                            Image(systemName: "person")
                                .font(.system(size: 45))
                                .foregroundColor(.white)
                        }
                        
                        Text("Profile")
                            .foregroundColor(.primary)
                            .font(.title3)
                    }
                    
                    
                    VStack(alignment: .center, spacing: 8){
                        ZStack{
                            Circle()
                                .frame(width: geo.size.width / 4, height: geo.size.width / 4)
                                .foregroundColor(.colorPrimary)
                            
                            Image(systemName: "person")
                                .font(.system(size: 45))
                                .foregroundColor(.white)
                        }
                        
                        Text("Profile")
                            .foregroundColor(.primary)
                            .font(.title3)
                    }
                    
                    
                }
            }
            .padding()
            .frame(width: geo.size.width, height: nil, alignment: .center)
            
        }
        
    }
}

struct DataView_Previews: PreviewProvider {
    static var previews: some View {
        DataView()
    }
}
