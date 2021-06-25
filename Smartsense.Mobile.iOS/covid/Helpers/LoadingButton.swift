//
//  LoadingButton.swift
//  covid
//
//  Created by SmartSense on 25.06.2021.
//

import SwiftUI

public struct LoadingButton<Content: View>: View{
    @Binding var isLoading: Bool
    var style: LoadingButtonStyle
    let content: Content
    var action: () -> () = {}
    
    public init(action: @escaping () -> Void, isLoading: Binding<Bool>, style: LoadingButtonStyle? = nil, @ViewBuilder builder: () -> Content) {
        self._isLoading = isLoading
        self.style = style ?? LoadingButtonStyle()
        content = builder()
        self.action = action
    }
    
    public var body: some View {
        Button(action: {
            if !isLoading {
                action()
            }
            //isLoading = true
        }) {
            ZStack {
                Rectangle()
                    .fill(isLoading ? style.loadingBackgroundColor : style.backgroundColor)
                    .frame(width: isLoading ? style.height : style.width, height: style.height)
                    .cornerRadius(isLoading ? style.height/2 : style.cornerRadius)
                    .animation(.easeInOut)
                if isLoading {
                    CircleLoadingBar(style: style)
                }
                else {
                    VStack {
                        content.animation(.easeInOut)
                    }
                }
            }
        }.frame(width: style.width, height: style.height)
    }
}


/// A Fully Configuarable Button Style
public struct LoadingButtonStyle {
    public init(width: CGFloat? = nil,
                height: CGFloat? = nil,
                cornerRadius: CGFloat? = nil,
                backgroundColor: Color? = nil,
                loadingColor: Color? = nil,
                strokeWidth: CGFloat? = nil,
                strokeColor: Color? = nil) {
        self.width = width ?? 312
        self.height = height ?? 45
        self.cornerRadius = cornerRadius ?? 90
        self.backgroundColor = backgroundColor ?? .colorPrimary
        self.loadingBackgroundColor = loadingColor ?? .colorPrimary
        self.strokeWidth = strokeWidth ?? 5
        self.strokeColor = strokeColor ?? .white
    }
    
    /// Width of button
    public var width: CGFloat = 312
    /// Height of button
    public var height: CGFloat = 54
    /// Corner radius of button
    public var cornerRadius: CGFloat = 27
    /// Background color of button
    public var backgroundColor: Color = .colorPrimary
    /// Background color of button when loading. 50% opacity of background color gonna be set if blank.
    public var loadingBackgroundColor: Color = .colorPrimary
    /// Width of circle loading bar stroke
    public var strokeWidth: CGFloat = 5
    /// Color of circle loading bar stroke
    public var strokeColor: Color = .white
}


struct CircleLoadingBar: View {
    @State private var isLoading = false
    @State var style: LoadingButtonStyle
    
    var body: some View {
        Circle()
            .trim(from: 0, to: 0.7)
            .stroke(style.strokeColor, style: StrokeStyle(lineWidth: style.strokeWidth, lineCap: .round, lineJoin: .round))
            .frame(width: style.height - 20, height: style.height - 20)
            .rotationEffect(Angle(degrees: isLoading ? 360 : 0))
            .animation(Animation.default.repeatForever(autoreverses: false))
            .onAppear() {
                self.isLoading = true
            }
    }
}

