//
//  Tabs.swift
//  NICE Algorithms
//
//  Created by NICE on 01/06/2021.
//

import SwiftUI

struct Tabs: View {
    @State var selection = 1;
    var imageFontSize : CGFloat = 30;
    var body: some View {
        TabView (selection: $selection) {
            
            AlgortihmView()
                .tabItem {
                    Image(systemName: "flowchart")
                        .font(.system(size: imageFontSize, weight: .bold))
                    Text("ALGORITHMS")
                }.tag(1)
            
            BNFView()
                .tabItem {
                    Image(systemName: "pills")
                        .font(.system(size: imageFontSize, weight: .bold))
                    Text("BNF")
                }.tag(2)
           
        
        }.navigationViewStyle( StackNavigationViewStyle())
    }
}

