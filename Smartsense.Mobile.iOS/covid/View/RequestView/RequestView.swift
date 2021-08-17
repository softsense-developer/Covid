//
//  ProfileView.swift
//  covid
//
//  Created by SmartSense on 24.06.2021.
//

import SwiftUI

struct RequestView: View {
    @State private var isHaveRequest = true
    
    var body: some View {
        VStack(alignment: .center){
            if !isHaveRequest{
                VStack(alignment: .center){
                    Spacer()
                    Text("no_request_text")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                        .multilineTextAlignment(.center)
                    Spacer()
                }
            }else{
                VStack(alignment: .center){
                    RequestViewContent()
                }
                
            }
        }.navigationTitle("request")
        .navigationBarTitleDisplayMode(.inline)
    }
}


struct RequestViewContent: View {
    //@ObservedObject var requestData = PromotionStore()
    
    var promotionData = [Promotion(id: 1, userID: 1, name: "Gökhan", surname: "Dağtekin", requestStatus: false),
                         Promotion(id: 2, userID: 2, name: "Ali", surname: "Yılmaz", requestStatus: false)]
    
    var body: some View {
        VStack{
            List {
                ForEach(promotionData, id: \.self) { promotion in
                    RequestRow(promotion: promotion)
                        .foregroundColor(.primary)
                }
            }
        }
    }
}

struct RequestView_Previews: PreviewProvider {
    static var previews: some View {
        RequestView()
    }
}
