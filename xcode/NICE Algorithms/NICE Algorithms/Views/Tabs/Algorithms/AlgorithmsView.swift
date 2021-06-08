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
        model.selected_algorithm = Algorithm()
        model.home_getAlgorithms();
    }
    
    var body: some View {
        NavigationView {
            
            VStack (spacing: 2)  {
                
                VStack {
                    HStack {
                        Text("\(model.selected_algorithm.name)".uppercased()).titleFont(size: .STD, color: .blue)
                        Spacer()
                        Text("Nodes: #\(model.selected_algorithm.nodes.count)".uppercased()).titleFont(size: .S, color: .green)
                        Text("Edges: #\(model.selected_algorithm.edges.count)".uppercased()).titleFont(size: .S, color: .green)
                    }
                    HDiv()
                    
                    
                    Picker(selection: $selected_tab, label: Text("")) {
                        Text("NODES").tag(1)
                        Text("EDGES").tag(2)
                        Text("STEP").tag(3)
                    }
                    .pickerStyle(SegmentedPickerStyle())
                    HDiv()
                    VStack {
                        if selected_tab == 1 {
                            NodeView()
                        } else  if selected_tab == 2 {
                            EdgeView()
                        } else  if selected_tab == 3 {
                            if model.selected_node.isEmpty() {
                                Text("Select a node...")
                            } else {
                                NavigationView {
                                    StepView(node : model.selected_node)
                                }
                            }
                            
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


