//
//  ContentView.swift
//  Shared
//
//  Created by NICE on 01/06/2021.
//

import SwiftUI
import AppTools
import Foundation

struct TabView: View {
    
    @State var selection : Int = 1
    let imageFontSize : CGFloat = 30.0
    
    var body: some View {
        
        TabView {
            BNFView()
                .tabItem {
                    Image(systemName: "pills")
                        .font(.system(size: imageFontSize, weight: .bold))
                    Text("BNF Browser")
                }.tag(1)
        }
    }
}

