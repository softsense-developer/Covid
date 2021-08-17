//
//  RequestRow.swift
//  covid
//
//  Created by SmartSense on 13.08.2021.
//

import SwiftUI

struct RequestRow: View {
    var promotion: Promotion
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack(alignment: .center, spacing: 32){
                
                //Name and surname icon
                Text((promotion.name.prefix(1) + promotion.surname.prefix(1)))
                    .font(.title3)
                    .foregroundColor(.colorPrimary)
                    .background(
                        Circle()
                            .fill(Color.colorPrimary.opacity(0.2))
                            .frame(width: 45, height: 45)
                    )
                
                //Request name surname text start
                VStack(alignment: .leading, spacing: 4){
                    Text((promotion.name + " " + promotion.surname + " " + NSLocalizedString("doctor_request_text", comment: "")))
                        .font(.body)
                        .foregroundColor(.primary)
                    
                }
                //Request name surname text end
                
                
                //Spacer for alignment .leading
                Spacer()
                
            }.padding(.horizontal, 16)
            .padding([.top, .bottom], 8)
            
            
            //Accept refuse buttons view start
            HStack(alignment: .center, spacing: 4){
                Button(action: {
                    
                }, label: {
                    Text("refuse")
                        .foregroundColor(.colorPrimary)
                        .frame(maxWidth: .infinity)
                        .frame(height: 40)
                })
                .padding([.top, .bottom], 4)
                .accentColor(.colorPrimary)
                
                Button(action: {
                    
                }, label: {
                    Text("accept")
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .frame(height: 30)
                })
                .padding([.top, .bottom], 4)
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .fill(Color.colorPrimary)
                )
            }
            //Accept refuse buttons view end
            
        }
    }
}

struct RequestRow_Previews: PreviewProvider {
    static var previews: some View {
        RequestRow(promotion: Promotion(id: 1, userID: 1, name: "Gökhan", surname: "Dağtekin", requestStatus: false))
    }
}
