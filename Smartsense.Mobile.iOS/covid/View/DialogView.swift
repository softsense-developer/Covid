//
//  DialogView.swift
//  covid
//
//  Created by SmartSense on 17.06.2021.
//

import SwiftUI

struct DialogView: View {
    var body: some View {
        
        Form{
            Section{
                Text("verification_info")
                    .font(.body)
                    .foregroundColor(.primary)
            }.padding(.vertical, 4.0)
            
            
            Section{
                VStack {
                    Button(action: {
                        
                    }, label: {
                        Text("back_to_login")
                            .frame(maxWidth: .infinity)
                            .frame(height: 40)
                    })
                    Divider()
                    Button(action: {
                        
                    }, label: {
                        Text("email_verified")
                            .frame(maxWidth: .infinity)
                            .frame(height: 40)
                    })
                    
                }
                
            }.padding(.vertical, 4.0)
        }
        
        
    }
}

struct DialogView_Previews: PreviewProvider {
    static var previews: some View {
        DialogView()
    }
}
