//
//  AlgorithmsView.swift
//  NICE Algorithms
//
//  Created by NICE on 02/06/2021.
//

import Foundation
import SwiftUI

struct AlgortihmView : View {
    
    @ObservedObject var model = AlgorithmNetworkModel.model;
    @State var selected_tab = 1
    init (){
        refresh()
    }
    
    func refresh() {
        model.home_getAlgorithms();
    }
    
    var body: some View {
        NavigationView {
            
            VStack (spacing: 2)  {
                
                
                VStack {
                    
                    Text("\(model.selected_algorithm.name)".uppercased()).titleFont(size: .STD)
                    
                    Picker(selection: $selected_tab, label: Text("")) {
                        Text("Nodes").tag(1)
                        Text("Edges").tag(2)
                        Text("Step").tag(3)
                    }
                    .pickerStyle(SegmentedPickerStyle())
                    
                    VStack {
                        if selected_tab == 1 {
                            NodeView()
                        } else  if selected_tab == 2 {
                            Text("Edges")
                        } else  if selected_tab == 3 {
                            Text("Step through")
                        }
                    }
                    Spacer()
                }
            }
            
            .padding(10)
            .navigationBarTitle("ALGORITHMS", displayMode: .inline)
            .navigationBarItems(
                leading:
                    Menu("Algorithm") {
                        if (model.algorithms.count == 0) {
                            Text("No Algorithms")
                        }
                        ForEach(model
                                    .algorithms, id :\.self){algorithm in
                            Button(model.selected_algorithm.name) {
                                model.selected_algorithm = algorithm
                            }
                        }
                        
                    },
                trailing:
                    HStack {
                        if model.isLoading {
                            ProgressView()
                        } else {
                            Button(action: {
                                refresh()
                            }) {
                                HStack {
                                    Image(systemName: "arrow.triangle.2.circlepath");
                                }
                            }
                        }
                    }
            )
        }.navigationViewStyle(StackNavigationViewStyle())
    }
}


