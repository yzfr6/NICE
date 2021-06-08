//
//  NetworkModel.swift
//  NICE Algorithms
//
//  Created by NICE on 02/06/2021.
//

import Foundation
import Alamofire
import SwiftUI
class NetworkLoaderTracker {
    
    @Published var isLoading : Bool = false;
    
    private var loadingCount : Int = 0;
    
    func decrement() -> Bool{
        loadingCount -= 1;
        if loadingCount < 0 {
            loadingCount = 0;
        }
        return loadingCount > 0
        
    }
    
    func increment(){
        self.loadingCount += 1;
        
    }
}

class NetworkModel : ObservableObject {

    @Published var nlt = NetworkLoaderTracker();
    @Published var isLoading = false;
    let timeoutInterval : Double = 20;
    func refresh() {
        increment()
        decrement()
    }
  
    func increment(){
        isLoading = true;
        nlt.increment()
        self.objectWillChange.send()
    }
    
    func decrement(){
        isLoading = nlt.decrement()
        self.objectWillChange.send()
    }
    
    func pushUpdate() {
        self.objectWillChange.send()
    }
    
}

class AlgorithmNetworkModel : NetworkModel {
    
    static let model = AlgorithmNetworkModel()
    
    @Published var algorithms : [Algorithm] = []
    @Published var selected_algorithm : Algorithm = Algorithm()
    @Published var selected_node : Node = Node()
    
    
    func home_getAlgorithms(){
        increment()
        
        let request = AF.request("http://localhost:8080/NICEPPRESTfulService/algorithms")
        request.responseDecodable(of: Array<Algorithm>.self) { (response) in
            
            guard let algorithms = response.value else {
                print("failed to decode latest algorithms")
                print(response.description)
                self.decrement()
                return
                
            }
            self.algorithms = algorithms;
            
            if self.selected_algorithm.isEmpty() && algorithms.count > 0 {
                self.selected_algorithm = algorithms.first!
            }
            
            //selected.wrappedValue = algorithms.first ?? Algorithm()
            self.pushUpdate()
            self.decrement()
        }
    }
    
    
    func getNodeTargets(node : Node) -> [Node] {
        var nodes : [Node] = Array()
        for edge in selected_algorithm.edges {
            if edge.source.id == node.id {
                nodes.append(edge.target)
            }
        }
        return nodes;
    }
}
