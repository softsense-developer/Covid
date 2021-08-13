//
//  CompanionRow.swift
//  covid
//
//  Created by SmartSense on 12.08.2021.
//

import SwiftUI

struct CompanionRow: View {
    var companion: Companion
    
    var body: some View {
        HStack(alignment: .center, spacing: 32){
            
            //Name and surname icon
            Text((companion.name.prefix(1) + companion.surname.prefix(1)))
                .font(.title3)
                .foregroundColor(.colorPrimary)
                .background(
                    Circle()
                        .fill(Color.colorPrimary.opacity(0.2))
                        .frame(width: 45, height: 45)
                )
            
            //Companion name and email view start
            VStack(alignment: .leading, spacing: 4){
                Text(companion.name + " " + companion.surname)
                    .font(.body)
                    .foregroundColor(.primary)
                
                Text(companion.email)
                    .font(.body)
                    .foregroundColor(.secondary)
                
            }
            //Companion name and email view end
            
            //Spacer for alignment .leading
            Spacer()
            
        }.padding(.horizontal, 16)
        .padding([.top, .bottom], 8)
        
        //Divider().padding(.leading, 64)
    }
    
}

struct CompanionRow_Previews: PreviewProvider {
    static var previews: some View {
        CompanionRow(companion: Companion(id: 1, name: "Gökhan", surname: "Dağtekin", email: "gdagtekin23@gmail.com", password: "123456"))
    }
}
