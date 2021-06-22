//
//  MainView.swift
//  covid
//
//  Created by SmartSense on 15.06.2021.
//

import SwiftUI
import Combine

struct MainView: View {
    @State private var selectedTab: Tab = .home
    @State private var otherTabClicked: Bool = false


    @StateObject var appState = AppState()
    @State private var selection = 0
    @State var newListingPresented = false
    
    enum Tab {
        case home
        case connection
        case data
        case medicine
        case other
    }
    var body: some View {
        TabView(selection: $appState.selectedTab){
            HomeView()
                .tabItem {
                    Label("tab_home", systemImage: "house")
                }
                .tag(0)
            ConnectionView()
                .tabItem {
                    Label("tab_connection", systemImage: "bolt.horizontal")
                }
                .tag(1)
            DataView()
                .tabItem{
                    Label("tab_data", systemImage: "list.dash")
                }
                .tag(2)
            MedicineView()
                .tabItem{
                    Label("tab_medicine", systemImage: "doc.on.doc")
                }
                .tag(3)
            
            SheetPresenter(isPresenting: $newListingPresented, content: OtherView())
                .tabItem {
                    Label("tab_other", systemImage: "ellipsis")
                }
                .environmentObject(self.appState)
                .tag(4)
            /*Button("tab_other", action:{
             /*MyKeychain.clear()
             SplashView()*/
             otherTabClicked.toggle()
             }).sheet(isPresented: $otherTabClicked){
             ScrollView{
             OtherView()
             }
             }
             .tabItem {
             Label("tab_other", systemImage: "ellipsis")
             }
             .tag(Tab.other)*/
        }
        .accentColor(.colorPrimary)
        
    }
}

struct SheetPresenter<Content>: View where Content: View {
    @EnvironmentObject var appState: AppState
    @Binding var isPresenting: Bool
    
    var content: Content
    var body: some View {
        Text("")
            .sheet(isPresented: self.$isPresenting, onDismiss: {
                // change back to previous tab selection
                self.appState.selectedTab = self.appState.previousSelectedTab
            }, content: { self.content })
            .onAppear {
                DispatchQueue.main.async {
                    self.isPresenting = true
                    print("New listing sheet appeared with previous tab as tab \(self.appState.selectedTab).")
                }
            }
    }
}
class AppState: ObservableObject {
    private (set) var previousSelectedTab = -1
        @Published var selectedTab: Int = 0 {
            didSet {
                previousSelectedTab = oldValue
            }
        }
}


struct MainView_Previews: PreviewProvider {
    @State static var appState = AppState()
  static var previews: some View {
    MainView(appState: appState)
      MainView(appState: appState)
          .preferredColorScheme(.dark)
    }
}
