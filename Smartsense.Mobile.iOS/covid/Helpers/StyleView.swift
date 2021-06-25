//
//  StyleView.swift
//  covid
//
//  Created by SmartSense on 25.06.2021.
//

import SwiftUI

struct FilledButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration
            .label
            .font(.title3)
            .foregroundColor(.white)
            .opacity(configuration.isPressed ? 0.3 : 1)
            .padding()
            .background(Color.colorPrimary)
            .cornerRadius(90)
    }
}

struct OutlineButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration
            .label
            .font(.title3)
            .foregroundColor(.colorPrimary)
            .opacity(configuration.isPressed ? 0.3 : 1)
            .padding()
            .background(
                RoundedRectangle(
                    cornerRadius: 90,
                    style: .continuous
                )
            .stroke(Color.colorPrimary)
                
        )
    }
}

struct CapsuleTextFieldStyle: TextFieldStyle {
    func _body(configuration: TextField<Self._Label>) -> some View {
        configuration
            .foregroundColor(.primary)
            .padding()
            .background(Capsule().fill(Color.textFieldBackground))
    }
}

